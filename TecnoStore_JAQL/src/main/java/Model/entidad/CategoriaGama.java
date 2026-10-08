package Model.entidad;

public enum CategoriaGama {
    ALTA("Alta"),
    MEDIA("Media"),
    BAJA("Baja");

    private String etiqueta;

    CategoriaGama(String etiqueta) {
        this.etiqueta = etiqueta;
    }
    
    public String getEtiqueta() {
        return etiqueta;
    }
    
    public static CategoriaGama fromEtiqueta(String etiqueta) {
        for (CategoriaGama g : values()) {
            if (g.etiqueta.equalsIgnoreCase(etiqueta)) return g;
        }
        return null;
    }
}
