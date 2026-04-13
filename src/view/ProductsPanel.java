package view;

import controller.ProductController;
import model.Product;
import patterns.AppFactory;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class ProductsPanel extends BasePanel {

    private JTable table;
    private DefaultTableModel model;
    private MainFrame frame;
    private ProductController ctrl = new ProductController();

    public ProductsPanel(MainFrame frame) {
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
        String[] cols = {"ID", "Nombre", "Marca", "Categoría", "Precio (RD$)", "Cantidad"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = buildTable(model);

        // Ocultar ID
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);

        // Clic en fila → editar
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0)
                openEditDialog(table.getSelectedRow());
        });

        JScrollPane scroll = styledScroll(table);
        JPanel tableWrap = new JPanel(new BorderLayout());
        tableWrap.setBackground(C_BG);
        tableWrap.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        // Hint
        JLabel hint = new JLabel("💡  Haz clic en un producto para editarlo o eliminarlo");
        hint.setFont(F_SMALL);
        hint.setForeground(C_TEXT_MUTED);
        hint.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        tableWrap.add(hint,   BorderLayout.NORTH);
        tableWrap.add(scroll, BorderLayout.CENTER);

        add(topBar,    BorderLayout.NORTH);
        add(tableWrap, BorderLayout.CENTER);
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                GradientPaint gp = new GradientPaint(0, 0, C_PINK, getWidth(), 0, C_PRIMARY);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        bar.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));

        JLabel title = new JLabel("📦  Gestión de Productos");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);

        JButton btnNew  = makeTopBtn("＋  Nuevo Producto", new Color(255,255,255,50), Color.WHITE);
        JButton btnBack = ghostBtn("← Volver");

        right.add(btnNew);
        right.add(btnBack);

        bar.add(title, BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);

        btnNew.addActionListener(e  -> openNewDialog());
        btnBack.addActionListener(e -> frame.show(MainFrame.HOME));

        return bar;
    }

    public void refresh() {
        model.setRowCount(0);
        for (Product p : ctrl.getAllProducts())
            model.addRow(new Object[]{
                p.getId(), p.getName(), p.getBrand(), p.getCategory(),
                String.format("%.2f", p.getPrice()), p.getQuantity()
            });
    }

    // ── Nuevo producto ──
    private void openNewDialog() {
        JTextField fName  = createField(""); fName.setPreferredSize(new Dimension(220, 38));
        JTextField fBrand = createField(""); fBrand.setPreferredSize(new Dimension(220, 38));
        JTextField fCat   = createField(""); fCat.setPreferredSize(new Dimension(220, 38));
        JTextField fPrice = createField(""); fPrice.setPreferredSize(new Dimension(220, 38));
        JTextField fQty   = createField(""); fQty.setPreferredSize(new Dimension(220, 38));

        JPanel form = buildProductForm(fName, fBrand, fCat, fPrice, fQty);

        int res = JOptionPane.showConfirmDialog(this, form,
                "Nuevo Producto", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return;

        Product p = parseProduct(-1, fName, fBrand, fCat, fPrice, fQty);
        if (p == null) return;

        ctrl.addProduct(p);
        showSuccess("Producto registrado correctamente.");
        refresh();
    }

    // ── Editar / Eliminar producto ──
    private void openEditDialog(int row) {
        int id = (int) model.getValueAt(row, 0);

        JTextField fName  = createField(""); fName.setText((String) model.getValueAt(row, 1));
        JTextField fBrand = createField(""); fBrand.setText((String) model.getValueAt(row, 2));
        JTextField fCat   = createField(""); fCat.setText((String) model.getValueAt(row, 3));
        JTextField fPrice = createField(""); fPrice.setText(((String) model.getValueAt(row, 4)).replace(",", "."));
        JTextField fQty   = createField(""); fQty.setText(String.valueOf(model.getValueAt(row, 5)));

        JPanel form = buildProductForm(fName, fBrand, fCat, fPrice, fQty);

        // Botones personalizados
        JButton btnSave = makeDialogBtn("💾  Guardar", C_PRIMARY);
        JButton btnDel  = makeDialogBtn("🗑  Eliminar", C_DANGER);
        JButton btnCnl  = makeDialogBtn("Cancelar",    C_TEXT_MUTED);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btns.setBackground(C_SURFACE);
        btns.add(btnSave); btns.add(btnDel); btns.add(btnCnl);

        JPanel all = new JPanel(new BorderLayout());
        all.setBackground(C_SURFACE);
        all.add(form, BorderLayout.CENTER);
        all.add(btns, BorderLayout.SOUTH);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Editar Producto", true);
        dialog.setContentPane(all);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        btnSave.addActionListener(e -> {
            Product p = parseProduct(id, fName, fBrand, fCat, fPrice, fQty);
            if (p == null) return;
            ctrl.updateProduct(p);
            showSuccess("Producto actualizado.");
            refresh();
            table.clearSelection();
            dialog.dispose();
        });

        btnDel.addActionListener(e -> {
            if (confirm("¿Eliminar el producto '" + fName.getText().trim() + "'?")) {
                ctrl.deleteProduct(id);
                showSuccess("Producto eliminado.");
                refresh();
                table.clearSelection();
                dialog.dispose();
            }
        });

        btnCnl.addActionListener(e -> { table.clearSelection(); dialog.dispose(); });

        dialog.setVisible(true);
    }

    // ── Helpers ──
    private JPanel buildProductForm(JTextField fName, JTextField fBrand,
                                     JTextField fCat,  JTextField fPrice, JTextField fQty) {
        JPanel form = new JPanel(new GridLayout(0, 2, 14, 10));
        form.setBackground(C_SURFACE);
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        addRow(form, "Nombre *",             fName);
        addRow(form, "Marca *",              fBrand);
        addRow(form, "Categoría *",          fCat);
        addRow(form, "Precio (RD$) *",       fPrice);
        addRow(form, "Cantidad disponible *", fQty);
        return form;
    }

    private Product parseProduct(int id, JTextField fName, JTextField fBrand,
                                  JTextField fCat, JTextField fPrice, JTextField fQty) {
        String name  = fName.getText().trim();
        String brand = fBrand.getText().trim();
        String cat   = fCat.getText().trim();
        String priceS= fPrice.getText().trim();
        String qtyS  = fQty.getText().trim();

        if (name.isEmpty())  { showError("El campo 'Nombre' es obligatorio."); return null; }
        if (brand.isEmpty()) { showError("El campo 'Marca' es obligatorio."); return null; }
        if (cat.isEmpty())   { showError("El campo 'Categoría' es obligatoria."); return null; }
        if (priceS.isEmpty()){ showError("El campo 'Precio' es obligatorio."); return null; }
        if (qtyS.isEmpty())  { showError("El campo 'Cantidad' es obligatoria."); return null; }

        double price; int qty;
        try { price = Double.parseDouble(priceS.replace(",",".")); }
        catch (NumberFormatException e) { showError("El precio debe ser un número válido."); return null; }
        try { qty = Integer.parseInt(qtyS); }
        catch (NumberFormatException e) { showError("La cantidad debe ser un número entero."); return null; }

        return id < 0
            ? AppFactory.createProduct(name, brand, cat, price, qty)
            : AppFactory.createProductWithId(id, name, brand, cat, price, qty);
    }

    private JTable buildTable(DefaultTableModel m) {
        JTable t = new JTable(m);
        t.setBackground(C_SURFACE);
        t.setForeground(C_TEXT);
        t.setFont(F_BODY);
        t.setRowHeight(48);
        t.setGridColor(C_BORDER);
        t.setShowVerticalLines(false);
        t.setIntercellSpacing(new Dimension(0, 0));
        t.setSelectionBackground(new Color(236,72,153,50));
        t.setSelectionForeground(C_TEXT);
        t.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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

    private JButton makeTopBtn(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setForeground(fg); b.setBackground(bg);
        b.setOpaque(true); b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JButton makeDialogBtn(String text, Color color) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setForeground(Color.WHITE); b.setBackground(color);
        b.setOpaque(true); b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(9, 20, 9, 20));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void addRow(JPanel p, String lbl, JComponent field) {
        JLabel l = new JLabel(lbl); l.setFont(F_LABEL); l.setForeground(C_TEXT_MUTED);
        p.add(l); p.add(field);
    }
}
