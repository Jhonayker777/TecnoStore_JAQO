package View;

import Model.CategoriaGama;
import Model.Celular;
import Model.Marca;
import Model.Precio;
import Model.SistemasOperativos;
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

    private VistaMarca vistaMarca = new VistaMarca();
    private VistaPrecio vistaPrecio = new VistaPrecio();
    private VistaSistemaOperativo vistaSO = new VistaSistemaOperativo();
    private VistaCategoriaGama vistaGama = new VistaCategoriaGama();

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
        System.out.println("------------------Celulares----------------------");
        lista.forEach(System.out::println);
        System.out.println("-------------------------------------------------");
    }

    public void FiltrarNombre(List<Celular> lista) {
        String nombre = entrada.texto("Ingrese el nombre a buscar");
        List<Celular> list = lista.stream()
                .filter(c -> c.getModelo()
                .toLowerCase()
                .contains(nombre.toLowerCase().trim()))
                .toList();
        if (!list.isEmpty()) {
            mostrar(list);
        } else {
            System.out.println("No se encontraron coincidencias por: " + nombre);
        }
    }

    public void filtrarGama(List<Celular> lista) {
        CategoriaGama gama = vistaGama.escogergama();
        List<Celular> list = lista.stream()
                .filter(c -> c.getGama().getId() == gama.getId())
                .toList();
        if (!list.isEmpty()) {
            mostrar(list);
        } else {
            System.out.println("No se encontraron coincidencias por gama: " + gama.getEtiqueta());
        }
    }

    public void filtrarMarca(List<Celular> lista) {
        Marca marca = vistaMarca.escogerMarca();
        List<Celular> list = lista.stream()
                .filter(c -> c.getMarca().getId() == marca.getId())
                .toList();
        if (!list.isEmpty()) {
            mostrar(list);
        } else {
            System.out.println("No se encontraron coincidencias por: " + marca.getNombre());
        }
    }

    public Celular ingresarDatos() {
        Marca marquiña = vistaMarca.escogerMarca();
        Precio p = vistaPrecio.escogerPrecio();
        SistemasOperativos so = vistaSO.escogerSistemaOperativo();
        CategoriaGama gama = vistaGama.escogergama();

        if (marquiña == null || p == null || so == null) {
            System.out.println("No se pudo registrar el Celular");
            return null;
        }

        return Celular.builder()
                .marca(marquiña)
                .modelo(entrada.texto("Ingrese el modelo"))
                .sistemaOperativo(so)
                .gama(gama)
                .precio(p)
                .stock(entrada.entero("Ingrese el stock"))
                .stock_minimo(entrada.entero("Ingrese el stock minimo"))
                .build();
    }

    public Celular actualizarDatos(long id) {
        Marca marquiña = vistaMarca.escogerMarca();
        Precio p = vistaPrecio.escogerPrecio();
        SistemasOperativos so = vistaSO.escogerSistemaOperativo();
        CategoriaGama gama = vistaGama.escogergama();

        if (marquiña == null || p == null || so == null) {
            System.out.println("No se pudo registrar el Celular");
            return null;
        }

        return Celular.builder()
                .id(id)
                .marca(marquiña)
                .modelo(entrada.texto("Ingrese el modelo"))
                .sistemaOperativo(so)
                .gama(gama)
                .precio(p)
                .stock(entrada.entero("Ingrese el stock"))
                .stock_minimo(entrada.entero("Ingrese el stock minimo"))
                .build();
    }

}
