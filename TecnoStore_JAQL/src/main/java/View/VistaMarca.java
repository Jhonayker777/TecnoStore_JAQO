package View;

import Dao.MarcaDao;
import Model.Marca;
import View.validaciones.Decimal;
import View.validaciones.Entero;
import View.validaciones.Entrada;
import View.validaciones.Texto;
import java.util.List;
import java.util.Scanner;

public class VistaMarca {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Texto TEXTO = new Texto(SCANNER);
    private static final Entero ENTERO = new Entero(SCANNER);
    private static final Decimal DECIMAL = new Decimal(SCANNER);
    private static final Entrada entrada = new Entrada(TEXTO, ENTERO, DECIMAL);

    MarcaDao marcas = new MarcaDao();

    public Marca escogerMarca() {
        if (marcas.listar() != null) {
            listar(marcas.listar());
        }else{
            System.out.println("No hay marcas a escoger");
            return null;
        }
        int op = entrada.entero("ingrese el id de la marca a escoger");
        Marca marca = marcas.buscar(op);
        while (marca == null) {
            op = entrada.entero("Marca no encontrada, intente de nuevo");
            marca = marcas.buscar(op);
        }
        return marca;
    }

    public void listar(List<Marca> lista) {
        System.out.println("------------------Marcas----------------------");
        lista.forEach(System.out::println);
        System.out.println("----------------------------------------------");
    }
}
