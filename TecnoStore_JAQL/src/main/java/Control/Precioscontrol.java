package Control;

import Dao.PreciosDao;
import View.VistaPrecio;

public class Precioscontrol {

    VistaPrecio vista = new VistaPrecio();
    PreciosDao preciosDao = new PreciosDao();

    public void menu() {

        int op;
        do {
            op = vista.menu();
            switch (op) {
                case 1 ->
                    crear();
                case 2 ->
                    actualizar();
                case 3 ->
                    eliminar();
                case 4 ->
                    listar();
                case 0 ->
                    System.out.println("Saliendo es saliendo");
                default ->
                    System.out.println("Opcion no encontrada");
            }
        } while (op != 0);
    }

    private void crear() {
        preciosDao.agregar(vista.crearPrecio());
    }

    private void actualizar() {
        if (listar()) {
            preciosDao.actualizar(vista.ActualizarPrecio());
        }
    }

    private void eliminar() {
        if (listar()) {
            preciosDao.eliminar(vista.eliminar());
        }
    }

    private boolean listar() {
        if (!preciosDao.listar().isEmpty()) {
            vista.listar(preciosDao.listar());
            return true;
        } else {
            System.out.println("Lista vacia");
            return false;
        }
    }

}
