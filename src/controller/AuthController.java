package controller;

import database.DatabaseManager;
import model.User;

/**
 * =====================================================
 * CAPA: CONTROLADOR (MVC - Model View Controller)
 * =====================================================
 * Maneja la lógica de autenticación.
 * Separa la lógica de negocio de la interfaz gráfica.
 *
 * Usa el SINGLETON de DatabaseManager para acceder
 * a la base de datos sin crear nuevas instancias.
 * =====================================================
 */
public class AuthController {

    // SINGLETON: acceso a la base de datos mediante getInstance()
    private DatabaseManager db = DatabaseManager.getInstance();

    /** Intenta autenticar al usuario. Retorna true si las credenciales son correctas. */
    public boolean login(String username, String password) {
        return db.authenticate(username, password) != null;
    }

    /** Registra un nuevo usuario. Retorna false si el username ya existe. */
    public boolean register(User user) {
        return db.registerUser(user);
    }
}
