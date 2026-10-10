package Control;

import View.Login;
import View.validaciones.Decimal;
import View.validaciones.Entero;
import View.validaciones.Entrada;
import View.validaciones.Texto;
import java.util.Scanner;

public class LoginControl {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Texto TEXTO = new Texto(SCANNER);
    private static final Entero ENTERO = new Entero(SCANNER);
    private static final Decimal DECIMAL = new Decimal(SCANNER);
    private static final Entrada entrada = new Entrada(TEXTO, ENTERO, DECIMAL);

    Login login = new Login();


    public void menu() {
        int op;
        do {
            op = login.general();
            switch (op) {
                case 1 ->
                    Cliente();
                case 2 ->
                    Empleado();
                default ->
                    System.out.println("Opcion no encontrada");
            }
        } while (op != 3);
    }

    private void Cliente() {
        String correo = login.correo();
    }

    private void Empleado() {

    }
    
    
    
    
    
}
