package View;

import View.validaciones.Decimal;
import View.validaciones.Entero;
import View.validaciones.Entrada;
import View.validaciones.Texto;
import java.util.Scanner;

public class VistaClientes {

    private static final Scanner SCANNER = new Scanner(System.in);

    private static final Texto TEXTO = new Texto(SCANNER);
    private static final Entero ENTERO = new Entero(SCANNER);
    private static final Decimal DECIMAL = new Decimal(SCANNER);

    private static final Entrada entrada = new Entrada(TEXTO, ENTERO, DECIMAL);

    public int principal() {

        return entrada.entero("""
                              1. Registrar cliente
                              2. Actualizar cliente
                              3. Eliminar 
                              4. Buscar por identificacion
                              5. Listar Todos
                              6. Ver stock bajo
                              0. Volver
                              """);

    }
}
