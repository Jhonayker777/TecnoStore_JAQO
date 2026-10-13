package Control;

import Dao.SistemasOperativosDao;
import Model.SistemasOperativos;
import View.VistaSistemaOperativo;
import View.validaciones.Decimal;
import View.validaciones.Entero;
import View.validaciones.Entrada;
import View.validaciones.Texto;
import java.util.Scanner;

public class SistemaOperativoControl {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Texto TEXTO = new Texto(SCANNER);
    private static final Entero ENTERO = new Entero(SCANNER);
    private static final Decimal DECIMAL = new Decimal(SCANNER);
    private static final Entrada entrada = new Entrada(TEXTO, ENTERO, DECIMAL);
    
    VistaSistemaOperativo vista = new VistaSistemaOperativo();
    SistemasOperativosDao so = new SistemasOperativosDao();
    
    
    public SistemasOperativos escogerSistemaOperativo(){    
        vista.listar(so.listar());
        int op = entrada.entero("Seleccione el de la marca a escoger");
        return so.buscar(op);
    }
}
