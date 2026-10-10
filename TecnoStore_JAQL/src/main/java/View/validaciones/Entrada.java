package View.validaciones;

public class Entrada {

    private final Texto texto;
    private final Entero entero;
    private final Decimal decimal;

    public Entrada(Texto texto, Entero entero, Decimal decimal) {
        this.texto = texto;
        this.entero = entero;
        this.decimal = decimal;
    }


    public String texto(String mensaje) {
        return texto.texto(mensaje);
    }

    public int entero(String mensaje) {
        return entero.entero(mensaje);
    }

    public long enteroGrande(String mensaje) {
        return entero.enteroGrande(mensaje);
    }

    public double decimal(String mensaje) {
        return decimal.decimal(mensaje);
    }
}