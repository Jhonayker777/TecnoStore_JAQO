package Model.entidad;

public class Precio {

    private long id;
    private String nombre;
    private double monto;

    public Precio(long id, String nombre, double monto) {
        this.id = id;
        this.nombre = nombre;
        this.monto = monto;
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

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

}
