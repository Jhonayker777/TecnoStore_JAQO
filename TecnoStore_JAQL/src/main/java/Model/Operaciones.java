package Model;


public final class Operaciones {


    private Operaciones() {}

    // ============================================================
    // SUMA
    // ============================================================
    public static Precio sumar(Precio a, Precio b) {
        if (a == null || b == null) return null;
        return new Precio(0, "suma", a.getMonto() + b.getMonto());
    }

    /** Suma segura: si uno es null, devuelve el otro. */
    public static Precio sumarSeguro(Precio a, Precio b) {
        if (a == null) return b;
        if (b == null) return a;
        return new Precio(0, "suma", a.getMonto() + b.getMonto());
    }

    // ============================================================
    // RESTA
    // ============================================================
    public static Precio restar(Precio a, Precio b) {
        if (a == null || b == null) return null;
        double r = a.getMonto() - b.getMonto();
        if (r < 0) return null;
        return new Precio(0, "resta", r);
    }

    // ============================================================
    // MULTIPLICACIÓN
    // ============================================================
    public static Precio multiplicar(Precio p, int factor) {
        if (p == null || factor < 0) return null;
        return new Precio(0, "multiplicacion", p.getMonto() * factor);
    }

    public static Precio multiplicar(Precio p, double factor) {
        if (p == null || factor < 0) return null;
        return new Precio(0, "multiplicacion", p.getMonto() * factor);
    }

    // ============================================================
    // DIVISIÓN
    // ============================================================
    public static Precio dividir(Precio p, int divisor) {
        if (p == null || divisor <= 0) return null;
        return new Precio(0, "division", p.getMonto() / divisor);
    }

    public static Precio dividir(Precio p, double divisor) {
        if (p == null || divisor <= 0) return null;
        return new Precio(0, "division", p.getMonto() / divisor);
    }

    // ============================================================
    // PORCENTAJE
    // ============================================================

    /** Calcula el x% del monto. */
    public static Precio porcentaje(Precio p, double porcentaje) {
        if (p == null || porcentaje < 0) return null;
        return new Precio(0, "porcentaje", p.getMonto() * (porcentaje / 100.0));
    }

    /** Aumenta un porcentaje. Ej: 10 → monto + 10%. */
    public static Precio aumentarPorcentaje(Precio p, double porcentaje) {
        if (p == null || porcentaje < 0) return null;
        return new Precio(0, "aumento", p.getMonto() * (1.0 + porcentaje / 100.0));
    }

    /** Disminuye un porcentaje. Ej: 10 → monto - 10%. */
    public static Precio disminuirPorcentaje(Precio p, double porcentaje) {
        if (p == null || porcentaje < 0 || porcentaje > 100) return null;
        return new Precio(0, "disminucion", p.getMonto() * (1.0 - porcentaje / 100.0));
    }

    

    /** Aplica IVA. Ej: 0.19 → monto * 1.19. */
    public static Precio aplicarIva(Precio p, double tasa) {
        if (p == null || tasa < 0) return null;
        return new Precio(0, "iva", p.getMonto() * (1.0 + tasa));
    }

    /** Quita IVA. Ej: 0.19 → monto / 1.19. */
    public static Precio quitarIva(Precio p, double tasa) {
        if (p == null || tasa < 0) return null;
        return new Precio(0, "sin_iva", p.getMonto() / (1.0 + tasa));
    }

    /** Calcula solo el IVA. Ej: 0.19 → monto * 0.19. */
    public static Precio calcularIva(Precio p, double tasa) {
        if (p == null || tasa < 0) return null;
        return new Precio(0, "iva_calculado", p.getMonto() * tasa);
    }

    

    /** Aplica descuento. Ej: 10 → monto - 10%. */
    public static Precio aplicarDescuento(Precio p, double porcentaje) {
        if (p == null || porcentaje < 0 || porcentaje > 100) return null;
        double descuento = p.getMonto() * (porcentaje / 100.0);
        return new Precio(0, "descuento", p.getMonto() - descuento);
    }

    /** Calcula solo el monto del descuento. */
    public static Precio calcularDescuento(Precio p, double porcentaje) {
        if (p == null || porcentaje < 0 || porcentaje > 100) return null;
        return new Precio(0, "descuento_calculado", p.getMonto() * (porcentaje / 100.0));
    }

  
}