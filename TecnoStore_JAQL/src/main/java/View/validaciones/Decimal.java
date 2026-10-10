package View.validaciones;

import java.util.Scanner;

public class Decimal {

    private final Scanner sc;

    public Decimal(Scanner sc) {
        this.sc = sc;
    }

    public double decimal(String mensaje) {
        System.out.println(mensaje);
        while (!sc.hasNextDouble()) {
            System.out.println("Error, se espera un valor decimal:");
            sc.next();
        }
        double valor = sc.nextDouble();
        sc.nextLine();
        return valor;
    }
}