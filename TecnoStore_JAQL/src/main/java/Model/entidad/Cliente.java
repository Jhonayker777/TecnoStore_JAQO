package Model.entidad;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Cliente extends Persona {

    private int UMBRAL_FRECUENTE = 3;
    private Date fechaRegistro;
    private List<Venta> compras;
    private String contraseña;
    

    private Cliente(Builder b) {
        super(b.id, b.nombre, b.identificacion, b.correo, b.telefono);
        this.fechaRegistro = b.fechaRegistro;
        this.compras = b.compras;
        this.contraseña = b.contraseña;
    }

    @Override
    public String getRol() {
        return "CLIENTE";
    }

    public boolean esFrecuente() {
        return compras.size() >= UMBRAL_FRECUENTE;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public List<Venta> getCompras() {
        return compras;
    }

    public String getContraseña() {
        return contraseña;
    }

    public Cliente agregarCompra(Venta v) {
        return this.toBuilder().compras(nuevaCompra(v)).build();
    }

    private List<Venta> nuevaCompra(Venta v) {
        List<Venta> nueva = new ArrayList<>(this.compras);
        if (v != null) {
            nueva.add(v);
        }
        return nueva;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return new Builder()
                .id(this.getId())
                .nombre(this.getNombre())
                .identificacion(this.getIdentificacion())
                .correo(this.getCorreo())
                .telefono(this.getTelefono())
                .fechaRegistro(this.fechaRegistro)
                .compras(this.compras)
                .contraseña(this.contraseña);
    }

    public static class Builder {

        private int id;
        private String nombre;
        private String identificacion;
        private String correo;
        private String telefono;
        private Date fechaRegistro;
        private List<Venta> compras;
        private String contraseña;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder nombre(String n) {
            this.nombre = n;
            return this;
        }

        public Builder identificacion(String i) {
            this.identificacion = i;
            return this;
        }

        public Builder correo(String c) {
            this.correo = c;
            return this;
        }

        public Builder telefono(String t) {
            this.telefono = t;
            return this;
        }

        public Builder fechaRegistro(Date f) {
            this.fechaRegistro = f;
            return this;
        }

        public Builder compras(List<Venta> c) {
            this.compras = c;
            return this;
        }

        public Cliente build() {
            return new Cliente(this);
        }

        public Builder contraseña(String t) {
            this.contraseña = t;
            return this;
        }
    }

    @Override
    public String toString() {
        return super.toString()+"""
                                fecha_registro: %s
                                """.formatted(fechaRegistro); 
    }
    
    
}
