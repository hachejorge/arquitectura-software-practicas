import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.List;

public class Diagram 
		extends JPanel 
		implements MouseListener, 
			   MouseMotionListener, 
			   KeyListener {
	
	//atributos
	private Window window; //Ventana en la que está el diagrama
	public Class clase; 
	public Class clasePulsada;
	public Class claseSobreVolada;
	public Class claseSeleccionada;
	public Class claseEnVerde;
	
	private Vector<Class> classes = new Vector(); //las clases que crea el usuario
	private Vector<Association> associations = new Vector(); // las asociaciones que crea el usuario
	
	private int margen = 10;
	private int num_clase = 1;
	private int antigua_x;
	private int antigua_y;

	// Crear asociaciones
	private Boolean creandoAsociacion = false;
	private Association asociacionEnCurso = null;

	//metodos
	public Diagram(Window theWindow) {
		window = theWindow;
		
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		
		setBorder(BorderFactory.createLineBorder(Color.black));

	}
	
	public void addClass() {
		clase = new Class(num_clase, margen, margen);
		num_clase++;
		// Añade una clase al diagrama
		classes.add(clase);
		repaint();
		margen += 10;
	}
			
	//Devuelve el número de clases
	public int getNClasses(){
		return classes.size();
	}
	
	//Devuelve el numero de asociaciones
	public int getNAssociations(){
		return associations.size();
	}

	public void paint(Graphics g) {
		super.paint(g);
		for (Class c : classes) {
			c.draw(g);
		}
		for (Association a : associations){
			a.draw(g);
		}
		if (asociacionEnCurso != null) { asociacionEnCurso.draw(g); }
	}
	
	/********************************************/
	/** MÈtodos de MouseListener               **/
	/********************************************/

	public void mousePressed(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)){
			int mouseX= e.getX();
			int mouseY = e.getY();
			
			for(Class c : classes){
				if(c.contienteElPunto(mouseX, mouseY)){
					clasePulsada = c;	
					antigua_x = mouseX - clasePulsada.getX();
					antigua_y = mouseY- clasePulsada.getY();	

					// Si estaba seleccionaa se inicia una asociación
					if (claseSeleccionada != null && claseSeleccionada == clasePulsada) {
						creandoAsociacion = true;
						asociacionEnCurso = new Association(clasePulsada, mouseX, mouseY);
					} 
				}
			}
			
		}
   	}
    
	public void mouseReleased(MouseEvent e) {
		if (creandoAsociacion) {
			int mouseX = e.getX();
			int mouseY = e.getY();
			Class claseDestino = null;

			// Verificar si soltamos sobre otra clase
			for (Class c : classes) {
				if(c.contienteElPunto(mouseX, mouseY)) {
					claseDestino = c;
					break;
				}
			}

			Boolean permitir = true;
			Class origen = asociacionEnCurso.getClaseOrigen();
			for (Association a: associations) {
				if (a != asociacionEnCurso) {
					if((a.getClaseOrigen()==origen && a.getClaseDestino() == claseDestino) || a.getClaseOrigen()==claseDestino && a.getClaseDestino() == origen ) {
						permitir = false;
					}
				}
			}
			// Si se suelta sobre una clase válida, se crea la asociación
			if (claseDestino != null && permitir) {
				asociacionEnCurso.setFinal(claseDestino.getX() + claseDestino.getWidth() / 2,
						claseDestino.getY() + claseDestino.getHeight() / 2);
				asociacionEnCurso.setDestino(claseDestino);

				// Si son las mismas clases es una asociación recursiva
				if(claseSeleccionada == claseDestino) {
					asociacionEnCurso.setEsRecursiva();
				}

				associations.add(asociacionEnCurso);
				window.updateNAssociations(this);
				
			} 
			
			// Reiniciar estados
			if (claseSeleccionada != null) {
				claseSeleccionada.colorFondoBlanco();
			}
			if (claseDestino != null) {
				claseDestino.colorFondoBlanco();
			}
			claseSeleccionada = null;
			asociacionEnCurso = null;
			creandoAsociacion = false;

			repaint();
		}
		
	}
    
	public void mouseEntered(MouseEvent e) {
	
	}
    
	public void mouseExited(MouseEvent e) {
    
	}
    
	public void mouseClicked(MouseEvent e) {
		// Se eliminan la clase
		if (SwingUtilities.isRightMouseButton(e)) {
			int mouseX= e.getX();
			int mouseY = e.getY();
			antigua_x = mouseX;
			antigua_y = mouseY;

			Class clase_g = null;
			for(Class c : classes){
				if( (c.contienteElPunto(mouseX, mouseY)) ){
					clase_g = c;
				}
			}
			if (clase_g != null) {
				classes.remove(clase_g);
				
				// Se borran las asociaciones vinculadas a la clase
				List<Association> asociacionesAEliminar = new ArrayList<>();
				for (Association a : associations) {
					if (a.involves(clase_g)) {
						asociacionesAEliminar.add(a);
					}
				}
				associations.removeAll(asociacionesAEliminar);
				
				window.updateNClasses(this);
				window.updateNAssociations(this);

				
				repaint();
				margen = 10;
			} 
		} 
    }

	/********************************************/
	/** Métodos de MouseMotionListener         **/
	/********************************************/    
	public void mouseMoved(MouseEvent e) {
		Boolean hayAlguna = false;
		int mouseX= e.getX();
		int mouseY = e.getY();
		for(Class c : classes){
			if(c.contienteElPunto(mouseX, mouseY)){
				claseSobreVolada = c;
				hayAlguna = true;		
			} 
		}
		if (!hayAlguna) {
			claseSobreVolada = null;
		}
		// Si hay alguna sobrevolada 
		if(claseSobreVolada != null) {
			// Adelanta la clase hasta el principio
			int index = classes.indexOf(claseSobreVolada);
			if (index != -1) {
				classes.remove(index);
				classes.add(claseSobreVolada);
				repaint();	
			}
		}
	}
    
	public void mouseDragged(MouseEvent e) {
		if(SwingUtilities.isLeftMouseButton(e)) {
			if(clasePulsada != null) {
				// Estoy creando una asociación
				if( creandoAsociacion ) {
					asociacionEnCurso.setFinal(e.getX(), e.getY());
					Class clasePotencialFinal = null;
					for(Class c : classes){
						if(c.contienteElPunto(e.getX(), e.getY())){
							clasePotencialFinal = c;
						} 
					}
					
					// Hay una clase candidata a final y no la propia
					if(clasePotencialFinal != null && clasePotencialFinal != claseSeleccionada) {
						// No había un candidadto previo
						if(claseEnVerde == null){
							clasePotencialFinal.colorFondoVerde();
							claseEnVerde = clasePotencialFinal;

						// Había un candidadto previo que no era el actual
						} else if (claseEnVerde != clasePotencialFinal) {
							claseEnVerde.colorFondoBlanco();
							clasePotencialFinal.colorFondoVerde();
							claseEnVerde = clasePotencialFinal;
						}

					// No hay una clase candidata a final
					} else {
						// Había una en verde
						if(claseEnVerde != null){
							claseEnVerde.colorFondoBlanco();
							claseEnVerde = null;
						}
					}

				// No estoy creando una asociación y muevo la clase seleccionada
				} else {
					clasePulsada.setX(e.getX() - antigua_x);
					clasePulsada.setY(e.getY() - antigua_y);
					// Muevo también sus nuevas asociaciones
					for(Association a : associations){
						if(a.involves(clasePulsada)) {
							if(a.esRecursiva()) {
								a.setOrigen(clasePulsada.getX(), clasePulsada.getY());
							} else {
								Point p = a.getPuntoEnBorde(clasePulsada, e.getX(), e.getY());
								if(a.involvesOrigen(clasePulsada)){
									a.setOrigen((int)p.getX(), (int)p.getY());
								} else {
									a.setFinal((int)p.getX(), (int)p.getY());
								}
							}
						}
					}
				}
				repaint();
			}
		}
	}
    
	/********************************************/
	/** Métodos de KeyListener                 **/
	/********************************************/

	public void keyTyped(KeyEvent e) {
    	
	}
    
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_S) {
			// Si no había ninguna seleccionada y hay una sobrevolada se selecciona
			if( claseSeleccionada == null && claseSobreVolada != null) {
				claseSobreVolada.colorFondoAzul();
				claseSeleccionada = claseSobreVolada;

			// Si ya había una seleccionada
			} else if (claseSeleccionada != null) {
				// Si la que estoy sobrevolando estaba seleccionada se quita
				if (claseSeleccionada == claseSobreVolada || claseSobreVolada == null) {
					claseSeleccionada.colorFondoBlanco();
					claseSeleccionada = null;

				// Si no se quita la que estuviera y se pone la nueva e azul
				} else {
					claseSeleccionada.colorFondoBlanco();
					claseSeleccionada = claseSobreVolada;
					claseSeleccionada.colorFondoAzul();
				}
			} 
			repaint();
		}
	}
    
    public void keyReleased(KeyEvent e) {
		
	}
}
