package Control;

import Dao.VentaDao;
import Model.Empleado;
import View.VistaEmpleados;
import View.VistaVentas;

public class EmpresaControl {

    private Empleado sesion;
    VentaDao ventasDaO = new VentaDao();
    ClienteControl celientControl = new ClienteControl();
    CelularesControl celular = new CelularesControl();
    Precioscontrol precioscontrol = new Precioscontrol();

    VistaEmpleados vista = new VistaEmpleados();
    VistaVentas vistaVentas = new VistaVentas();

    public EmpresaControl(Empleado sesion) {
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
                case 5 -> {
                    vistaVentas.listar(ventasDaO.listar());
                }
                case 6 -> {
                    System.out.println("Reportes");
                }
                case 7 -> {
                    precioscontrol.menu();
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
                case 5 -> {
                    vistaVentas.listar(ventasDaO.listar());
                }
                case 6 -> {
                    System.out.println("Reportes");
                }
                case 7 -> {
                    precioscontrol.menu();
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
