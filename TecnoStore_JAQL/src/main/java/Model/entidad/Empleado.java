package Model.entidad;

import java.sql.Date;

public class Empleado extends Persona {

    private Cargo cargo;
    private double salario;
    private Date fechaIngreso;
    private boolean activo;
    private String contraseña;

    private Empleado(Builder b) {
        super(b.id, b.nombre, b.identificacion, b.correo, b.telefono);
        this.cargo = b.cargo;
        this.salario = b.salario;
        this.fechaIngreso = b.fechaIngreso;
        this.activo = b.activo;
        this.contraseña = b.contraseña;

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

    public double getSalario() {
        return salario;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public boolean getActivo() {
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

    public String getContraseña() {
        return contraseña;
    }
    
    public String isActivo(){
        if(activo == true){
            return "activo";
        }
        return "inactivo";
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
                .activo(this.activo)
                .contraseña(this.contraseña);
    }

    public static class Builder {

        private int id;
        private String nombre;
        private String identificacion;
        private String correo;
        private String telefono;
        private Cargo cargo;
        private double salario;
        private Date fechaIngreso;
        private String contraseña;
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

        public Builder salario(double s) {
            this.salario = s;
            return this;
        }

        public Builder fechaIngreso(Date f) {
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

        public Builder contraseña(String c) {
            this.contraseña = c;
            return this;
        }
    }

    @Override
    public String toString() {
        return super.toString() +"""
                                 Cargo: %s
                                 Salario: %S
                                 Fecha_Ingreso: %S
                                 Estado: %s
                                 """.formatted(cargo.getEtiqueta(),salario,fechaIngreso,isActivo());
    }
}
