package Model.entidad;

public class SistemasOperativos {

    private int id;
    private String nombre;

    public SistemasOperativos() {
    }

    public SistemasOperativos(int id, String nombre) {
        this.id = id;
        setNombre(nombre);
    }

    public SistemasOperativos(String nombre) {
        setNombre(nombre);
    }

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

}
