package view;

import controller.AuthController;
import model.User;
import patterns.AppFactory;
import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends BasePanel {

    private JTextField    fUser, fName, fLast, fPhone, fEmail;
    private JPasswordField fPass, fConfirm;
    private MainFrame frame;
    private AuthController auth = new AuthController();

    public RegisterPanel(MainFrame frame) {
        this.frame = frame;
        initComponents();
    }

    @Override
    protected void initComponents() {
        setLayout(new BorderLayout());
        setBackground(C_BG);

        // Header
        JPanel header = createHeader("Crear Cuenta", "Completa todos los campos para registrarte");
        JButton btnBack = new JButton("← Volver");
        btnBack.setFont(F_SMALL);
        btnBack.setForeground(new Color(255, 255, 255, 200));
        btnBack.setBackground(null);
        btnBack.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        btnBack.setContentAreaFilled(false);
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel headerWrap = new JPanel(new BorderLayout());
        headerWrap.setOpaque(false);
        headerWrap.add(header, BorderLayout.CENTER);
        headerWrap.add(btnBack, BorderLayout.EAST);

        // Formulario
        JPanel formOuter = new JPanel(new GridBagLayout());
        formOuter.setBackground(C_BG);

        JPanel form = new JPanel();
        form.setBackground(C_SURFACE);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDER, 1, true),
            BorderFactory.createEmptyBorder(32, 40, 32, 40)
        ));

        // Dos columnas
        JPanel cols = new JPanel(new GridLayout(1, 2, 24, 0));
        cols.setBackground(C_SURFACE);
        cols.setAlignmentX(LEFT_ALIGNMENT);

        JPanel col1 = makeCol();
        JPanel col2 = makeCol();

        fUser   = createField(""); fUser.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        fName   = createField(""); fName.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        fLast   = createField(""); fLast.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        fPhone  = createField(""); fPhone.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        fEmail  = createField(""); fEmail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        fPass   = createPassField(); fPass.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        fConfirm= createPassField(); fConfirm.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        addField(col1, "Nombre de usuario *", fUser);
        addField(col1, "Nombre *", fName);
        addField(col1, "Apellido *", fLast);
        addField(col1, "Teléfono *", fPhone);

        addField(col2, "Correo electrónico *", fEmail);
        addField(col2, "Contraseña *", fPass);
        addField(col2, "Confirmar contraseña *", fConfirm);

        cols.add(col1); cols.add(col2);

        JButton btnReg = createPrimaryBtn("✔  Crear Cuenta");
        btnReg.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btnReg.setAlignmentX(LEFT_ALIGNMENT);

        form.add(cols);
        form.add(Box.createVerticalStrut(20));
        form.add(btnReg);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(30, 40, 30, 40);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1; gbc.weighty = 1;
        formOuter.add(form, gbc);

        add(headerWrap, BorderLayout.NORTH);
        add(formOuter,  BorderLayout.CENTER);

        btnReg.addActionListener(e -> doRegister());
        btnBack.addActionListener(e -> frame.show(MainFrame.LOGIN));
    }

    private JPanel makeCol() {
        JPanel p = new JPanel();
        p.setBackground(C_SURFACE);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        return p;
    }

    private void doRegister() {
        String u  = fUser.getText().trim();
        String n  = fName.getText().trim();
        String l  = fLast.getText().trim();
        String ph = fPhone.getText().trim();
        String em = fEmail.getText().trim();
        String pw = new String(fPass.getPassword()).trim();
        String cf = new String(fConfirm.getPassword()).trim();

        if (u.isEmpty())  { showError("El campo 'Nombre de usuario' es obligatorio."); return; }
        if (n.isEmpty())  { showError("El campo 'Nombre' es obligatorio."); return; }
        if (l.isEmpty())  { showError("El campo 'Apellido' es obligatorio."); return; }
        if (ph.isEmpty()) { showError("El campo 'Teléfono' es obligatorio."); return; }
        if (em.isEmpty()) { showError("El campo 'Correo electrónico' es obligatorio."); return; }
        if (pw.isEmpty()) { showError("El campo 'Contraseña' es obligatorio."); return; }
        if (cf.isEmpty()) { showError("El campo 'Confirmar contraseña' es obligatorio."); return; }
        if (!pw.equals(cf)) { showError("La contraseña y la confirmación no coinciden."); return; }

        User user = AppFactory.createUser(u, n, l, ph, em, pw);
        if (auth.register(user)) {
            showSuccess("¡Cuenta creada exitosamente! Ya puedes iniciar sesión.");
            clearAll();
            frame.show(MainFrame.LOGIN);
        } else {
            showError("El nombre de usuario '" + u + "' ya está en uso.");
        }
    }

    private void clearAll() {
        fUser.setText(""); fName.setText(""); fLast.setText("");
        fPhone.setText(""); fEmail.setText("");
        fPass.setText(""); fConfirm.setText("");
    }
}
