package Model.entidad;

import java.time.LocalDate;

public class Empleado extends Persona {

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

    private final Cargo cargo;
    private  Precio salario;
    private  LocalDate fechaIngreso;
    private final boolean activo;

    private Empleado(Builder b) {
        super(b.id, b.nombre, b.identificacion, b.correo, b.telefono);
        this.cargo = b.cargo;
        this.salario = b.salario;
        this.fechaIngreso = b.fechaIngreso;
        this.activo = b.activo;
    }

    @Override
    public String getRol() {
        return "EMPLEADO";
    }

    public boolean puedeRegistrarVentas() {
        return activo && cargo == Cargo.VENDEDOR;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public Precio getSalario() {
        return salario;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public boolean isActivo() {
        return activo;
    }

    public Empleado desactivar() {
        return this.toBuilder().activo(false).build();
    }

    public Empleado activar() {
        return this.toBuilder().activo(true).build();
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
                .cargo(this.cargo)
                .salario(this.salario)
                .fechaIngreso(this.fechaIngreso)
                .activo(this.activo);
    }

    public static class Builder {

        private int id;
        private String nombre;
        private String identificacion;
        private String correo;
        private String telefono;
        private Cargo cargo;
        private Precio salario;
        private LocalDate fechaIngreso;
        private boolean activo = true;

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

        public Builder cargo(Cargo c) {
            this.cargo = c;
            return this;
        }

        public Builder salario(Precio s) {
            this.salario = s;
            return this;
        }

        public Builder fechaIngreso(LocalDate f) {
            this.fechaIngreso = f;
            return this;
        }

        public Builder activo(boolean a) {
            this.activo = a;
            return this;
        }

        public Empleado build() {
            return new Empleado(this);
        }
    }

    @Override
    public String toString() {
        return super.toString() + " | cargo: " + cargo + " | salario: " + salario
                + (activo ? "" : " [INACTIVO]");
    }
}

