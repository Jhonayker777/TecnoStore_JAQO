package View;

import Model.Empleado;
import View.validaciones.Decimal;
import View.validaciones.Entero;
import View.validaciones.Entrada;
import View.validaciones.Texto;
import java.util.List;
import java.util.Scanner;

public class VistaEmpleados {

    private static final Scanner SCANNER = new Scanner(System.in);

    private static final Texto TEXTO = new Texto(SCANNER);
    private static final Entero ENTERO = new Entero(SCANNER);
    private static final Decimal DECIMAL = new Decimal(SCANNER);

    private static final Entrada entrada = new Entrada(TEXTO, ENTERO, DECIMAL);

    public int vendedor() {

        return entrada.entero("""
                              1. Ver catalogo
                              2. Registrar venta
                              3. ver ventas
                              0. Cerrar sesion
                              """);
    }

    public int bodega() {

        return entrada.entero("""
                              1. Ver catalogo
                              2. Gestion de celulares
                              0. Cerrar sesion
                              """);
    }

    public int administrador() {

        return entrada.entero("""
                              1. Ver catalogo
                              2. Gestion de celulares 
                              3. Gestion de clientes
                              4. Registrar venta
                              5. ver ventas
                              6. Reportes
                              7. Gestion de precios
                              0. Cerrar sesion
                              """);
    }

    public int gerente() {

        return entrada.entero("""
                              1. Ver catalogo
                              2. Gestion de celulares 
                              3. Gestion de clientes
                              4. Registrar venta
                              5. ver ventas
                              6. Reportes
                              7. Gestion de precios
                              8. Gestion de empleados
                              0. Cerrar sesion
                              """);
    }

    public void listar(List<Empleado> lista) {
        for (Empleado empleado : lista) {
            System.out.println(empleado);
        }
    }

}
