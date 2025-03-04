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
	
	private Vector<Class> classes = new Vector(); //las clases que crea el usuario
	private Vector<Association> associations = new Vector(); // las asociaciones que crea el usuario
	
	private int margen = 10;
	// ... (otros posibles atributos)


	//metodos
	public Diagram(Window theWindow) {
		window = theWindow;
		
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		
		setBorder(BorderFactory.createLineBorder(Color.black));
	}
	
	public void addClass() {
		clase = new Class(classes.size() + 1, margen, margen);
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
		if (SwingUtilities.isRightMouseButton(e)) {
            e.getX();
			e.getY();

			Class clase_g = new Class();
			for(Class c : classes){
				if( (c.getX() <= e.getX() && (c.getX() + c.getWidth()) >= e.getX() ) && (c.getY() <= e.getY() && (c.getY() + c.getHeight()) >= e.getY()) ){
					clase_g = c;
				}
			}
			if( clase_g != null) {
				classes.remove(clase_g);
				clase_g.undraw(getGraphics());
				repaint();
			}
        }
   	 }
    
    	public void mouseReleased(MouseEvent e) {
 		//…		
    	}
    
	    public void mouseEntered(MouseEvent e) {
    	}
    
	public void mouseExited(MouseEvent e) {
    	}
    
	public void mouseClicked(MouseEvent e) {
    	}

	/********************************************/
	/** MÈtodos de MouseMotionListener         **/
	/********************************************/    
    	public void mouseMoved(MouseEvent e) {
		//…
	}
    
	public void mouseDragged(MouseEvent e) {
		//…
	}
    
	/********************************************/
	/** Métodos de KeyListener                 **/
	/********************************************/

	public void keyTyped(KeyEvent e) {
    	}
    
	public void keyPressed(KeyEvent e) {
		//…
	}
    
    	public void keyReleased(KeyEvent e) {
    	}
}
