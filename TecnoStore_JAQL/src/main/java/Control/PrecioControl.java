package Control;

import Dao.PreciosDao;
import Model.Marca;
import Model.Precio;
import View.VistaPrecio;
import View.validaciones.Decimal;
import View.validaciones.Entero;
import View.validaciones.Entrada;
import View.validaciones.Texto;
import java.util.Scanner;

public class PrecioControl {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Texto TEXTO = new Texto(SCANNER);
    private static final Entero ENTERO = new Entero(SCANNER);
    private static final Decimal DECIMAL = new Decimal(SCANNER);
    private static final Entrada entrada = new Entrada(TEXTO, ENTERO, DECIMAL);

    VistaPrecio vista = new VistaPrecio();
    PreciosDao precio = new PreciosDao();

    public Precio escogerPrecio() {
        vista.listar(precio.listar());
        int op = entrada.entero("Seleccione el de la marca a escoger");
        return precio.buscar(op);
    }
}
