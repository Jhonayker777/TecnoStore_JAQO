package Model.entidad;

public abstract class Persona {

    private int id;
    private String nombre;
    private String identificacion;
    private String correo;
    private String telefono;

    protected Persona() {
    }

    protected Persona(int id, String nombre, String identificacion,
            String correo, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.correo = correo;
        this.telefono = telefono;
    }

    public abstract String getRol();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre.trim();
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion.trim();
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo.toLowerCase().trim();
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono.trim();
    }

    @Override
    public String toString() {
        return """
               Id: %s
               Nombre: %S
               Identificacion: %S
               Correo: %s
               Telefono: %s
               """.formatted(id,nombre,identificacion,correo,telefono);
    }
    
    
}
