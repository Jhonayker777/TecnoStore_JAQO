package Control;

import Dao.VentaDao;
import Model.Carrito;
import Model.Cliente;
import Model.Venta;
import View.VistaReportes;
import View.VistaTienda;
import View.VistaVentas;

public class CarritoControl {

    private final Carrito carrito;
    private final Cliente cliente;
    private final VistaVentas vista = new VistaVentas();
    private final VistaTienda vistaTienda;
    private final VentaDao ventaDao = new VentaDao();
    private final VistaReportes vistaReportes = new VistaReportes();
    private final ReporteControl reporteControl = new ReporteControl();

    public CarritoControl(Cliente cliente, Carrito carrito) {
        this.cliente = cliente;
        this.carrito = carrito;
        this.vistaTienda = new VistaTienda(carrito);
    }

    public void menuCarrito() {
        int op;
        do {
            op = vista.menuCarrito();
            switch (op) {
                case 1 ->
                    registrarCompra();
                case 2 ->
                    eliminar();
                case 3 ->
                    vaciar();
                case 0 ->
                    System.out.println("Volviendo");
                default ->
                    System.out.println("Opción no encontrada");
            }
        } while (op != 0);
    }

    private void registrarCompra() {
        boolean confirmado = vista.confirmar();
        if (!confirmado) {
            System.out.println("Regresando");
            return;
        }

        if (carrito.getItems() == null || carrito.getItems().isEmpty()) {
            System.out.println("El carrito está vacío");
            return;
        }

        Venta venta = new Venta(cliente, carrito.getItems());

        ventaDao.registrar(venta);

        vistaReportes.generar();

        carrito.vaciar();
    }

    private void eliminar() {
        if (carrito.estaVacio()) {
            System.out.println("El carrito está vacío");
            return;
        }

        vistaTienda.verCarrito();

        long celularId = vista.pedirId();

        if (celularId == 0) {
            System.out.println("Cancelado");
            return;
        }

        if (!carrito.contiene(celularId)) {
            System.out.println("No está en el carrito");
            return;
        }

        carrito.eliminar(celularId);
        System.out.println("Eliminado");
    }

    private void vaciar() {
        carrito.vaciar();
        System.out.println("Carrito vaciado");
    }
}
