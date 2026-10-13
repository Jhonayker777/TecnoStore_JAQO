package Control;

import Dao.ClientesDao;
import Model.Empleado;
import View.VistaEmpleados;

public class EmpleadosControl {

    ClienteControl celientControl = new ClienteControl();
    CelularesControl celular = new CelularesControl();
    VistaEmpleados vista = new VistaEmpleados();
    private Empleado sesion;

    public EmpleadosControl(Empleado sesion) {
        this.sesion = sesion;
    }

    public void gerente() {
        int op;
        do {
            op = vista.gerente();
            switch (op) {
                case 1 -> {
                    celular.catalogo();
                }
                case 2 -> {
                    celular.administrador();
                }
                case 3 -> {
                    celientControl.administrar();
                }
                case 4 -> {
                    System.out.println("Registrar venta");
                }
                case 5 -> {
                    System.out.println("Ver ventas");
                }
                case 6 -> {
                    System.out.println("Reportes");
                }
                case 7 -> {
                    System.out.println("Gestion precios");
                }
                case 8 -> {
                    System.out.println("Gestion empleados");
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

    public void administrador() {
        int op;
        do {
            op = vista.administrador();
            switch (op) {
                case 1 -> {
                    celular.catalogo();
                }
                case 2 -> {
                    celular.administrador();
                }
                case 3 -> {
                    celientControl.administrar();
                }
                case 4 -> {
                    System.out.println("Registrar venta");
                }
                case 5 -> {
                    System.out.println("Ver ventas");
                }
                case 6 -> {
                    System.out.println("Reportes");
                }
                case 7 -> {
                    System.out.println("Gestion precios");
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

    public void vededor() {
        int op;
        do {
            op = vista.vendedor();
            switch (op) {
                case 1 -> {
                    celular.catalogo();
                }
                case 2 -> {
                    System.out.println("Registrar venta");
                }
                case 3 -> {
                    System.out.println("ver ventas");
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

    public void bodega() {
        int op;
        do {
            op = vista.bodega();
            switch (op) {
                case 1 -> {
                    celular.catalogo();
                }
                case 2 -> {
                    celular.administrador();
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

}
