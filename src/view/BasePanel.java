package view;

import javax.swing.*;
import java.awt.*;

/**
 * =====================================================
 * PILAR OOP: ABSTRACCIÓN + HERENCIA
 * =====================================================
 * ABSTRACCIÓN:
 *   BasePanel es una clase ABSTRACTA. Define la estructura
 *   común que deben tener todos los paneles, pero NO puede
 *   ser instanciada directamente (no se puede hacer
 *   new BasePanel()). Obliga a las subclases a implementar
 *   el método abstracto initComponents().
 *
 * HERENCIA:
 *   Todos los paneles de la aplicación HEREDAN de BasePanel:
 *     - LoginPanel    extends BasePanel
 *     - RegisterPanel extends BasePanel
 *     - HomePanel     extends BasePanel
 *     - UsersPanel    extends BasePanel
 *     - ProductsPanel extends BasePanel
 *
 *   Cada uno hereda automáticamente:
 *     - La paleta de colores (constantes)
 *     - Los métodos de creación de componentes visuales
 *     - Los métodos showError(), showSuccess(), confirm()
 *
 * PILAR OOP: POLIMORFISMO
 *   El método initComponents() es abstracto, lo que significa
 *   que cada subclase lo implementa de manera diferente
 *   según su propia funcionalidad. Esto es polimorfismo:
 *   el mismo método, comportamientos distintos.
 * =====================================================
 */
public abstract class BasePanel extends JPanel {

    // ── Paleta de colores del sistema (morado, rosa, crema, blanco) ──
    protected static final Color C_BG         = new Color(250, 245, 255); // fondo crema violáceo
    protected static final Color C_SURFACE    = new Color(255, 255, 255); // blanco (tarjetas)
    protected static final Color C_SURFACE2   = new Color(245, 240, 255); // crema suave
    protected static final Color C_PRIMARY    = new Color(139,  92, 246); // morado principal
    protected static final Color C_PRIMARY_D  = new Color(109,  40, 217); // morado oscuro (hover)
    protected static final Color C_PINK       = new Color(236,  72, 153); // rosa
    protected static final Color C_PINK_L     = new Color(251, 207, 232); // rosa claro
    protected static final Color C_BORDER     = new Color(221, 214, 254); // borde lila
    protected static final Color C_TEXT       = new Color( 30,  27,  75); // texto oscuro
    protected static final Color C_TEXT_MUTED = new Color(107, 114, 128); // texto gris
    protected static final Color C_DANGER     = new Color(239,  68,  68); // rojo (eliminar)
    protected static final Color C_SUCCESS    = new Color( 34, 197,  94); // verde
    protected static final Color C_CREAM      = new Color(255, 251, 245); // crema puro

    // ── Fuentes ──
    protected static final Font F_TITLE   = new Font("Segoe UI", Font.BOLD,  24);
    protected static final Font F_HEADING = new Font("Segoe UI", Font.BOLD,  16);
    protected static final Font F_BODY    = new Font("Segoe UI", Font.PLAIN, 14);
    protected static final Font F_SMALL   = new Font("Segoe UI", Font.PLAIN, 12);
    protected static final Font F_LABEL   = new Font("Segoe UI", Font.BOLD,  12);

    public BasePanel() {
        setBackground(C_BG);
    }

    /**
     * ABSTRACCIÓN + POLIMORFISMO:
     * Método abstracto que TODAS las subclases deben implementar.
     * Cada panel construye su propia interfaz dentro de este método.
     */
    protected abstract void initComponents();

    // ── Métodos heredados por todos los paneles hijos ──

    /** Crea un campo de texto con el estilo del sistema. */
    protected JTextField createField(String hint) {
        JTextField f = new JTextField();
        styleField(f);
        return f;
    }

    /** Crea un campo de contraseña (texto oculto) con el estilo del sistema. */
    protected JPasswordField createPassField() {
        JPasswordField f = new JPasswordField();
        styleField(f);
        return f;
    }

    // Aplica el estilo visual común a campos de texto
    private void styleField(JComponent f) {
        f.setFont(F_BODY);
        f.setForeground(C_TEXT);
        if (f instanceof JTextField)    ((JTextField)f).setCaretColor(C_PRIMARY);
        if (f instanceof JPasswordField)((JPasswordField)f).setCaretColor(C_PRIMARY);
        f.setBackground(C_SURFACE);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDER, 1, true),
            BorderFactory.createEmptyBorder(9, 14, 9, 14)
        ));
    }

    /** Crea una etiqueta de campo con estilo muted. */
    protected JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(F_LABEL);
        l.setForeground(C_TEXT_MUTED);
        return l;
    }

    /** Crea un título principal. */
    protected JLabel createTitle(String text) {
        JLabel l = new JLabel(text);
        l.setFont(F_TITLE);
        l.setForeground(C_TEXT);
        return l;
    }

    /** Botón primario morado — acción principal. */
    protected JButton createPrimaryBtn(String text) {
        return makeBtn(text, C_PRIMARY, C_PRIMARY_D, Color.WHITE);
    }

    /** Botón rosa — acción secundaria o destacada. */
    protected JButton createPinkBtn(String text) {
        return makeBtn(text, C_PINK, new Color(219, 39, 119), Color.WHITE);
    }

    /** Botón rojo — acción destructiva (eliminar). */
    protected JButton createDangerBtn(String text) {
        return makeBtn(text, C_DANGER, new Color(220, 38, 38), Color.WHITE);
    }

    /** Botón secundario con borde morado y fondo blanco. */
    protected JButton createSecondaryBtn(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? C_SURFACE2 : C_SURFACE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(C_PRIMARY);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 10, 10);
                g2.setColor(C_PRIMARY);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false); btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // Fábrica interna de botones personalizados con paintComponent
    private JButton makeBtn(String text, Color normal, Color hover, Color fg) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() || getModel().isPressed() ? hover : normal);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(fg);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(fg);
        btn.setFocusPainted(false); btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /** Muestra un diálogo de error. */
    protected void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /** Muestra un diálogo de éxito. */
    protected void showSuccess(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    /** Muestra un diálogo de confirmación. Retorna true si el usuario acepta. */
    protected boolean confirm(String msg) {
        return JOptionPane.showConfirmDialog(this, msg, "Confirmar",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }

    /** Agrega un campo con su etiqueta a un panel vertical (BoxLayout). */
    protected void addField(JPanel panel, String labelText, JComponent field) {
        JLabel lbl = createLabel(labelText);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(5));
        panel.add(field);
        panel.add(Box.createVerticalStrut(14));
    }

    /** Crea un header decorativo con gradiente morado → rosa. */
    protected JPanel createHeader(String title, String subtitle) {
        JPanel header = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Gradiente horizontal: morado → rosa
                GradientPaint gp = new GradientPaint(0, 0, C_PRIMARY, getWidth(), 0, C_PINK);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 22));
        t.setForeground(Color.WHITE);
        t.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel s = new JLabel(subtitle);
        s.setFont(F_SMALL);
        s.setForeground(new Color(255, 255, 255, 180));
        s.setAlignmentX(Component.LEFT_ALIGNMENT);

        header.add(t);
        if (!subtitle.isEmpty()) {
            header.add(Box.createVerticalStrut(4));
            header.add(s);
        }
        return header;
    }
}
