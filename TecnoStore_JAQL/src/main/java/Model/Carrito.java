package Model;

import java.util.ArrayList;
import java.util.List;

public class Carrito {

    private final List<CelularVenta> items = new ArrayList<>();

    public void agregar(Celular celular, int cantidad) {
        if (celular == null || cantidad <= 0) {
            return;
        }
        for (CelularVenta item : items) {
            if (item.getCelular().getId() == celular.getId()) {
                item.setCantidad(item.getCantidad() + cantidad);
                return;
            }
        }
        items.add(new CelularVenta(celular, cantidad));
    }

    public void eliminar(int celularId) {
        items.removeIf(i -> i.getCelular().getId() == celularId);
    }

    public void vaciar() {
        items.clear();
    }

    public boolean estaVacio() {
        return items.isEmpty();
    }

    public List<CelularVenta> getItems() {
        return items;
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
}
