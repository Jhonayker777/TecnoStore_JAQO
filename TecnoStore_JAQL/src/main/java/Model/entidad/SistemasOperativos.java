package Model.entidad;

public class SistemasOperativos {

    private long id;
    private String nombre;

    public SistemasOperativos() {
    }

    public SistemasOperativos(long id, String nombre) {
        this.id = id;
        setNombre(nombre);
    }

    public SistemasOperativos(String nombre) {
        setNombre(nombre);
    }

    public long getId() {
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

}
