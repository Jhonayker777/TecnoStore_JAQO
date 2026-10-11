package Model.entidad;

public class Celular {

    private long id;
    private Marca marca;
    private String modelo;
    private SistemasOperativos sistemaOperativo;
    private CategoriaGama gama;
    private Precio precio;
    private int stock;
    private int stock_minimo;

    private Celular(Builder b) {
        this.id = b.id;
        this.marca = b.marca;
        this.modelo = b.modelo;
        this.sistemaOperativo = b.sistemaOperativo;
        this.gama = b.gama;
        this.precio = b.precio;
        this.stock = b.stock;
        this.stock_minimo =b.stock_minimo;
    }

    public boolean hayStock(int cantidad) {
        return cantidad > 0 && stock >= cantidad;
    }

    public Celular reducirStock(int cantidad) {
        if (!hayStock(cantidad)) {
            throw new IllegalStateException(
                    "Stock insuficiente para " + marca + " " + modelo
                    + " (disponible: " + stock + ")");
        }
        return this.toBuilder().stock(this.stock - cantidad).build();
    }

    public Celular aumentarStock(int cantidad) {
        return this.toBuilder().stock(this.stock + cantidad).build();
    }

    public long getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getStock_minimo() {
        return stock_minimo;
    }

    public Marca getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public SistemasOperativos getSistemaOperativo() {
        return sistemaOperativo;
    }

    public CategoriaGama getGama() {
        return gama;
    }

    public Precio getPrecio() {
        return precio;
    }

    public int getStock() {
        return stock;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return new Builder()
                .id(this.id)
                .marca(this.marca)
                .modelo(this.modelo)
                .sistemaOperativo(this.sistemaOperativo)
                .gama(this.gama)
                .precio(this.precio)
                .stock(this.stock)
                .stock_minimo(this.stock_minimo);
    }

    public static class Builder {

        private long id;
        private Marca marca;
        private String modelo;
        private SistemasOperativos sistemaOperativo;
        private CategoriaGama gama;
        private Precio precio;
        private int stock;
        private int stock_minimo;

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder marca(Marca marca) {
            this.marca = marca;
            return this;
        }

        public Builder modelo(String modelo) {
            this.modelo = modelo;
            return this;
        }

        public Builder sistemaOperativo(SistemasOperativos so) {
            this.sistemaOperativo = so;
            return this;
        }

        public Builder gama(CategoriaGama gama) {
            this.gama = gama;
            return this;
        }

        public Builder precio(Precio precio) {
            this.precio = precio;
            return this;
        }

        public Builder stock(int stock) {
            this.stock = stock;
            return this;
        }
        
        public Builder stock_minimo(int stock_minimo) {
            this.stock_minimo = stock_minimo;
            return this;
        }

        public Celular build() {
            this.modelo = modelo.trim();
            return new Celular(this);
        }
    }

    @Override
    public String toString() {
        return """
               id: %s
               Marca: %s
               Modelo: %s
               OS: %s
               Precio: %s
               Stock: %S
               """.formatted((id), marca, modelo, sistemaOperativo, precio, stock);
    }

}
