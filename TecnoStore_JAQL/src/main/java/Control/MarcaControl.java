package Control;

import Dao.MarcaDao;
import Model.Marca;
import View.VistaMarca;
import View.validaciones.Decimal;
import View.validaciones.Entero;
import View.validaciones.Entrada;
import View.validaciones.Texto;
import java.util.Scanner;

public class MarcaControl {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Texto TEXTO = new Texto(SCANNER);
    private static final Entero ENTERO = new Entero(SCANNER);
    private static final Decimal DECIMAL = new Decimal(SCANNER);
    private static final Entrada entrada = new Entrada(TEXTO, ENTERO, DECIMAL);
    
    VistaMarca vista = new VistaMarca();
    MarcaDao marcas = new MarcaDao();
    
    
    public Marca escogerMarca(){    
        vista.listar(marcas.listar());
        int op = entrada.entero("Seleccione el de la marca a escoger");
        return marcas.buscar(op);
    }
    
}
