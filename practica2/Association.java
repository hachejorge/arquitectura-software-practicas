import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
//otros imports …
import java.awt.Point;


public class Association {

	// Atributos
	private Class claseOrigen, claseDestino;
	private Boolean esRecursiva;
	private int xOrigen, yOrigen, xDestino, yDestino;

	public Association(Class claseOrigen, int xOrigen, int yOrigen) {
		this.claseOrigen = claseOrigen;
		this.xOrigen = xOrigen;
		this.yOrigen = yOrigen;
		this.claseDestino = null;
		this.esRecursiva = false;
	}

	public void draw(Graphics g) {
		// Dibuja la asociación
		Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));

        // Si la asociación es una auto-asociación
        if (claseDestino != null && claseDestino == claseOrigen) {
            int squareSize = 30;  // Tamaño del cuadrado
			int cx = xOrigen;     // Coordenada X de la clase
			int cy = yOrigen;     // Coordenada Y de la clase

			// Dibujar la figura cuadrada en el borde de la clase
			g2.drawLine(cx + 45, cy + 80 + squareSize, cx + 45 + squareSize, cy + 80 + squareSize);
			g2.drawLine(cx + 45, cy + 80, cx + 45, cy + 80 + squareSize);
			g2.drawLine(cx + 45 + squareSize, cy + 80, cx + 45 + squareSize, cy + 80 + squareSize);

        // Si la asociación es entre dos clases
        } else {
            // Dibujar línea normal entre clases
            g2.drawLine(xOrigen, yOrigen, xDestino, yDestino);
        }
	}

    public Class getClaseOrigen() {
        return this.claseOrigen;
    }

    public Class getClaseDestino() {
        return this.claseDestino;
    }

	public void setOrigen(int x, int y) {
		xOrigen = x;
		yOrigen = y;
	}

	public void setFinal(int x, int y) {
		xDestino = x;
		yDestino = y;
	}

	public void setDestino(Class claseDestino) {
        this.claseDestino = claseDestino;
		Point inicio = getPuntoEnBorde(claseOrigen, claseDestino.getX(), claseDestino.getY());
        Point fin = getPuntoEnBorde(claseDestino, claseOrigen.getX(), claseOrigen.getY());
		
		this.xOrigen = inicio.x;
        this.yOrigen = inicio.y;
        this.xDestino = fin.x;
        this.yDestino = fin.y;
    }

	public boolean involves(Class c) {
        return claseOrigen == c || claseDestino == c;
    }

	public boolean involvesOrigen(Class c) {
		return claseOrigen == c;
	}

	public Point getPuntoEnBorde(Class clase, int xObjetivo, int yObjetivo) {
        int cx = clase.getX() + clase.getWidth() / 2; // Centro de la clase
        int cy = clase.getY() + clase.getHeight() / 2;

        int xIzq = clase.getX();
        int xDer = clase.getX() + clase.getWidth();
        int yArriba = clase.getY();
        int yAbajo = clase.getY() + clase.getHeight();

        // Calcular la dirección de la línea
        double dx = xObjetivo - cx;
        double dy = yObjetivo - cy;
        double m = dy / dx; // Pendiente de la recta

        int xBorde, yBorde;

        if (Math.abs(m) <= (double) clase.getHeight() / clase.getWidth()) {
            // Interseca con borde izquierdo o derecho
            if (dx > 0) {
                xBorde = xDer;
                yBorde = (int) (cy + m * (xBorde - cx));
            } else {
                xBorde = xIzq;
                yBorde = (int) (cy + m * (xBorde - cx));
            }
        } else {
            // Interseca con borde superior o inferior
            if (dy > 0) {
                yBorde = yAbajo;
                xBorde = (int) (cx + (yBorde - cy) / m);
            } else {
                yBorde = yArriba;
                xBorde = (int) (cx + (yBorde - cy) / m);
            }
        }

        return new Point(xBorde, yBorde);
    }

	public void setEsRecursiva() {
		this.esRecursiva = true;
	}

	public Boolean esRecursiva() {
		return esRecursiva;
	}
}
