package controller;

import database.DatabaseManager;
import model.User;
import java.util.List;

/**
 * =====================================================
 * CAPA: CONTROLADOR (MVC - Model View Controller)
 * =====================================================
 * Maneja la lógica de gestión de usuarios (CRUD).
 * La vista (UsersPanel) llama a este controlador,
 * que a su vez interactúa con DatabaseManager.
 *
 * Usa el SINGLETON de DatabaseManager.
 * =====================================================
 */
public class UserController {

    // SINGLETON: acceso a la única instancia de la base de datos
    private DatabaseManager db = DatabaseManager.getInstance();

    /** Obtiene la lista completa de usuarios registrados. */
    public List<User> getAllUsers()   { return db.getAllUsers(); }

    /** Actualiza los datos de un usuario existente. */
    public boolean updateUser(User u) { return db.updateUser(u); }

    /** Elimina un usuario por su ID. */
    public boolean deleteUser(int id) { return db.deleteUser(id); }
}
