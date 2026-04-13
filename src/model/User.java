package model;

/**
 * =====================================================
 * PILAR OOP: ENCAPSULAMIENTO
 * =====================================================
 * Todos los atributos son PRIVADOS (private).
 * Solo se acceden desde fuera a través de
 * métodos públicos getters y setters.
 * Esto protege la integridad de los datos del usuario.
 * =====================================================
 */
public class User {

    // ENCAPSULAMIENTO: atributos privados, inaccesibles directamente
    private int    id;
    private String username;
    private String name;
    private String lastname;
    private String phone;
    private String email;
    private String password;

    // Constructor completo (con ID, para usuarios cargados desde BD)
    public User(int id, String username, String name, String lastname,
                String phone, String email, String password) {
        this.id       = id;
        this.username = username;
        this.name     = name;
        this.lastname = lastname;
        this.phone    = phone;
        this.email    = email;
        this.password = password;
    }

    // Constructor sin ID (para nuevos usuarios antes de insertarlos)
    public User(String username, String name, String lastname,
                String phone, String email, String password) {
        this.username = username;
        this.name     = name;
        this.lastname = lastname;
        this.phone    = phone;
        this.email    = email;
        this.password = password;
    }

    // ENCAPSULAMIENTO: getters y setters públicos para acceder a los datos
    public int    getId()                { return id; }
    public void   setId(int id)          { this.id = id; }
    public String getUsername()          { return username; }
    public void   setUsername(String v)  { this.username = v; }
    public String getName()              { return name; }
    public void   setName(String v)      { this.name = v; }
    public String getLastname()          { return lastname; }
    public void   setLastname(String v)  { this.lastname = v; }
    public String getPhone()             { return phone; }
    public void   setPhone(String v)     { this.phone = v; }
    public String getEmail()             { return email; }
    public void   setEmail(String v)     { this.email = v; }
    public String getPassword()          { return password; }
    public void   setPassword(String v)  { this.password = v; }

    @Override
    public String toString() {
        return name + " " + lastname + " (@" + username + ")";
    }
}
