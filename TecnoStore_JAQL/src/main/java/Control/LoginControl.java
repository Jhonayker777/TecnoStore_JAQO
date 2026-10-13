package Control;

import Dao.ClientesDao;
import Dao.EmpleadoDao;
import Model.Carrito;
import Model.Cliente;
import Model.Empleado;
import View.VistaClientes;
import View.VistaLogin;
import java.util.List;

public class LoginControl {
    private Carrito carrito = new Carrito();

    VistaClientes vistaClientes = new VistaClientes();
    VistaLogin login = new VistaLogin();
    ClientesDao clienteDao = new ClientesDao();
    EmpleadoDao empleadoDao = new EmpleadoDao();

    public void menu() {
        login.Inicio();
        int op;
        do {
            op = login.general();
            switch (op) {
                case 1 ->
                    Cliente();
                case 2 ->
                    Empleado();
                case 0 ->
                    System.out.println("Saliendo es saliendo");
                default ->
                    System.out.println("Opcion no encontrada");
            }
        } while (op != 3);
    }

    private void Cliente() {
        int op;
        do {
            op = vistaClientes.sesion();
            switch (op) {
                case 1 ->
                    registrarse();
                case 2 ->
                    sesionCliente();
                case 0 ->
                    System.out.println("Saliendo es saliendo");
                default ->
                    System.out.println("Opcion no encontrada");
            }
        } while (op != 0);

    }

    private void Empleado() {

        String correo = login.correo();
        String contraseña = login.contraseña();
        Empleado sesion = empleadoDao.login(correo, contraseña);

        if (sesion != null) {
            String cargo = sesion.getCargo().getEtiqueta();
            EmpleadosControl empleado = new EmpleadosControl(sesion);

            switch (cargo) {
                case "Vendedor" -> {
                    empleado.vededor();
                }
                case "Admin" -> {
                    empleado.administrador();
                }
                case "Bodega" -> {
                    empleado.bodega();
                }
                case "Gerente" -> {
                    empleado.gerente();
                }
                default ->
                    System.out.println("Cargo no encontrado");
            }

        } else {
            System.out.println("Credenciales incorrectas");
        }
    }

    protected void sesionCliente() {

        String correo = login.correo();
        String contraseña = login.contraseña();
        Cliente sesion = clienteDao.login(correo, contraseña);
        if (sesion != null) {

            TiendaControl cliente = new TiendaControl(sesion, carrito);
            cliente.menu();

        } else {
            System.out.println("Credenciales incorrectas");
        }
    }

    protected void registrarse() {
        Cliente cliente = vistaClientes.registrar();
        boolean filtro = clienteDao.listar().stream()
                .anyMatch(c -> c.getCorreo()
                .equalsIgnoreCase(cliente.getCorreo().trim()));
        if (filtro) {
            System.out.println("El correo ya se encuentra en uso");
        } else {
            clienteDao.registrar(cliente);
        }
    }
}
