package Control;

import Dao.EmpleadoDao;
import View.VistaEmpleados;

public class EmpleadosControl {

    VistaEmpleados vistaEmpleados = new VistaEmpleados();
    EmpleadoDao empleadoDao = new EmpleadoDao();

    public void menu() {
        int op;
        do {
            op = vistaEmpleados.gestionEmpleado();
            switch (op) {
                case 1 -> {
                    registrar();
                }
                case 2 -> {
                    actualizar();
                }
                case 3 -> {
                    estado();
                }
                case 4 -> {
                    listar();
                }
                case 0 -> {
                    System.out.println("Saliendo");
                }
                default -> {
                    System.out.println("Opcion no encontrada!");
                }
            }
        } while (op != 0);
    }

    private void registrar() {
        empleadoDao.registrar(vistaEmpleados.registrar());
    }

    private void actualizar() {

        if (listar()) {
            empleadoDao.actualizarDatos(vistaEmpleados.actualizar());
        }
    }

    private void estado() {

        if (listar()) {
            int op;
            do {
                op = vistaEmpleados.estado();
                switch (op) {
                    case 1 -> {
                        empleadoDao.activar(vistaEmpleados.pedirId());
                    }
                    case 2 -> {
                        empleadoDao.desactivar(vistaEmpleados.pedirId());
                    }
                    case 0 -> {
                        System.out.println("Saliendo");
                    }
                    default -> {
                        System.out.println("Opcion no encontrada!");
                    }
                }

            } while (op != 0);
        }
    }

    private boolean listar() {
        if (!empleadoDao.listar().isEmpty()) {
            vistaEmpleados.listar(empleadoDao.listar());
            return true;
        } else {
            System.out.println("No hay empleados para listar");
            return false;
        }
    }
}
