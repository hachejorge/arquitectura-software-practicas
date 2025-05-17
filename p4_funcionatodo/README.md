// Para compilar todas las clases
javac .java

// Para ejecutar, previamente ejecutar "rmiregistry 32003 &" en la máquinas del bróker
-En la máquina del Broker: 
	1) ejecutar "rmiregistry <puerto> &"
	2) ejecutar "java Broker <ip_maquina:puerto>

-En las máquinas de los clientes:
	1) ejecutar java CLASE (ConcreteConsumer, ConcreteProducer, Admin…)
	2) Aparecerá un mensaje en la terminal para introducir por teclado <ip_maquina:puerto>