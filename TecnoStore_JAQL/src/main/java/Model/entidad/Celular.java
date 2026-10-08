package Model.entidad;

public class Celular {

    private final int id;
    private final Marca marca;
    private final String modelo;
    private final SistemasOperativos sistemaOperativo;
    private final CategoriaGama gama;
    private final Precio precio;
    private final int stock;

    private Celular(Builder b) {
        this.id = b.id;
        this.marca = b.marca;
        this.modelo = b.modelo;
        this.sistemaOperativo = b.sistemaOperativo;
        this.gama = b.gama;
        this.precio = b.precio;
        this.stock = b.stock;
    }

    // ---------- Reglas de negocio ----------
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

    public boolean esGamaAlta() {
        return gama == CategoriaGama.ALTA;
    }

    public int getId() {
        return id;
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
                .stock(this.stock);
    }

    public static class Builder {

        private int id;
        private Marca marca;
        private String modelo;
        private SistemasOperativos sistemaOperativo;
        private CategoriaGama gama;
        private Precio precio;
        private int stock;

        public Builder id(int id) {
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

        public Celular build() {
            this.modelo = modelo.trim();
            return new Celular(this);
        }
    }

}
