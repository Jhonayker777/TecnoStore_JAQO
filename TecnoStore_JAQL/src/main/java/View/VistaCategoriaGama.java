package View;

import Model.CategoriaGama;
import View.validaciones.Decimal;
import View.validaciones.Entero;
import View.validaciones.Entrada;
import View.validaciones.Texto;
import java.util.Scanner;

public class VistaCategoriaGama {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Texto TEXTO = new Texto(SCANNER);
    private static final Entero ENTERO = new Entero(SCANNER);
    private static final Decimal DECIMAL = new Decimal(SCANNER);
    private static final Entrada entrada = new Entrada(TEXTO, ENTERO, DECIMAL);

    CategoriaGama escogergama() {

        CategoriaGama gama = CategoriaGama.fromId(entrada.enteroGrande("""
                                                                      Seleccione el numero de la gama:
                                                                      1. ALTA
                                                                      2. MEDIA
                                                                      3. BAJA
                                                                      """));
        while (gama == null) {
            gama = CategoriaGama.fromEtiqueta(entrada.texto("Gama no encontrada Intente nuevamente"));
        }
        return gama;
    }
}
