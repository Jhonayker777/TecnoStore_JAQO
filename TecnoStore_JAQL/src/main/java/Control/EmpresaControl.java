package Control;

import Dao.VentaDao;
import Model.Empleado;
import View.VistaEmpleados;
import View.VistaReportes;
import View.VistaVentas;

public class EmpresaControl {

    private Empleado sesion;
    VentaDao ventasDaO = new VentaDao();

    ClienteControl celientControl = new ClienteControl();
    CelularesControl celular = new CelularesControl();
    Precioscontrol precioscontrol = new Precioscontrol();
    EmpleadosControl empleadosControl = new EmpleadosControl();
    ReporteControl reporteControl = new ReporteControl();

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
                case 4 -> {
                    reporteControl.ventasPorMes();
                }
                case 5 -> {
                    reporteControl.todos();
                }
                case 6 -> {
                    precioscontrol.menu();
                }
                case 7 -> {
                    empleadosControl.menu();
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
                    reporteControl.todos();
                }
                case 5 -> {
                    reporteControl.ventasPorMes();
                }
                case 6 -> {
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
