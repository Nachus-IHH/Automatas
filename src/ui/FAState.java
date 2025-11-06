package ui;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * Representa un estado visual del FA (DFA, NFA o NFAE)
 * Esta clase contiene las coordenadas y propiedades visuales del estado.
 */
public class FAState {
    public String name;
    public int x;
    public int y;
    public static final int RADIUS = 25;

    /**
     * Constructor del FAState
     * @param name nombre logico del estado
     * @param x Coordenada x de su centro
     * @param y Coordenada y de su centro
     */
    public FAState(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    /**
     * Obtiene los límites de la zona de selección del estado en forma de rectángulo.
     * Esto facilita la detección de clics del mouse (hit detection).
     * @return {@link java.awt.Rectangle} que abarca la circunferencia del estado.
     */
    public Rectangle getBounds() {
        return new Rectangle(x - RADIUS, y - RADIUS, 2 * RADIUS, 2 * RADIUS);
    }

    /**
     * Obtiene el par de coordenas centrales del estado
     * @return {@link java.awt.Point} que representa las coordenadas (x, y) centrales del estado en la GUI.
     */
    public Point getCenter() {
        return new Point(x, y);
    }
}