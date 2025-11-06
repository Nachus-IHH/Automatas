package ui;

import core.FA;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/**
 * Componente de Swing (JPanel) encargado de dibujar la representación gráfica
 * del Autómata Finito (FA), incluyendo estados y transiciones.
 * Esta clase también maneja la interacción del usuario como el arrastre de estados.
 */
public class FAGraphPanel extends JPanel {

    private final FA faLogic;
    private final List<FAState> visualStates;
    private FAState selectedState = null;
    private Point dragOffset;

    /**
     * Constructor del panel gráfico.
     * @param faLogic La instancia del Autómata Finito (FA, DFA, NFA o NFAE) cuyos estados y transiciones se dibujarán.
     */
    public FAGraphPanel(FA faLogic) {
        this.faLogic = faLogic;
        this.visualStates = new ArrayList<>();
        initializeVisualStates();
        enableDragging();
    }

    // --- Inicialización de estados visuales ---
    private void initializeVisualStates() {
        int centerX = 400, centerY = 300, radius = 220;
        int total = faLogic.states.size(), i = 0;

        for (String stateName : faLogic.states) {
            double angle = 2 * Math.PI * i / total;
            int x = (int) (centerX + radius * Math.cos(angle));
            int y = (int) (centerY + radius * Math.sin(angle));
            visualStates.add(new FAState(stateName, x, y));
            i++;
        }
    }

    // --- Habilitar arrastre con el mouse ---
    private void enableDragging() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                for (FAState s : visualStates) {
                    if (s.getBounds().contains(e.getPoint())) {
                        selectedState = s;
                        dragOffset = new Point(e.getX() - s.x, e.getY() - s.y);
                        break;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                selectedState = null;
                dragOffset = null;
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectedState != null) {
                    selectedState.x = e.getX() - dragOffset.x;
                    selectedState.y = e.getY() - dragOffset.y;
                    repaint();
                }
            }
        });
    }

    private FAState getVisualState(String name) {
        for (FAState s : visualStates)
            if (s.name.equals(name))
                return s;
        return null;
    }

    // --- Dibujo principal ---
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawTransitions(g2d);
        drawStates(g2d);
    }

    /**
     * Sobrescribe el método de dibujo para renderizar los estados y las transiciones del FA.
     * Es llamado automáticamente por Swing cuando el componente necesita ser redibujado.
     * @param g2g El contexto gráfico utilizado para dibujar.
     */
    private void drawTransitions(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.BLACK);

        for (String from : faLogic.transitionTable.keySet()) {
            FAState fromState = getVisualState(from);
            if (fromState == null) continue;

            for (Map.Entry<Character, HashSet<String>> entry : faLogic.transitionTable.get(from).entrySet()) {
                char symbol = entry.getKey();
                String label = (symbol == 'ε') ? "ε" : String.valueOf(symbol);

                for (String to : entry.getValue()) {
                    FAState toState = getVisualState(to);
                    if (toState == null) continue;

                    if (from.equals(to))
                        drawLoop(g2d, fromState, label);
                    else
                        drawArrow(g2d, fromState, toState, label);
                }
            }
        }
    }

    private void drawArrow(Graphics2D g2d, FAState from, FAState to, String label) {
        Point a = from.getCenter(), b = to.getCenter();
        double angle = Math.atan2(b.y - a.y, b.x - a.x);

        int sx = (int) (a.x + FAState.RADIUS * Math.cos(angle));
        int sy = (int) (a.y + FAState.RADIUS * Math.sin(angle));
        int ex = (int) (b.x - FAState.RADIUS * Math.cos(angle));
        int ey = (int) (b.y - FAState.RADIUS * Math.sin(angle));

        g2d.drawLine(sx, sy, ex, ey);
        drawArrowHead(g2d, ex, ey, angle);

        int mx = (sx + ex) / 2, my = (sy + ey) / 2;
        g2d.setColor(Color.BLUE);
        g2d.drawString(label, mx, my - 5);
        g2d.setColor(Color.BLACK);
    }

    private void drawLoop(Graphics2D g2d, FAState s, String label) {
        int loopR = FAState.RADIUS + 15;
        g2d.drawOval(s.x - loopR, s.y - loopR - 25, loopR * 2, loopR * 2);
        g2d.setColor(Color.BLUE);
        g2d.drawString(label, s.x, s.y - loopR - 35);
        g2d.setColor(Color.BLACK);
    }

    private void drawArrowHead(Graphics2D g2d, int x, int y, double angle) {
        int size = 10;
        int x1 = (int) (x - size * Math.cos(angle - Math.PI / 6));
        int y1 = (int) (y - size * Math.sin(angle - Math.PI / 6));
        int x2 = (int) (x - size * Math.cos(angle + Math.PI / 6));
        int y2 = (int) (y - size * Math.sin(angle + Math.PI / 6));
        g2d.fillPolygon(new int[]{x, x1, x2}, new int[]{y, y1, y2}, 3);
    }

    // --- Dibujar estados ---
    private void drawStates(Graphics2D g2d) {
        for (FAState s : visualStates) {
            int r = FAState.RADIUS;

            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fillOval(s.x - r, s.y - r, 2 * r, 2 * r);

            g2d.setColor(Color.BLACK);
            g2d.drawOval(s.x - r, s.y - r, 2 * r, 2 * r);

            if (faLogic.finalStates.contains(s.name)) {
                g2d.drawOval(s.x - r + 4, s.y - r + 4, 2 * r - 8, 2 * r - 8);
            }

            if (s.name.equals(faLogic.initialState)) {
                g2d.drawLine(s.x - r - 20, s.y, s.x - r, s.y);
                drawArrowHead(g2d, s.x - r, s.y, Math.PI);
            }

            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            FontMetrics fm = g2d.getFontMetrics();
            int sw = fm.stringWidth(s.name);
            g2d.drawString(s.name, s.x - sw / 2, s.y + fm.getAscent() / 2);
        }
    }
}
