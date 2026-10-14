package View;

import Model.Empleado;
import View.validaciones.Decimal;
import View.validaciones.Entero;
import View.validaciones.Entrada;
import View.validaciones.Texto;
import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class VistaEmpleados {

    private static final Scanner SCANNER = new Scanner(System.in);

    private static final Texto TEXTO = new Texto(SCANNER);
    private static final Entero ENTERO = new Entero(SCANNER);
    private static final Decimal DECIMAL = new Decimal(SCANNER);

    private static final Entrada entrada = new Entrada(TEXTO, ENTERO, DECIMAL);

    VistaCargo vistaCargo = new VistaCargo();

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

    public int empleado() {

        return entrada.entero("""
                              1. Registrar 
                              2. Actualizar
                              3. Cambiar estado 
                              4. Listar
                              """);
    }

    public boolean estado() {
        int op = entrada.entero("""
                            1.Activar
                            2.Desactivar
                            """);
        while (op > 2 || op < 1) {
            op = entrada.entero("Opcion no valida");
        }
        return 1 == op ? true : false;
    }

    public void listar(List<Empleado> lista) {
        for (Empleado empleado : lista) {
            System.out.println(empleado);
        }
    }

    public long pedirId() {
        return entrada.enteroGrande("Ingrese el id del empleado");

    }

    public Empleado registrar() {
        return Empleado.builder()
                .nombre(entrada.texto("Ingrese el nombre del empleado"))
                .identificacion(entrada.texto("Ingrese la identificacion del empleado"))
                .correo(entrada.texto("Ingrese el correo del empleado"))
                .telefono(entrada.texto("Ingrese el telefono del empleado"))
                .cargo(vistaCargo.escogerCargo())
                .salario(entrada.decimal("Ingrese el salario del empeado"))
                .fechaIngreso(Date.valueOf(entrada.texto("Escriba la fecha de ingreso(formato yyy-mm-dd)")))
                .contraseña(entrada.texto("Ingrese su caontraseña"))
                .build();
    }

    public Empleado actualizar() {
        long id = pedirId();
        return Empleado.builder()
                .id(id)
                .nombre(entrada.texto("Ingrese el nombre del empleado"))
                .identificacion(entrada.texto("Ingrese la identificacion del empleado"))
                .correo(entrada.texto("Ingrese el correo del empleado"))
                .telefono(entrada.texto("Ingrese el telefono del empleado"))
                .cargo(vistaCargo.escogerCargo())
                .salario(entrada.decimal("Ingrese el salario del empeado"))
                .fechaIngreso(Date.valueOf(entrada.texto("Escriba la fecha de ingreso(formato yyy-mm-dd)")))
                .contraseña(entrada.texto("Ingrese su caontraseña"))
                .build();
    }

}
