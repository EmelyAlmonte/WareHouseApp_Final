package model;

/**
 * =====================================================
 * PILAR OOP: ENCAPSULAMIENTO
 * =====================================================
 * Todos los atributos del producto son PRIVADOS.
 * El acceso y modificación se realiza exclusivamente
 * a través de getters y setters públicos.
 * =====================================================
 */
public class Product {

    // ENCAPSULAMIENTO: atributos privados
    private int    id;
    private String name;
    private String brand;
    private String category;
    private double price;
    private int    quantity;

    // Constructor completo (con ID, para productos cargados desde BD)
    public Product(int id, String name, String brand, String category,
                   double price, int quantity) {
        this.id       = id;
        this.name     = name;
        this.brand    = brand;
        this.category = category;
        this.price    = price;
        this.quantity = quantity;
    }

    // Constructor sin ID (para nuevos productos antes de insertarlos)
    public Product(String name, String brand, String category,
                   double price, int quantity) {
        this.name     = name;
        this.brand    = brand;
        this.category = category;
        this.price    = price;
        this.quantity = quantity;
    }

    // ENCAPSULAMIENTO: getters y setters públicos
    public int    getId()               { return id; }
    public void   setId(int id)         { this.id = id; }
    public String getName()             { return name; }
    public void   setName(String v)     { this.name = v; }
    public String getBrand()            { return brand; }
    public void   setBrand(String v)    { this.brand = v; }
    public String getCategory()         { return category; }
    public void   setCategory(String v) { this.category = v; }
    public double getPrice()            { return price; }
    public void   setPrice(double v)    { this.price = v; }
    public int    getQuantity()         { return quantity; }
    public void   setQuantity(int v)    { this.quantity = v; }

    @Override
    public String toString() {
        return name + " — " + brand;
    }
}
