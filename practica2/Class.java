import java.awt.*;
//otros import
import java.util.Vector;

public class Class {
	
	//Atributos
	private String name;
	private int x, y, width = 120, height = 80;

    private Color colorFondo = Color.WHITE;
    private Color colorBordes = Color.BLACK;

    public Class(){}

	public Class(int n_class, int x, int y) {
        this.name = "Class " + n_class;
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        if (g == null) return; // Evitar error si Graphics es null
        Graphics2D g2 = (Graphics2D) g;

        // Dibujar el rectángulo
        g2.setColor(colorFondo);
        g2.fillRect(x, y, width, height);
        g2.setColor(colorBordes);
        g2.drawRect(x, y, width, height);

        // Dibujar el nombre de la clase
        g2.drawString(name, x + 10, y + 15);

        // Dibujar líneas divisorias
        g2.drawLine(x, y + 20, x + 120, y + 20);
        g2.drawString("Atributos", x + 10, y + 40);

        g2.drawLine(x, y + 50, x + 120, y + 50);
        g2.drawString("Métodos", x + 10, y + 70);

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

    public void setX(int _x){
        this.x = _x;
    }
        
    public void setY(int _y){
        this.y = _y;
    }

    public void colorFondoAzul(){
        colorFondo = Color.CYAN;
    }

    public void colorFondoBlanco(){
        colorFondo = Color.WHITE;
    }

    public void colorFondoVerde(){
        colorFondo = Color.GREEN;
    }

    public Boolean contienteElPunto(int x, int y) {
        return (this.getX() <= x) && (this.getX() + this.getWidth() >= x)  && (this.getY() <= y) && (this.getY() + this.getHeight() >= y);
    }
}
