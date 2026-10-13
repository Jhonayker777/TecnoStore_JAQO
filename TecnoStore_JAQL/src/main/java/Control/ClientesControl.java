package Control;

import Model.Cliente;
import View.VistaTienda;

public class ClientesControl {

    VistaTienda vista = new VistaTienda();

    private Cliente cliente;

    public ClientesControl(Cliente cliente) {
        this.cliente = cliente;
    }

    public void menu() {
        int op;
        do {
            op = vista.principal();
            switch (op) {
                case 1 -> {
                    System.out.println("Ver catalogo");
                }
                case 2 -> {
                    System.out.println("Buscar celular");
                }
                case 3 -> {
                    System.out.println("Ver por marca");
                }
                case 4 -> {
                    System.out.println("Ver gama");
                }
                case 5 -> {
                    System.out.println("Mi carrito");
                }
                case 6 -> {
                    System.out.println("Mis compras");
                }
                case 7 -> {
                    System.out.println("Mi perfil");
                }
                case 8 -> {
                    System.out.println("Cambiar contraseña");
                }
                case 0 -> {
                }
                default -> {
                    System.out.println("Opcion no encontrada!");
                }
            }
        } while (op != 0);
    }
}
