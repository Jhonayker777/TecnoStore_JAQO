package View;

import Model.Venta;
import View.validaciones.Decimal;
import View.validaciones.Entero;
import View.validaciones.Entrada;
import View.validaciones.Texto;
import java.util.List;
import java.util.Scanner;

public class VistaVentas {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Texto TEXTO = new Texto(SCANNER);
    private static final Entero ENTERO = new Entero(SCANNER);
    private static final Decimal DECIMAL = new Decimal(SCANNER);
    private static final Entrada entrada = new Entrada(TEXTO, ENTERO, DECIMAL);

    public int menuCarrito() {
        return entrada.entero("""
                              
                              1. Confirmar
                              2. Eliminar
                              3. Vaciar carrito
                              0. volver
                              """);
    }

    public boolean confirmar() {
        String conf = entrada.texto("¿Confirmar compra? (s/n): ");
        return conf.equalsIgnoreCase("s");
    }

    public int pedirId() {
        return entrada.entero("Ingrese el id del prodcuto a eliminar");
    }

    public void listar(List<Venta> lista) {
        System.out.println("------------------Ventas----------------------");
        lista.forEach(System.out::println);
        System.out.println("-----------------------------------------------");
    }
}
