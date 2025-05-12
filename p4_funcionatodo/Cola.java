import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class Cola {
    private final String nombre;  // Nombre de la cola (y directorio de almacenamiento)
    
    // Cola de mensajes en memoria (FIFO, thread-safe)
    private final BlockingQueue<Message> messages = new LinkedBlockingQueue<>();

    // Lista de consumidores registrados
    private final List<Callback> consumidores = new ArrayList<>();

    // Ejecutores para diferentes tareas en segundo plano
    private final ExecutorService controlador = Executors.newSingleThreadExecutor();
    private final ExecutorService hilos = Executors.newCachedThreadPool();
    private final ScheduledExecutorService limpiador = Executors.newSingleThreadScheduledExecutor();
    

    // Mapas para llevar control del uso de consumidores
    private final Map<Callback, Integer> contadorMensajes = new ConcurrentHashMap<>();
    private final Set<Callback> consumidoresOcupados = ConcurrentHashMap.newKeySet();

     // Locks para sincronizar acceso a consumidores
    private final ReentrantLock lockConsumidores = new ReentrantLock();

    // Persistencia de mensajes (para reinicio o recuperación)
    private final File dir;
    private final Map<String, File> persistedMessages = new ConcurrentHashMap<>();

    // Constructor principal
    public Cola(String nombre) {
        this.nombre = nombre;
        this.dir = new File(nombre);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        limpiarArchivosCorruptos(); // Eliminar mensajes corruptos (no deserializables)
        obtenerMensajesPersistidos();  // Cargar mensajes pendientes en memoria

        // Limpieza periódica: elimina mensajes con más de 5 minutos sin procesar
        limpiador.scheduleAtFixedRate(() -> {
            long ahora = System.currentTimeMillis();
            persistedMessages.entrySet().removeIf(entry -> {
                long modificado = entry.getValue().lastModified();
                if (ahora - modificado > 300000) {
                    entry.getValue().delete();
                    return true;
                }
                return false;
            });
        }, 1, 1, TimeUnit.MINUTES);

        // Dispatcher que distribuye mensajes a consumidores disponibles
        controlador.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Message msg = messages.take(); // Espera bloqueante
                    Callback seleccionado = null;
        
                    lockConsumidores.lock(); //Se bloquea el acceso a la lista de consumidores para evitar condiciones de carrera.
                    try {
                        int min = Integer.MAX_VALUE;
                        //Se busca el consumidor no ocupado con la menor cantidad de mensajes procesados 
                        for (Callback c : consumidores) {
                            if (!consumidoresOcupados.contains(c)) {
                                int cuenta = contadorMensajes.getOrDefault(c, 0);
                                if (cuenta < min) {
                                    min = cuenta;
                                    seleccionado = c;
                                }
                            }
                        }
        
                        if (seleccionado == null) {
                            // nadie libre: reinsertar el mensaje y esperar
                            messages.put(msg);
                            Thread.sleep(100);
                            continue;
                        }
        
                        consumidoresOcupados.add(seleccionado); //Se marca como ocupado
                        contadorMensajes.put(seleccionado, contadorMensajes.get(seleccionado) + 1); //Se incrementa su contador de carga.
        
                    } finally {
                        lockConsumidores.unlock();
                    }
        
                    Callback finalSeleccionado = seleccionado;
                    Message finalMsg = msg;
        
                    //Se lanza un hilo para procesar el mensaje en paralelo
                    hilos.submit(() -> {
                        boolean procesado = false;
                        try {
                            finalSeleccionado.procesarMensaje(finalMsg.getId(), finalMsg.getContenido(), nombre);
                            procesado = true;
                        } catch (Exception e) {
                            //Si el consumidor ya no está, se elimian y de reencola el mensaje
                            System.out.println("Consumidor inactivo, se elimina.");
                            try {
                                messages.put(finalMsg); // Reenviar
                            } catch (InterruptedException ie) {
                                Thread.currentThread().interrupt();
                            }
                            eliminarConsumidor(finalSeleccionado);
                        } finally {    
                            if (procesado) {
                                // Marcar el consumidor como libre otra vez
                                consumidoresOcupados.remove(finalSeleccionado);
                            } else {
                                // Decrementar contador si el mensaje no fue realmente procesado
                                contadorMensajes.put(finalSeleccionado, contadorMensajes.get(finalSeleccionado) - 1);
                            }
                        }
                    });
        
                } catch (InterruptedException e) {
                    //Sale del bucle cuando se interrumpe el hilo (al cerrar la cola)
                    System.out.println("Dispatcher interrumpido, terminando hilo.");
                    Thread.currentThread().interrupt(); 
                    break; 
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public String getNombre() {
        return this.nombre;
    }

    //Eliminar consumidor si se "corta" el proceso
    private void eliminarConsumidor(Callback callback) {
        lockConsumidores.lock();
        try {
            consumidores.remove(callback);
            contadorMensajes.remove(callback);
            consumidoresOcupados.remove(callback);
            System.out.println("Consumidor eliminado por error de comunicación.");
        } finally {
            lockConsumidores.unlock();
        }
    }

    // Publicar nuevo mensaje en la cola (y persistirlo en disco)
    public void publicar(String contenido) {
        try {
            Message mensaje = new Message(contenido);
            messages.put(mensaje); // Enviar a cola en memoria
    
            // Guardar en disco
            File f = new File(dir, mensaje.getId() + ".msg");
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f))) {
                out.writeObject(mensaje);
                persistedMessages.put(mensaje.getId(), f);
            }
        } catch (Exception e) {
            System.out.println("Error publicando mensaje: " + e.getMessage());
        }
    }
    

    // Registrar consumidor si aún no está registrado
    public void consumir(Callback callback) {
        lockConsumidores.lock();
        try {
            if (!consumidores.contains(callback)) {
                consumidores.add(callback);
                contadorMensajes.put(callback, 0);
            }            
        } finally {
            lockConsumidores.unlock();
        }
    }

    // Confirmar recepción del mensaje y eliminar de disco
    public void ack(String idMensaje) {
        File f = persistedMessages.remove(idMensaje);
        if (f != null && f.exists()) {
            f.delete();
            System.out.println("ACK recibido del mensaje " + idMensaje + ".");
        } else {
            System.out.println("ACK recibido del mensaje " + idMensaje + ".");
        }
    }
    
    
    // Obtener mensajes almacenados en disco al iniciar
    private void obtenerMensajesPersistidos() {
        File[] archivos = dir.listFiles((dir, name) -> name.endsWith(".msg"));
        if (archivos != null) {
            for (File archivo : archivos) {
                try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(archivo))) {
                    Message mensaje = (Message) in.readObject();
                    messages.put(mensaje);
                    persistedMessages.put(mensaje.getId(), archivo);
                } catch (Exception e) {
                    System.out.println("Error cargando mensaje: " + e.getMessage());
                }
            }
        }
    }

    // Eliminar archivos corruptos que no se pueden deserializar
    private void limpiarArchivosCorruptos() {
        File[] archivos = dir.listFiles((dir, name) -> name.endsWith(".msg"));
        if (archivos != null) {
            for (File archivo : archivos) {
                try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(archivo))) {
                    Message m = (Message) in.readObject(); // Verifica que se puede leer
                } catch (Exception e) {
                    System.out.println("Archivo corrupto eliminado: " + archivo.getName());
                    archivo.delete(); // Elimina si no se puede deserializar
                }
            }
        }
    }

    // Apagar hilos al eliminar la cola
    public void shutdown() {
        controlador.shutdownNow();
        limpiador.shutdownNow();
        hilos.shutdownNow();
    }
    
}
