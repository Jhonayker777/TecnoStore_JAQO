package Model;

public enum CategoriaGama {
    ALTA(1, "Alta"),
    MEDIA(2, "Media"),
    BAJA(3, "Baja");

    private String etiqueta;
    private long id;

    CategoriaGama(long id, String etiqueta) {
        this.etiqueta = etiqueta;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public static CategoriaGama fromEtiqueta(String etiqueta) {
        for (CategoriaGama g : values()) {
            if (g.etiqueta.equalsIgnoreCase(etiqueta)) {
                return g;
            }
        }
        return null;
    }

    public static CategoriaGama fromId(long id) {
        for (CategoriaGama g : values()) {
            if (g.id == id) {
                return g;
            }
        }
        return null;
    }

}
