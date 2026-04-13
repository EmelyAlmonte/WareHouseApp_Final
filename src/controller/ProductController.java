package controller;

import database.DatabaseManager;
import model.Product;
import java.util.List;

/**
 * =====================================================
 * CAPA: CONTROLADOR (MVC - Model View Controller)
 * =====================================================
 * Maneja la lógica de gestión de productos (CRUD).
 * La vista (ProductsPanel) llama a este controlador,
 * que a su vez interactúa con DatabaseManager.
 *
 * Usa el SINGLETON de DatabaseManager.
 * =====================================================
 */
public class ProductController {

    // SINGLETON: acceso a la única instancia de la base de datos
    private DatabaseManager db = DatabaseManager.getInstance();

    /** Obtiene la lista completa de productos del almacén. */
    public List<Product> getAllProducts()    { return db.getAllProducts(); }

    /** Agrega un nuevo producto al almacén. */
    public boolean addProduct(Product p)    { return db.addProduct(p); }

    /** Actualiza los datos de un producto existente. */
    public boolean updateProduct(Product p) { return db.updateProduct(p); }

    /** Elimina un producto por su ID. */
    public boolean deleteProduct(int id)    { return db.deleteProduct(id); }
}
