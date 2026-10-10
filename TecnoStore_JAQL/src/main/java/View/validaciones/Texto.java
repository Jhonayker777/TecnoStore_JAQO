package View.validaciones;

import java.util.Scanner;

public class Texto {

    private final Scanner sc;

    public Texto(Scanner sc) {
        this.sc = sc;
    }

    public String texto(String mensaje) {
        System.out.println(mensaje);
        String texto = sc.nextLine().trim();
        while (texto.isEmpty()) {
            System.out.println("El texto no puede estar vacío. Intente de nuevo:");
            texto = sc.nextLine().trim();
        }
        return texto;
    }
}