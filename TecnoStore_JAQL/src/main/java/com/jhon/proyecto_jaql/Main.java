package com.jhon.proyecto_jaql;

import Dao.EmpleadoDao;
import Model.entidad.Cargo;
import Model.entidad.Empleado;
import Model.entidad.Precio;
import java.sql.Date;

public class Main {

    public static void main(String[] args) {
        EmpleadoDao e = new EmpleadoDao();
        e.crear(Empleado.builder()
                .nombre("Lau")
                .identificacion("12365401")
                .correo("Lau@tecnoStore.com")
                .telefono("123456789")
                .cargo(Cargo.GERENTE)
                .salario(new Precio(1, "Vendedor", 123456))
                .fechaIngreso(Date.valueOf("2026-12-06"))
                .build());
    }

}
