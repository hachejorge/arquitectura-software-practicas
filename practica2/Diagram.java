import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

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
	
	private Vector<Class> classes = new Vector(); //las clases que crea el usuario
	private Vector<Association> associations = new Vector(); // las asociaciones que crea el usuario
	
	private int margen = 10;
	private int num_clase = 1;
	private int antigua_x;
	private int antigua_y;

	// Crear asociaciones
	private Boolean creadoAsociacion = false;

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
	}
	
	/********************************************/
	/** MÈtodos de MouseListener               **/
	/********************************************/

	public void mousePressed(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)){
			int mouseX= e.getX();
			int mouseY = e.getY();
			for(Class c : classes){
				if((c.getX() <= mouseX && (c.getX() + c.getWidth()) >= mouseX ) && (c.getY() <= mouseY && (c.getY() + c.getHeight()) >= mouseY) ){
					clasePulsada = c;		
				}
			}
		}
   	}
    
	public void mouseReleased(MouseEvent e) {
		if(SwingUtilities.isLeftMouseButton(e)) {
			int mouseX= e.getX();
			int mouseY = e.getY();
			
			if(clasePulsada != null) {
				clasePulsada.setX(mouseX);
				clasePulsada.setY(mouseY);
				repaint();
				clasePulsada = null;
			}
		}
	}
    
	public void mouseEntered(MouseEvent e) {
		
	}
    
	public void mouseExited(MouseEvent e) {
    }
    
	public void mouseClicked(MouseEvent e) {
		if (SwingUtilities.isRightMouseButton(e)) {
            int mouseX= e.getX();
			int mouseY = e.getY();
			antigua_x = mouseX;
			antigua_y = mouseY;
			Class clase_g = null;
			for(Class c : classes){
				if( (c.getX() <= mouseX && (c.getX() + c.getWidth()) >= mouseX ) && (c.getY() <= mouseY && (c.getY() + c.getHeight()) >= mouseY) ){
					clase_g = c;
				}
			}
			if (clase_g != null) {
				classes.remove(clase_g);
				window.updateNClasses(this);
				repaint();
				margen = 10;
				if(clase_g == claseSeleccionada) {
					creadoAsociacion = true;
				}
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
			if((c.getX() <= mouseX && (c.getX() + c.getWidth()) >= mouseX ) && (c.getY() <= mouseY && (c.getY() + c.getHeight()) >= mouseY) ){
				claseSobreVolada = c;
				hayAlguna = true;		
			} 
		}
		if (!hayAlguna) {
			claseSobreVolada = null;
		}
		if(claseSobreVolada != null) {
			int index = classes.indexOf(claseSobreVolada);
			if (index != -1) {
				classes.remove(index);
				classes.add(claseSobreVolada);
				repaint();	
			}
		}
	}
    
	public void mouseDragged(MouseEvent e) {
		if(clasePulsada != null) {
			clasePulsada.setX(e.getX());
			clasePulsada.setY(e.getY());
			repaint();
			if( creadoAsociacion ) {
				
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
			if( claseSeleccionada == null && claseSobreVolada != null) {
				claseSobreVolada.colorFondoAzul();
				claseSeleccionada = claseSobreVolada;
			} else if (claseSeleccionada != null) {
				if (claseSeleccionada == claseSobreVolada || claseSobreVolada == null) {
					claseSeleccionada.colorFondoBlanco();
					claseSeleccionada = null;
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
