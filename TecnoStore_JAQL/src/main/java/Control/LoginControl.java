package Control;

import Dao.ClientesDao;
import Dao.EmpleadoDao;
import Model.Cliente;
import Model.Empleado;
import View.VistaLogin;

public class LoginControl {

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
                case 3 ->
                    System.out.println("Saliendo es saliendo");
                default ->
                    System.out.println("Opcion no encontrada");
            }
        } while (op != 3);
    }

    private void Cliente() {

        String correo = login.correo();
        String contraseña = login.contraseña();
        Cliente sesion = clienteDao.login(correo, contraseña);
        if (sesion != null) {

            ClientesControl cliente = new ClientesControl(sesion);
            cliente.menu();

        } else {
            System.out.println("Error al iniciar sesion");
        }
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
            System.out.println("Error al iniciar sesion");
        }
    }

}
