package View;

import Model.Cargo;
import View.validaciones.Decimal;
import View.validaciones.Entero;
import View.validaciones.Entrada;
import View.validaciones.Texto;
import java.util.Scanner;

public class VistaCargo {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Texto TEXTO = new Texto(SCANNER);
    private static final Entero ENTERO = new Entero(SCANNER);
    private static final Decimal DECIMAL = new Decimal(SCANNER);
    private static final Entrada entrada = new Entrada(TEXTO, ENTERO, DECIMAL);

    Cargo escogerCargo() {

        Cargo cargo = Cargo.fromEtiqueta(entrada.texto("""
                                                                      Seleccione el cargo:
                                                                      - Admin
                                                                      - Bodega
                                                                      - Gerente
                                                                      """));
        while (cargo == null) {
            cargo = Cargo.fromEtiqueta(entrada.texto("Gama no encontrada Intente nuevamente"));
        }
        return cargo;
    }
}
