package View;

import View.validaciones.Decimal;
import View.validaciones.Entero;
import View.validaciones.Entrada;
import View.validaciones.Texto;
import java.util.Scanner;

public class Login {

    private static final Scanner SCANNER = new Scanner(System.in);

    private static final Texto TEXTO = new Texto(SCANNER);
    private static final Entero ENTERO = new Entero(SCANNER);
    private static final Decimal DECIMAL = new Decimal(SCANNER);

    private static final Entrada entrada = new Entrada(TEXTO, ENTERO, DECIMAL);

    public void Inicio() {

        System.out.println("""
                                TECNO STORE
                                INICIO DE SESION
                           """);
    }

     public int general() {
        return entrada.entero("""
                              1- Cliente.
                              2- Empleado.
                              3- Salir.
                              """);
    }
    
    public String correo() {
        return entrada.texto("Ingrese su correo:");
    }
    
    public String contraseña(){
        return entrada.texto("Ingrese su contraseña:");
    }

}
