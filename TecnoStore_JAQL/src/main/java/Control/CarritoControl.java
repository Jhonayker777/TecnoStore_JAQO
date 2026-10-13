package Control;

import Model.Carrito;
import Model.Cliente;
import Model.Venta;
import View.VistaVentas;

public class CarritoControl {

    private Carrito carrito;
    private final Cliente cliente;
    private VistaVentas vista = new VistaVentas();

    public CarritoControl(Cliente cliente, Carrito carrito) {
        this.cliente = cliente;
        this.carrito = carrito;
    }

    public void menuCarrito() {
        int op;
        do {
            op = vista.menuCarrito();
            switch (op) {
                case 1 -> {
                    registrarCompra();
                }
                case 2 -> {
                }
                case 0 -> {
                    System.out.println("Cerrando sesion...");
                }
                default -> {
                    System.out.println("Opcion no encontrada!");
                }
            }
        } while (op != 0);
    }

    private void registrarCompra() {
        boolean c = vista.confirmar();
        if (c) {
            if (carrito.getItems() == null || carrito.getItems().isEmpty()) {
                System.out.println("El carrito esta vacio");
                return;
            }
            Venta venta = new Venta(cliente, carrito.getItems());

        } else {
            System.out.println("Regresando");
        }
    }
}
