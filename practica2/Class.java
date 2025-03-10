import java.awt.*;
//otros import
import java.util.Vector;

public class Class {
	
	//Atributos
	private String name;
	private Vector<String> attributes;
	private Vector<String> operations; 
	private int x, y, width = 120, height = 80;

    public Class(){

    }

	public Class(int n_class, int x, int y) {
        this.name = "Class " + n_class;
        this.attributes = new Vector<>();  // Asegurarse de inicializar los atributos
        this.operations = new Vector<>();  // Asegurarse de inicializar las operaciones
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        if (g == null) return; // Evitar error si Graphics es null
        Graphics2D g2 = (Graphics2D) g;

        // Dibujar el rectángulo
        g2.setColor(Color.WHITE);
        g2.fillRect(x, y, width, height);
        g2.setColor(Color.BLACK);
        g2.drawRect(x, y, width, height);

        // Dibujar el nombre de la clase
        g2.drawString(name, x + 10, y + 15);

        // Dibujar líneas divisorias
        g2.drawLine(x, y + 20, x + 120, y + 20);
        g2.drawString("Atributos", x + 10, y + 40);

        g2.drawLine(x, y + 50, x + 120, y + 50);
        g2.drawString("Métodos", x + 10, y + 70);

    }

	public void undraw(Graphics g) {
        if (g == null) return;
        g.setColor(Color.WHITE); // Color de fondo para "borrar"
        g.fillRect(x, y, width, height); // Rellena el área con el color del fondo
    }

	public int getX(){
        return this.x;
    }	

    public int getY(){
        return this.y;
    }

    public int getWidth(){
        return this.width;
    }

    public int getHeight(){
        return this.height;
    }

	// …
}
