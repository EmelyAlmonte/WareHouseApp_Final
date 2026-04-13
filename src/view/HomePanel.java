package view;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends BasePanel {

    private MainFrame frame;

    public HomePanel(MainFrame frame) {
        this.frame = frame;
        initComponents();
    }

    @Override
    protected void initComponents() {
        setLayout(new BorderLayout());
        setBackground(C_BG);

        // ── Header ──
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JPanel headerGrad = createHeader("AlmacénPro", "Selecciona un módulo para comenzar");

        JButton btnLogout = createDangerBtn("  Cerrar Sesión  ");
        btnLogout.setPreferredSize(new Dimension(150, 38));
        JPanel logoutWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutWrap.setOpaque(false);

        // Meter logout dentro del header
        JPanel topBar = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                GradientPaint gp = new GradientPaint(0, 0, C_PRIMARY, getWidth(), 0, C_PINK);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        topBar.setBorder(BorderFactory.createEmptyBorder(18, 30, 18, 24));

        JPanel textBlock = new JPanel();
        textBlock.setOpaque(false);
        textBlock.setLayout(new BoxLayout(textBlock, BoxLayout.Y_AXIS));

        JLabel appIcon = new JLabel("🏪  AlmacénPro");
        appIcon.setFont(new Font("Segoe UI", Font.BOLD, 22));
        appIcon.setForeground(Color.WHITE);

        JLabel appSub = new JLabel("Selecciona un módulo para continuar");
        appSub.setFont(F_SMALL);
        appSub.setForeground(new Color(255, 255, 255, 180));

        textBlock.add(appIcon);
        textBlock.add(Box.createVerticalStrut(4));
        textBlock.add(appSub);

        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 13));

        topBar.add(textBlock, BorderLayout.WEST);
        topBar.add(btnLogout, BorderLayout.EAST);

        // ── Centro con los dos botones ──
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(C_BG);

        JPanel cards = new JPanel(new GridLayout(1, 2, 30, 0));
        cards.setBackground(C_BG);
        cards.setPreferredSize(new Dimension(700, 300));

        cards.add(makeModuleCard("👤", "Gestión de Usuarios",
                "Administra los usuarios del sistema:\nregistros, edición y eliminación.",
                C_PRIMARY, e -> frame.show(MainFrame.USERS)));

        cards.add(makeModuleCard("📦", "Gestión de Productos",
                "Administra el inventario del almacén:\nagregar, editar y eliminar productos.",
                C_PINK, e -> frame.show(MainFrame.PRODUCTS)));

        center.add(cards);

        add(topBar, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);

        btnLogout.addActionListener(e -> frame.show(MainFrame.LOGIN));
    }

    private JPanel makeModuleCard(String icon, String title, String desc,
                                   Color accent, java.awt.event.ActionListener action) {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(C_SURFACE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                // Franja superior de color
                g2.setColor(accent);
                g2.fillRoundRect(0, 0, getWidth(), 8, 4, 4);
                g2.dispose();
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDER, 1, true),
            BorderFactory.createEmptyBorder(36, 36, 36, 36)
        ));
        card.setOpaque(false);

        JLabel ico = new JLabel(icon);
        ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 44));
        ico.setAlignmentX(CENTER_ALIGNMENT);

        JLabel ttl = new JLabel(title);
        ttl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        ttl.setForeground(C_TEXT);
        ttl.setAlignmentX(CENTER_ALIGNMENT);

        JLabel dsc = new JLabel("<html><center>" + desc.replace("\n", "<br>") + "</center></html>");
        dsc.setFont(F_SMALL);
        dsc.setForeground(C_TEXT_MUTED);
        dsc.setHorizontalAlignment(SwingConstants.CENTER);
        dsc.setAlignmentX(CENTER_ALIGNMENT);

        JButton btn = makeBtn("  Abrir módulo →", accent);
        btn.setAlignmentX(CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(200, 42));
        btn.addActionListener(action);

        card.add(Box.createVerticalGlue());
        card.add(ico);
        card.add(Box.createVerticalStrut(16));
        card.add(ttl);
        card.add(Box.createVerticalStrut(10));
        card.add(dsc);
        card.add(Box.createVerticalStrut(24));
        card.add(btn);
        card.add(Box.createVerticalGlue());

        return card;
    }

    private JButton makeBtn(String text, Color color) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? color.darker() : color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
