package Model.Productos;

public class Celular {

    private long id;
    private Marca marca;
    private String modelo;
    private SistemasOperativos sistemasOperativos;
    private CategoriaGama gama;
    private Precio precio;

    public Celular(long id, Marca marca, String modelo, SistemasOperativos sistemasOperativos, CategoriaGama gama, Precio precio) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.sistemasOperativos = sistemasOperativos;
        this.gama = gama;
        this.precio = precio;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public SistemasOperativos getSistemasOperativos() {
        return sistemasOperativos;
    }

    public void setSistemasOperativos(SistemasOperativos sistemasOperativos) {
        this.sistemasOperativos = sistemasOperativos;
    }

    public CategoriaGama getGama() {
        return gama;
    }

    public void setGama(CategoriaGama gama) {
        this.gama = gama;
    }

    public Precio getPrecio() {
        return precio;
    }

    public void setPrecio(Precio precio) {
        this.precio = precio;
    }

}
