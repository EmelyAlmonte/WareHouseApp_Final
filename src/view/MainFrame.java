package view;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal — CardLayout para navegación entre pantallas
 */
public class MainFrame extends JFrame {

    private CardLayout card = new CardLayout();
    private JPanel     root = new JPanel(card);

    public static final String LOGIN     = "LOGIN";
    public static final String REGISTER  = "REGISTER";
    public static final String HOME      = "HOME";
    public static final String USERS     = "USERS";
    public static final String PRODUCTS  = "PRODUCTS";

    private LoginPanel    loginPanel;
    private RegisterPanel registerPanel;
    private HomePanel     homePanel;
    private UsersPanel    usersPanel;
    private ProductsPanel productsPanel;

    public MainFrame() {
        setTitle("Sistema de Gestión de Almacén");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(960, 660));
        setPreferredSize(new Dimension(1100, 720));

        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}

        root.setBackground(new Color(250, 245, 255));

        loginPanel    = new LoginPanel(this);
        registerPanel = new RegisterPanel(this);
        homePanel     = new HomePanel(this);
        usersPanel    = new UsersPanel(this);
        productsPanel = new ProductsPanel(this);

        root.add(loginPanel,    LOGIN);
        root.add(registerPanel, REGISTER);
        root.add(homePanel,     HOME);
        root.add(usersPanel,    USERS);
        root.add(productsPanel, PRODUCTS);

        add(root);
        pack();
        setLocationRelativeTo(null);
        show(LOGIN);
        setVisible(true);
    }

    public void show(String screen) {
        if (screen.equals(USERS))    usersPanel.refresh();
        if (screen.equals(PRODUCTS)) productsPanel.refresh();
        card.show(root, screen);
    }
}
