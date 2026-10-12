package Control;

import Dao.ClientesDao;
import Dao.EmpleadoDao;
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
        
        if (clienteDao.login(correo, contraseña)) {
            System.out.println("Secion iniciada correctamente");
        } else {
            System.out.println("Error al iniciar sesion");
        }
    }

    private void Empleado() {
        String correo = login.correo();
        String contraseña = login.contraseña();
        
        if (empleadoDao.login(correo, contraseña)) {
            System.out.println("Secion iniciada correctamente");
        } else {
            System.out.println("Error al iniciar sesion");
        }
    }

}
