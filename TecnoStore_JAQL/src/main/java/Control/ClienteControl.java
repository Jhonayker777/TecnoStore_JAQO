package Control;

import Dao.ClientesDao;
import Model.Cliente;
import View.VistaClientes;
import View.validaciones.Decimal;
import View.validaciones.Entero;
import View.validaciones.Entrada;
import View.validaciones.Texto;
import java.util.Scanner;

public class ClienteControl {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Texto TEXTO = new Texto(SCANNER);
    private static final Entero ENTERO = new Entero(SCANNER);
    private static final Decimal DECIMAL = new Decimal(SCANNER);
    private static final Entrada entrada = new Entrada(TEXTO, ENTERO, DECIMAL);

    VistaClientes clienteVista = new VistaClientes();
    ClientesDao clientesDao = new ClientesDao();

    public void administrar() {
        int op;
        do {
            op = clienteVista.principal();
            switch (op) {
                case 1 -> {
                    registrar();
                }
                case 2 -> {
                    actualizar();
                }
                case 4 -> {
                    buscarDocuemnto();
                }
                case 5 -> {
                    listar();
                }
                case 0 -> {
                    System.out.println("Saliendo");
                }
                default -> {
                    System.out.println("Opcion no encontrada!");
                }
            }
        } while (op != 0);
    }

    protected void registrar() {
        clientesDao.registrar(clienteVista.registrar());
    }

    protected void actualizar() {
        listar();
        Cliente cliente = clientesDao.buscarId(entrada.enteroGrande("Ingrese el id a actualizar"));
        if (cliente != null) {
            clientesDao.actualizar(clienteVista.actualizar(cliente.getId()));
        } else {
            System.out.println("No se encontro el cliente a actualizar");
        }
    }

    protected void listar() {
        if (clientesDao.listar() != null) {
            clienteVista.mostrar(clientesDao.listar());
<<<<<<< HEAD
        } else {
=======
        }else{
>>>>>>> 673be06c8c8aca8d70ca23c23fc57804078ad080
            System.out.println("No hay clientes registrados");
        }
    }

<<<<<<< HEAD
    protected void buscarDocuemnto() {
        String op = entrada.texto("Ingrese la identificacion");
        Cliente cliente = clientesDao.buscarDocumento(op);
        if (cliente != null) {
            System.out.println(cliente);
        } else {
            System.out.println("Cliente no encontrado");
        }

    }

=======
>>>>>>> 673be06c8c8aca8d70ca23c23fc57804078ad080
}
