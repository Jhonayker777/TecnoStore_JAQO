package View;

import Model.Cliente;
import View.validaciones.Decimal;
import View.validaciones.Entero;
import View.validaciones.Entrada;
import View.validaciones.Texto;
import java.util.List;
import java.util.Scanner;

public class VistaClientes {

    private static final Scanner SCANNER = new Scanner(System.in);

    private static final Texto TEXTO = new Texto(SCANNER);
    private static final Entero ENTERO = new Entero(SCANNER);
    private static final Decimal DECIMAL = new Decimal(SCANNER);

    private static final Entrada entrada = new Entrada(TEXTO, ENTERO, DECIMAL);

    public int principal() {

        return entrada.entero("""
                              
                              1. Registrar cliente
                              2. Actualizar cliente
                              4. Buscar por identificacion
                              5. Listar Todos
                              0. Volver
                              
                              """);

    }
    
    public int sesion() {

        return entrada.entero("""
                              
                              1. Registrarse
                              2. Iniciar sesion
                              0. Volver
                              
                              """);

    }

    public void mostrar(List<Cliente> lista) {
        System.out.println("------------------Clientes----------------------");
        lista.forEach(System.out::println);
        System.out.println("-------------------------------------------------");
    }

    protected String cambiarContraseña() {
        String newContraseña1 = entrada.texto("Ingrese una contraseña ");
        String newContraseña2 = entrada.texto("Ingrese nuevamente la contraseña");

        while (!newContraseña1.trim().equals(newContraseña2.trim())) {
            System.out.println("Las contraseñas no son iguales");
            newContraseña1 = entrada.texto("Ingrese la contraseña ");
            newContraseña2 = entrada.texto("Ingrese nuevamente la contraseña");
        }

        return newContraseña1;
    }

    public String contraseñaValida(String contraseñaSystem) {
        String contraseñaUser = entrada.texto("Ingrese Ingrese su antigua contraseña");
        if (contraseñaSystem.equals(contraseñaUser.trim())) {
            System.out.println("Contraseña validada");
            return cambiarContraseña();
        }
        return null;
    }

    public Cliente registrar() {

        return new Cliente.Builder()
                .nombre(entrada.texto("Ingrese su nombre"))
                .identificacion(entrada.texto("Ingrese su identificacion"))
                .correo(entrada.texto("Ingrese su correo"))
                .telefono(entrada.texto("Ingrese su telefono"))
                .contraseña(cambiarContraseña())
                .build();

    }

    public Cliente actualizar(long id) {
        return new Cliente.Builder()
                .id(id)
                .nombre(entrada.texto("Ingrese su nombre"))
                .identificacion(entrada.texto("Ingrese su identificacion"))
                .correo(entrada.texto("Ingrese su correo"))
                .telefono(entrada.texto("Ingrese su telefono"))
                .contraseña(cambiarContraseña())
                .build();

    }

}
