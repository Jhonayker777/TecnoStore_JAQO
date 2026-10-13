package View;

import Dao.SistemasOperativosDao;
import Model.SistemasOperativos;
import View.validaciones.Decimal;
import View.validaciones.Entero;
import View.validaciones.Entrada;
import View.validaciones.Texto;
import java.util.List;
import java.util.Scanner;

public class VistaSistemaOperativo {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Texto TEXTO = new Texto(SCANNER);
    private static final Entero ENTERO = new Entero(SCANNER);
    private static final Decimal DECIMAL = new Decimal(SCANNER);
    private static final Entrada entrada = new Entrada(TEXTO, ENTERO, DECIMAL);

    SistemasOperativosDao so = new SistemasOperativosDao();

    public SistemasOperativos escogerSistemaOperativo() {
        if (so.listar() != null) {
            listar(so.listar());
        } else {
            System.out.println("No hay sistemas operativos para escoger");
            return null;
        }
        int op = entrada.entero("ingrese el id del sistema operativo a escoger escoger");
        SistemasOperativos sistemaOperativo = so.buscar(op);
        while (sistemaOperativo == null) {
            op = entrada.entero("Sistema operativo no encontrado, intente de nuevo");
            sistemaOperativo = so.buscar(op);
        }
        return sistemaOperativo;
    }

    public void listar(List<SistemasOperativos> lista) {
        System.out.println("------------------Sistemas Operativos----------------------");
        lista.forEach(System.out::println);
        System.out.println("-----------------------------------------------------------");
    }
}
