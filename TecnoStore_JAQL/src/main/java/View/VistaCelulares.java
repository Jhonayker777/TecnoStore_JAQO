package View;

import Control.MarcaControl;
import Control.PrecioControl;
import Control.SistemaOperativoControl;
import Dao.MarcaDao;
import Dao.PreciosDao;
import Dao.SistemasOperativosDao;
import Model.CategoriaGama;
import Model.Celular;
import View.validaciones.Decimal;
import View.validaciones.Entero;
import View.validaciones.Entrada;
import View.validaciones.Texto;
import java.util.List;
import java.util.Scanner;

public class VistaCelulares {

    private static final Scanner SCANNER = new Scanner(System.in);

    private static final Texto TEXTO = new Texto(SCANNER);
    private static final Entero ENTERO = new Entero(SCANNER);
    private static final Decimal DECIMAL = new Decimal(SCANNER);

    private static final Entrada entrada = new Entrada(TEXTO, ENTERO, DECIMAL);
    
    MarcaControl marcaControl = new MarcaControl();
    SistemaOperativoControl soControl = new SistemaOperativoControl();
    PrecioControl precioControl = new PrecioControl();

    public int principal() {

        return entrada.entero("""
                              1. Registrar nuevo celular
                              2. Actualizar celular
                              3. Eliminar celular
                              4. Listar todos
                              5. Buscar por id
                              0. Volver
                              """);
    }

    public void mostrar(List<Celular> lista) {
        for (Celular celular : lista) {
            System.out.println(celular);
        }
    }

    public Celular ingresarDatos() {
        return Celular.builder()
                .marca(marcaControl.escogerMarca())
                .modelo(entrada.texto("Ingrese el modelo"))
                .sistemaOperativo(soControl.escogerSistemaOperativo())
                .gama(CategoriaGama.fromEtiqueta(entrada.texto("Escoja la gama : ALTA, MEDIA, BAJA")))
                .precio(precioControl.escogerPrecio())
                .stock(entrada.entero("Ingrese el stock"))
                .stock_minimo(entrada.entero("Ingrese el stock minimo"))
                .build();
    }
    
    public Celular actualizarDatos(long id) {
        return Celular.builder()
                .id(id)
                .marca(marcaControl.escogerMarca())
                .modelo(entrada.texto("Ingrese el modelo"))
                .sistemaOperativo(soControl.escogerSistemaOperativo())
                .gama(CategoriaGama.fromEtiqueta(entrada.texto("Escoja la gama : ALTA, MEDIA, BAJA")))
                .precio(precioControl.escogerPrecio())
                .stock(entrada.entero("Ingrese el stock"))
                .stock_minimo(entrada.entero("Ingrese el stock minimo"))
                .build();
    }

}
