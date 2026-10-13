package Control;

import Dao.CelularDao;
import Model.Celular;
import View.VistaCelulares;
import View.validaciones.Decimal;
import View.validaciones.Entero;
import View.validaciones.Entrada;
import View.validaciones.Texto;
import java.util.Scanner;

public class CelularesControl {

    private static final Scanner SCANNER = new Scanner(System.in);

    private static final Texto TEXTO = new Texto(SCANNER);
    private static final Entero ENTERO = new Entero(SCANNER);
    private static final Decimal DECIMAL = new Decimal(SCANNER);

    private static final Entrada entrada = new Entrada(TEXTO, ENTERO, DECIMAL);

    VistaCelulares vista = new VistaCelulares();
    CelularDao celular = new CelularDao();

    public void administrador() {
        int op;
        do {
            op = vista.principal();
            switch (op) {
                case 1 -> {
                    celular.agregar(vista.ingresarDatos());
                }
                case 2 -> {
                    actualizar();
                }
                case 3 -> {
                    eliminar();
                }
                case 4 -> {
                    catalogo();
                }
                case 5 -> {
                    Celular celu = celular.buscar(entrada.enteroGrande("Ingrese el id del celular"));
                    System.out.println(celu);
                }
                case 0 -> {
                    System.out.println("");
                }
                default -> {
                    System.out.println("Opcion no encontrada!");
                }
            }
        } while (op != 0);
    }

    private void actualizar() {
        vista.mostrar(celular.listar());
        Celular celu = celular.buscar(entrada.enteroGrande("Ingrese el id del celular"));
        if (celu == null) {
            System.out.println("No se encontro el celulara a actualizar");
        } else {
            celular.actualizar(vista.actualizarDatos(celu.getId()));
        }
    }

    private void eliminar() {
        vista.mostrar(celular.listar());
        Celular celu = celular.buscar(entrada.enteroGrande("Ingrese el id del celular"));
        if (celu == null) {
            System.out.println("No se encontro el celulara a eliminar");
        } else {
            celular.eliminar(celu.getId());
        }
    }

    public void catalogo() {
        if (celular.listar() != null) {
            vista.mostrar(celular.listar());
        } else {
            System.out.println("El catalogo esta vacio");
        }
        System.out.println("");
    }
}
