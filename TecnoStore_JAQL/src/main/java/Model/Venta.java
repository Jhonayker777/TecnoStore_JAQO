package Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Venta {

    private int id;
    private Cliente cliente;
    private LocalDateTime fecha;
    private List<CelularVenta> items = new ArrayList<>();
    private double total;

    public Venta() {
    }

    public Venta(Cliente cliente, List<CelularVenta> items) {
        this.cliente = cliente;
        this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
    }

    public Precio calcularSubtotal() {
        Precio total = new Precio(0, "cero", 0.0);
        for (CelularVenta item : items) {
            Precio sub = item.getSubtotal();
            if (sub == null) {
                return null;
            }
            total = Operaciones.sumar(total, sub);
        }
        return total;
    }

    public Precio calcularTotal() {
        Precio sub = calcularSubtotal();
        if (sub == null) {
            return null;
        }
        return Operaciones.aplicarIva(sub, 0.19);
    }

    public int getUnidadesTotales() {
        int total = 0;
        for (CelularVenta i : items) {
            total += i.getCantidad();
        }
        return total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente c) {
        this.cliente = c;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime f) {
        this.fecha = f;
    }

    public List<CelularVenta> getItems() {
        return items;
    }

    public void setItems(List<CelularVenta> items) {
        this.items = items;
    }

    public void agregarItem(CelularVenta item) {
        if (item != null) {
            items.add(item);
        }
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return """
               
               id: %s
               Cliente: %s
               Celulares: %s
               total: %s
               """.formatted(id,cliente.getNombre(),items);
    }
    
    
    
}
