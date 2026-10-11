package Model.entidad;

public class Marca {

    private long id;
    private String nombre;

    public Marca(long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return """
               id: %s
               Nombre: %s
               """.formatted(id, nombre);
    }

}
