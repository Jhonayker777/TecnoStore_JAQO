package Model;

public class CelularVenta {

    private int id;
    private Celular celular;
    private int cantidad;
    private Precio precioUnitario;

    public CelularVenta() {
    }

    public CelularVenta(Celular celular, int cantidad) {
        this.celular = celular;
        this.cantidad = cantidad;
        this.precioUnitario = celular != null ? celular.getPrecio() : null;
    }

    public Precio getSubtotal() {
        if (precioUnitario == null) {
            return null;
        }
        return Operaciones.multiplicar(precioUnitario, cantidad);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Celular getCelular() {
        return celular;
    }

    public void setCelular(Celular c) {
        this.celular = c;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int c) {
        if (c > 0) {
            this.cantidad = c;
        }
    }

    public Precio getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Precio p) {
        this.precioUnitario = p;
    }

    @Override
    public String toString() {
        return """
               
               Celular: %s
               Cantidad: %s
               PrecioUnitario: %s
               """.formatted(celular.getModelo(), cantidad, precioUnitario.getMonto());
    }

}
