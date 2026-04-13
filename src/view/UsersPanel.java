package view;

import controller.UserController;
import model.User;
import patterns.AppFactory;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class UsersPanel extends BasePanel {

    private JTable table;
    private DefaultTableModel model;
    private MainFrame frame;
    private UserController ctrl = new UserController();

    public UsersPanel(MainFrame frame) {
        this.frame = frame;
        initComponents();
    }

    @Override
    protected void initComponents() {
        setLayout(new BorderLayout());
        setBackground(C_BG);

        // ── Top bar ──
        JPanel topBar = buildTopBar();

        // ── Tabla ──
        String[] cols = {"ID", "Usuario", "Nombre", "Apellido", "Teléfono", "Correo", ""};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return c == 6; }
        };
        table = buildTable(model);

        // Ocultar columna ID
        hideCol(0);

        // Renderer y editor botones
        table.getColumnModel().getColumn(6).setCellRenderer(new BtnRenderer());
        table.getColumnModel().getColumn(6).setCellEditor(new BtnEditor());
        table.getColumnModel().getColumn(6).setPreferredWidth(170);
        table.getColumnModel().getColumn(6).setMinWidth(170);

        JScrollPane scroll = styledScroll(table);
        JPanel tableWrap = new JPanel(new BorderLayout());
        tableWrap.setBackground(C_BG);
        tableWrap.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        tableWrap.add(scroll);

        add(topBar,    BorderLayout.NORTH);
        add(tableWrap, BorderLayout.CENTER);
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                GradientPaint gp = new GradientPaint(0, 0, C_PRIMARY, getWidth(), 0, C_PINK);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        bar.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));

        JLabel title = new JLabel("👤  Gestión de Usuarios");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);

        JButton btnBack = ghostBtn("← Volver");
        btnBack.addActionListener(e -> frame.show(MainFrame.HOME));

        bar.add(title,   BorderLayout.WEST);
        bar.add(btnBack, BorderLayout.EAST);
        return bar;
    }

    public void refresh() {
        model.setRowCount(0);
        for (User u : ctrl.getAllUsers())
            model.addRow(new Object[]{u.getId(), u.getUsername(), u.getName(),
                    u.getLastname(), u.getPhone(), u.getEmail(), ""});
    }

    private void hideCol(int col) {
        table.getColumnModel().getColumn(col).setMinWidth(0);
        table.getColumnModel().getColumn(col).setMaxWidth(0);
        table.getColumnModel().getColumn(col).setWidth(0);
    }

    // ── Editar ──
    void doEdit(int row) {
        int id    = (int) model.getValueAt(row, 0);
        JTextField fUser  = createField(""); fUser.setText((String) model.getValueAt(row, 1));
        JTextField fName  = createField(""); fName.setText((String) model.getValueAt(row, 2));
        JTextField fLast  = createField(""); fLast.setText((String) model.getValueAt(row, 3));
        JTextField fPhone = createField(""); fPhone.setText((String) model.getValueAt(row, 4));
        JTextField fEmail = createField(""); fEmail.setText((String) model.getValueAt(row, 5));
        JPasswordField fPass = createPassField();

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.setBackground(C_SURFACE);
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        addRow(form, "Usuario:",  fUser);
        addRow(form, "Nombre:",   fName);
        addRow(form, "Apellido:", fLast);
        addRow(form, "Teléfono:", fPhone);
        addRow(form, "Correo:",   fEmail);
        addRow(form, "Nueva contraseña (opcional):", fPass);

        int res = JOptionPane.showConfirmDialog(this, form,
                "Editar Usuario", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return;

        String u = fUser.getText().trim(), n = fName.getText().trim(),
               l = fLast.getText().trim(), ph = fPhone.getText().trim(),
               em = fEmail.getText().trim(), pw = new String(fPass.getPassword()).trim();

        if (u.isEmpty()||n.isEmpty()||l.isEmpty()||ph.isEmpty()||em.isEmpty()) {
            showError("Todos los campos son obligatorios."); return;
        }
        String curPass = ctrl.getAllUsers().stream()
                .filter(x -> x.getId() == id).map(User::getPassword)
                .findFirst().orElse("");
        String finalPass = pw.isEmpty() ? curPass : pw;

        if (ctrl.updateUser(AppFactory.createUserWithId(id, u, n, l, ph, em, finalPass))) {
            showSuccess("Usuario actualizado.");
            refresh();
        } else showError("No se pudo actualizar.");
    }

    // ── Eliminar ──
    void doDelete(int row) {
        int id    = (int) model.getValueAt(row, 0);
        String un = (String) model.getValueAt(row, 1);
        if (confirm("¿Eliminar al usuario '" + un + "'?")) {
            if (ctrl.deleteUser(id)) { showSuccess("Usuario eliminado."); refresh(); }
            else showError("No se pudo eliminar.");
        }
    }

    // ── Helpers ──
    private JTable buildTable(DefaultTableModel m) {
        JTable t = new JTable(m);
        t.setBackground(C_SURFACE);
        t.setForeground(C_TEXT);
        t.setFont(F_BODY);
        t.setRowHeight(48);
        t.setGridColor(C_BORDER);
        t.setShowVerticalLines(false);
        t.setIntercellSpacing(new Dimension(0, 0));
        t.setSelectionBackground(new Color(139, 92, 246, 50));
        t.setSelectionForeground(C_TEXT);
        JTableHeader h = t.getTableHeader();
        h.setBackground(C_SURFACE2);
        h.setForeground(C_TEXT_MUTED);
        h.setFont(F_LABEL);
        h.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, C_BORDER));
        h.setReorderingAllowed(false);
        return t;
    }

    private JScrollPane styledScroll(JTable t) {
        JScrollPane s = new JScrollPane(t);
        s.setBorder(BorderFactory.createLineBorder(C_BORDER, 1, true));
        s.getViewport().setBackground(C_SURFACE);
        return s;
    }

    private JButton ghostBtn(String text) {
        JButton b = new JButton(text);
        b.setFont(F_SMALL); b.setForeground(new Color(255,255,255,200));
        b.setBackground(null); b.setBorder(null);
        b.setContentAreaFilled(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void addRow(JPanel p, String lbl, JComponent field) {
        JLabel l = new JLabel(lbl); l.setFont(F_LABEL); l.setForeground(C_TEXT_MUTED);
        p.add(l); p.add(field);
    }

    // ── Renderer ──
    class BtnRenderer extends JPanel implements TableCellRenderer {
        JButton e = makeActionBtn("✏ Editar", C_PRIMARY);
        JButton d = makeActionBtn("🗑 Eliminar", C_DANGER);
        BtnRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 6, 8));
            setOpaque(true); add(e); add(d);
        }
        public Component getTableCellRendererComponent(JTable t, Object v,
                boolean s, boolean f, int r, int c) {
            setBackground(s ? new Color(139,92,246,40) : C_SURFACE); return this;
        }
    }

    // ── Editor ──
    class BtnEditor extends DefaultCellEditor {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 8));
        JButton e = makeActionBtn("✏ Editar", C_PRIMARY);
        JButton d = makeActionBtn("🗑 Eliminar", C_DANGER);
        int curRow;
        BtnEditor() {
            super(new JCheckBox());
            panel.setBackground(C_SURFACE);
            panel.add(e); panel.add(d);
            e.addActionListener(ev -> { fireEditingStopped(); doEdit(curRow); });
            d.addActionListener(ev -> { fireEditingStopped(); doDelete(curRow); });
        }
        public Component getTableCellEditorComponent(JTable t, Object v,
                boolean s, int r, int c) { curRow = r; return panel; }
        public Object getCellEditorValue() { return ""; }
    }

    private JButton makeActionBtn(String text, Color color) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setBackground(color); b.setForeground(Color.WHITE);
        b.setOpaque(true); b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(5, 14, 5, 14));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
}
