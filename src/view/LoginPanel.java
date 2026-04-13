package view;

import controller.AuthController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPanel extends BasePanel {

    private JTextField    txtUser;
    private JPasswordField txtPass;
    private MainFrame frame;
    private AuthController auth = new AuthController();

    public LoginPanel(MainFrame frame) {
        this.frame = frame;
        initComponents();
    }

    @Override
    protected void initComponents() {
        setLayout(new BorderLayout());
        setBackground(C_BG);

        // ── Panel izquierdo decorativo ──
        JPanel left = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, C_PRIMARY, 0, getHeight(), C_PINK);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Círculos decorativos
                g2.setColor(new Color(255, 255, 255, 30));
                g2.fillOval(-60, -60, 250, 250);
                g2.fillOval(getWidth() - 120, getHeight() - 120, 240, 240);
                g2.setColor(new Color(255, 255, 255, 15));
                g2.fillOval(50, getHeight() / 2 - 80, 160, 160);
                g2.dispose();
            }
        };
        left.setPreferredSize(new Dimension(380, 0));
        left.setLayout(new GridBagLayout());

        JPanel leftContent = new JPanel();
        leftContent.setOpaque(false);
        leftContent.setLayout(new BoxLayout(leftContent, BoxLayout.Y_AXIS));
        leftContent.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));

        JLabel icon = new JLabel("🏪");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 52));
        icon.setAlignmentX(CENTER_ALIGNMENT);

        JLabel appName = new JLabel("AlmacénPro");
        appName.setFont(new Font("Segoe UI", Font.BOLD, 30));
        appName.setForeground(Color.WHITE);
        appName.setAlignmentX(CENTER_ALIGNMENT);

        JLabel tagline = new JLabel("<html><center>Gestión inteligente<br>de tu almacén</center></html>");
        tagline.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tagline.setForeground(new Color(255, 255, 255, 200));
        tagline.setHorizontalAlignment(SwingConstants.CENTER);
        tagline.setAlignmentX(CENTER_ALIGNMENT);

        leftContent.add(icon);
        leftContent.add(Box.createVerticalStrut(16));
        leftContent.add(appName);
        leftContent.add(Box.createVerticalStrut(10));
        leftContent.add(tagline);
        left.add(leftContent);

        // ── Panel derecho (formulario) ──
        JPanel right = new JPanel(new GridBagLayout());
        right.setBackground(C_CREAM);

        JPanel form = new JPanel();
        form.setBackground(C_SURFACE);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDER, 1, true),
            BorderFactory.createEmptyBorder(44, 44, 44, 44)
        ));
        form.setPreferredSize(new Dimension(380, 420));

        JLabel title = new JLabel("Iniciar Sesión");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(C_TEXT);
        title.setAlignmentX(CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Ingresa tus credenciales");
        sub.setFont(F_SMALL);
        sub.setForeground(C_TEXT_MUTED);
        sub.setAlignmentX(CENTER_ALIGNMENT);

        txtUser = createField("");
        txtPass = createPassField();

        JButton btnLogin = createPrimaryBtn("Iniciar Sesión");
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btnLogin.setAlignmentX(LEFT_ALIGNMENT);

        // Link registro
        JPanel regRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        regRow.setBackground(C_SURFACE);
        regRow.setAlignmentX(LEFT_ALIGNMENT);
        JLabel noAcc = new JLabel("¿No tienes cuenta?");
        noAcc.setFont(F_SMALL);
        noAcc.setForeground(C_TEXT_MUTED);
        JButton btnReg = new JButton("Regístrate");
        btnReg.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnReg.setForeground(C_PINK);
        btnReg.setBackground(null);
        btnReg.setBorder(null);
        btnReg.setContentAreaFilled(false);
        btnReg.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        regRow.add(noAcc); regRow.add(btnReg);

        form.add(title);
        form.add(Box.createVerticalStrut(4));
        form.add(sub);
        form.add(Box.createVerticalStrut(30));
        addField(form, "Usuario", txtUser);
        addField(form, "Contraseña", txtPass);
        form.add(Box.createVerticalStrut(6));
        form.add(btnLogin);
        form.add(Box.createVerticalStrut(14));
        form.add(regRow);

        right.add(form);

        add(left,  BorderLayout.WEST);
        add(right, BorderLayout.CENTER);

        btnLogin.addActionListener(e -> doLogin());
        btnReg.addActionListener(e -> frame.show(MainFrame.REGISTER));
        txtPass.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) { if (e.getKeyCode() == KeyEvent.VK_ENTER) doLogin(); }
        });
    }

    private void doLogin() {
        String u = txtUser.getText().trim();
        String p = new String(txtPass.getPassword()).trim();
        if (u.isEmpty() || p.isEmpty()) {
            showError("Debe ingresar su usuario y contraseña.\nSi no está registrado debe registrarse.");
            return;
        }
        if (auth.login(u, p)) {
            txtUser.setText(""); txtPass.setText("");
            frame.show(MainFrame.HOME);
        } else {
            showError("Usuario o contraseña incorrectos.");
        }
    }
}
