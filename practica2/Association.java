import java.awt.Graphics;
import java.awt.Graphics2D;
//otros imports …


public class Association {

	// Atributos
	Class claseOrigen, claseDestino;
	
	int xOrigen, yOrigen, xDestino, yDestino;

	public Association(Class claseOrigen, int xOrigen, int yOrigen) {
		this.claseOrigen = claseOrigen;
		this.xOrigen = xOrigen;
		this.yOrigen = yOrigen;
	}
	
	public void draw(Graphics graphics) {
		// Dibuja la asociación
		Graphics2D graphics2d = (Graphics2D)graphics;

		// ...
	}

	// Más métodos
	// ...

	public void setFinal(int x, int y) {
		xDestino = x;
		yDestino = y;
	}
}
