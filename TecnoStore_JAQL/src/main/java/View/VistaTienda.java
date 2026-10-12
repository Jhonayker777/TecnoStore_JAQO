package View;

import View.validaciones.Decimal;
import View.validaciones.Entero;
import View.validaciones.Entrada;
import View.validaciones.Texto;
import java.util.Scanner;

public class VistaTienda {

    private static final Scanner SCANNER = new Scanner(System.in);

    private static final Texto TEXTO = new Texto(SCANNER);
    private static final Entero ENTERO = new Entero(SCANNER);
    private static final Decimal DECIMAL = new Decimal(SCANNER);

    private static final Entrada entrada = new Entrada(TEXTO, ENTERO, DECIMAL);

    public int principal() {

        return entrada.entero("""
                              1. Ver catalogo
                              2. Buscar celular
                              3. ver por marca
                              4. ver por gama
                              5. Mi carrito
                              6. Mis compras
                              7. Mi perfil
                              8. Cambiar contraseña
                              0. Cerrar sesion
                              """);

    }
}
