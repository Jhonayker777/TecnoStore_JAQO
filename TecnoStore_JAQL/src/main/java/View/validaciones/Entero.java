package View.validaciones;

import java.util.Scanner;

public class Entero {

    private final Scanner sc;

    public Entero(Scanner sc) {
        this.sc = sc;
    }

    public int entero(String mensaje) {
        System.out.println(mensaje);
        while (!sc.hasNextInt()) {
            System.out.println("Error, se espera un valor entero:");
            sc.next();
        }
        int valor = sc.nextInt();
        sc.nextLine(); 
        return valor;
    }

    public long enteroGrande(String mensaje) {
        System.out.println(mensaje);
        while (!sc.hasNextLong()) {
            System.out.println("Error, se espera un valor entero:");
            sc.next();
        }
        long valor = sc.nextLong();
        sc.nextLine(); 
        return valor;
    }
}