package View;

import Dao.PreciosDao;
import Model.Precio;
import View.validaciones.Decimal;
import View.validaciones.Entero;
import View.validaciones.Entrada;
import View.validaciones.Texto;
import java.util.List;
import java.util.Scanner;

public class VistaPrecio {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Texto TEXTO = new Texto(SCANNER);
    private static final Entero ENTERO = new Entero(SCANNER);
    private static final Decimal DECIMAL = new Decimal(SCANNER);
    private static final Entrada entrada = new Entrada(TEXTO, ENTERO, DECIMAL);

    PreciosDao p = new PreciosDao();

    public Precio escogerPrecio() {
        if (p.listar() != null) {
            listar(p.listar());
        } else {
            System.out.println("No hay precios para escoger");
            return null;
        }
        int op = entrada.entero("ingrese el id del precio a escoger");
        Precio precio = p.buscar(op);
        while (precio == null) {
            op = entrada.entero("Precio no encontrada, intente de nuevo");
            precio = p.buscar(op);
        }
        return precio;
    }

    public void listar(List<Precio> lista) {
        System.out.println("------------------Precios----------------------");
        lista.forEach(System.out::println);
        System.out.println("-----------------------------------------------");
    }
}
