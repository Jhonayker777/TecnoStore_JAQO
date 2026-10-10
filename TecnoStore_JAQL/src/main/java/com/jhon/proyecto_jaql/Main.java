package com.jhon.proyecto_jaql;

import Dao.EmpleadoDao;
import Model.entidad.Cargo;
import Model.entidad.Empleado;
import java.sql.Date;

public class Main {

    public static void main(String[] args) {
        EmpleadoDao e = new EmpleadoDao();
        
        Empleado em = Empleado.builder()
                .nombre("Lau")
                .identificacion("12365401")
                .correo("Lau@tecnoStore.com")
                .telefono("123456789")
                .cargo(Cargo.GERENTE)
                .salario(123456)
                .fechaIngreso(Date.valueOf("2025-12-06"))
                .contraseña("1234567")
                .build();
        
        
    }

}
