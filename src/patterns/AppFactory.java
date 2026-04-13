package patterns;

import model.Product;
import model.User;

/**
 * =====================================================
 * PATRÓN DE DISEÑO: FÁBRICA (Factory Pattern)
 * =====================================================
 * Centraliza la creación de objetos User y Product.
 * En lugar de usar 'new User(...)' o 'new Product(...)'
 * disperso en todo el código, se delega aquí.
 *
 * Beneficios:
 *  - Si cambia la estructura de User o Product,
 *    solo se modifica en un lugar.
 *  - Código más limpio y mantenible.
 *  - Separa la lógica de creación del resto del sistema.
 * =====================================================
 */
public class AppFactory {

    /**
     * FÁBRICA: crea un User nuevo sin ID (para registro).
     * El ID lo asigna la base de datos automáticamente.
     */
    public static User createUser(String username, String name, String lastname,
                                   String phone, String email, String password) {
        return new User(username, name, lastname, phone, email, password);
    }

    /**
     * FÁBRICA: crea un User con ID (para actualizaciones).
     * Se usa cuando ya existe el usuario en la base de datos.
     */
    public static User createUserWithId(int id, String username, String name, String lastname,
                                         String phone, String email, String password) {
        return new User(id, username, name, lastname, phone, email, password);
    }

    /**
     * FÁBRICA: crea un Product nuevo sin ID (para registro).
     * El ID lo asigna la base de datos automáticamente.
     */
    public static Product createProduct(String name, String brand, String category,
                                         double price, int quantity) {
        return new Product(name, brand, category, price, quantity);
    }

    /**
     * FÁBRICA: crea un Product con ID (para actualizaciones).
     * Se usa cuando ya existe el producto en la base de datos.
     */
    public static Product createProductWithId(int id, String name, String brand, String category,
                                               double price, int quantity) {
        return new Product(id, name, brand, category, price, quantity);
    }
}
