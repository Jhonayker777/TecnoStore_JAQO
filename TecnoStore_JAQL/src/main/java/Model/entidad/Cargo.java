package Model.entidad;

public enum Cargo {
        VENDEDOR("Vendedor"),
        ADMIN("Admin"),
        BODEGA("Bodega"),
        GERENTE("Gerente");

        private final String etiqueta;

        Cargo(String etiqueta) {
            this.etiqueta = etiqueta;
        }

        public String getEtiqueta() {
            return etiqueta;
        }

        public static Cargo fromEtiqueta(String e) {
            for (Cargo c : values()) {
                if (c.etiqueta.equalsIgnoreCase(e)) {
                    return c;
                }
            }
            System.out.println("Cargo invalido");
            return null;
        }
    }