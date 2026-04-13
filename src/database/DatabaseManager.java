package database;

import model.Product;
import model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * =====================================================
 * PATRÓN DE DISEÑO: SINGLETON
 * =====================================================
 * Esta clase garantiza que solo exista UNA instancia
 * del gestor de base de datos durante toda la ejecución.
 *
 * Características del Singleton aplicadas aquí:
 *  1. Constructor PRIVADO — nadie puede hacer new DatabaseManager()
 *  2. Atributo estático 'instance' — guarda la única instancia
 *  3. Método estático getInstance() — único punto de acceso
 *
 * Beneficio: toda la app comparte la misma conexión a MySQL.
 * =====================================================
 */
public class DatabaseManager {

    // SINGLETON: la única instancia, compartida en toda la app
    private static DatabaseManager instance;

    // Credenciales de la base de datos remota (provistos por el profesor)
    private static final String URL     = "jdbc:mysql://almacenitla-db-itla-3837.e.aivencloud.com:25037/almacenitlafinal?sslMode=REQUIRED";
    private static final String DB_USER = "avnadmin";
    private static final String DB_PASS = "AVNS_pPa2xcIg1UbjOzcsoMg";

    private Connection connection;

    // SINGLETON: constructor privado — impide instanciación externa
    private DatabaseManager() {
        connect();
    }

    /**
     * SINGLETON: método público de acceso a la instancia única.
     * Si no existe la crea; si ya existe la retorna directamente.
     */
    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    // Establece la conexión con MySQL remoto
    private void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, DB_USER, DB_PASS);
            System.out.println("✅ Conexión a base de datos remota establecida.");
        } catch (Exception e) {
            System.err.println("❌ Error de conexión: " + e.getMessage());
            connection = null;
        }
    }

    // Retorna la conexión activa, reconectando si fue cerrada
    private Connection getConn() {
        try {
            if (connection == null || connection.isClosed()) connect();
        } catch (SQLException e) {
            connect();
        }
        return connection;
    }

    // ══════════════════════════════════════════════
    //  OPERACIONES DE USUARIOS
    // ══════════════════════════════════════════════

    /** Registra un nuevo usuario. Retorna false si el username ya existe. */
    public boolean registerUser(User u) {
        try {
            PreparedStatement check = getConn().prepareStatement(
                "SELECT id FROM users WHERE username = ?");
            check.setString(1, u.getUsername());
            if (check.executeQuery().next()) return false;

            PreparedStatement ps = getConn().prepareStatement(
                "INSERT INTO users (username, nombre, apellido, telefono, email, password) VALUES (?,?,?,?,?,?)");
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getName());
            ps.setString(3, u.getLastname());
            ps.setString(4, u.getPhone());
            ps.setString(5, u.getEmail());
            ps.setString(6, u.getPassword());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error registerUser: " + e.getMessage());
            return false;
        }
    }

    /** Autentica un usuario por username y password. */
    public User authenticate(String username, String password) {
        try {
            PreparedStatement ps = getConn().prepareStatement(
                "SELECT * FROM users WHERE username = ? AND password = ?");
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("telefono"),
                    rs.getString("email"),
                    rs.getString("password")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error authenticate: " + e.getMessage());
        }
        return null;
    }

    /** Obtiene todos los usuarios registrados en la BD. */
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        try {
            ResultSet rs = getConn().createStatement()
                .executeQuery("SELECT * FROM users");
            while (rs.next()) {
                list.add(new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("telefono"),
                    rs.getString("email"),
                    rs.getString("password")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getAllUsers: " + e.getMessage());
        }
        return list;
    }

    /** Actualiza los datos de un usuario existente. */
    public boolean updateUser(User u) {
        try {
            PreparedStatement ps = getConn().prepareStatement(
                "UPDATE users SET username=?, nombre=?, apellido=?, telefono=?, email=?, password=? WHERE id=?");
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getName());
            ps.setString(3, u.getLastname());
            ps.setString(4, u.getPhone());
            ps.setString(5, u.getEmail());
            ps.setString(6, u.getPassword());
            ps.setInt(7, u.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updateUser: " + e.getMessage());
            return false;
        }
    }

    /** Elimina un usuario por su ID. */
    public boolean deleteUser(int id) {
        try {
            PreparedStatement ps = getConn().prepareStatement(
                "DELETE FROM users WHERE id = ?");
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleteUser: " + e.getMessage());
            return false;
        }
    }

    // ══════════════════════════════════════════════
    //  OPERACIONES DE PRODUCTOS
    // ══════════════════════════════════════════════

    /** Agrega un nuevo producto al almacén. */
    public boolean addProduct(Product p) {
        try {
            PreparedStatement ps = getConn().prepareStatement(
                "INSERT INTO productos (nombre, marca, categoria, precio, cantidad_disponible) VALUES (?,?,?,?,?)");
            ps.setString(1, p.getName());
            ps.setString(2, p.getBrand());
            ps.setString(3, p.getCategory());
            ps.setDouble(4, p.getPrice());
            ps.setInt(5, p.getQuantity());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error addProduct: " + e.getMessage());
            return false;
        }
    }

    /** Obtiene todos los productos del almacén desde la BD. */
    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        try {
            ResultSet rs = getConn().createStatement()
                .executeQuery("SELECT * FROM productos");
            while (rs.next()) {
                list.add(new Product(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("marca"),
                    rs.getString("categoria"),
                    rs.getDouble("precio"),
                    rs.getInt("cantidad_disponible")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getAllProducts: " + e.getMessage());
        }
        return list;
    }

    /** Actualiza los datos de un producto existente. */
    public boolean updateProduct(Product p) {
        try {
            PreparedStatement ps = getConn().prepareStatement(
                "UPDATE productos SET nombre=?, marca=?, categoria=?, precio=?, cantidad_disponible=? WHERE id=?");
            ps.setString(1, p.getName());
            ps.setString(2, p.getBrand());
            ps.setString(3, p.getCategory());
            ps.setDouble(4, p.getPrice());
            ps.setInt(5, p.getQuantity());
            ps.setInt(6, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updateProduct: " + e.getMessage());
            return false;
        }
    }

    /** Elimina un producto por su ID. */
    public boolean deleteProduct(int id) {
        try {
            PreparedStatement ps = getConn().prepareStatement(
                "DELETE FROM productos WHERE id = ?");
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleteProduct: " + e.getMessage());
            return false;
        }
    }
}
