package View;

import Dao.CelularDao;
import Model.Carrito;
import Model.Celular;
import Model.CelularVenta;
import Model.Cliente;
import View.validaciones.Decimal;
import View.validaciones.Entero;
import View.validaciones.Entrada;
import View.validaciones.Texto;
import java.util.List;
import java.util.Scanner;

public class VistaTienda {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Texto TEXTO = new Texto(SCANNER);
    private static final Entero ENTERO = new Entero(SCANNER);
    private static final Decimal DECIMAL = new Decimal(SCANNER);
    private static final Entrada entrada = new Entrada(TEXTO, ENTERO, DECIMAL);

    CelularDao celularDao = new CelularDao();
    Carrito carrito;

    public VistaTienda(Carrito carrito) {
        this.carrito = carrito;
    }

    public int principal() {

        return entrada.entero("""
                              
                              1. Ver catalogo
                              2. Buscar celular
                              3. ver por marca
                              4. ver por gama
                              5. Mi carrito
                              6. Mis compras
                              7. Mi perfil
                              8. Cambiar contraseña
                              0. Cerrar sesion
                              """);

    }

    public void catalogo(List<Celular> celulares) {
        System.out.println("------------------Catalogo----------------------");

        for (Celular c : celulares) {
            System.out.printf("%d. %s %s - $%.2f (stock: %d)%n",
                    c.getId(), c.getMarca().getNombre(), c.getModelo(),
                    c.getPrecio().getMonto(), c.getStock());
        }
        System.out.println("------------------------------------------------");

        long id = entrada.enteroGrande("Ingrese el id para agregar al carrito - 0 para salir");
        if (id == 0) {
            System.out.println("Saliendo al menu principal");
        } else {
            agregarAlCarrito(id);
        }

    }

    private void agregarAlCarrito(long id) {
        Celular cel = celularDao.buscar(id);

        if (cel == null) {
            System.out.println("No se encontro el celular");
            return;
        }
        int cant = entrada.entero("Cantidad: ");

        if (cant <= 0) {
            System.out.println("Cantidad inválida");
            return;
        }
        if (cant > cel.getStock()) {
            System.out.println("Stock insuficiente (disponible: " + cel.getStock() + ")");
            return;
        }

        carrito.agregar(cel, cant);
        System.out.println("Agregado: " + cel.getModelo() + " x " + cant);
    }

    public void verCarrito() {
        for (CelularVenta celular : carrito.getItems()) {
            System.out.println("------------------Carrito----------------------");
            System.out.println(celular);
            System.out.println("------------------------------------------------");
        }
        System.out.println("Subtotal: " + carrito.calcularSubtotal().getMonto());
        System.out.println("Total:    " + carrito.calcularTotal().getMonto());

    }
}
