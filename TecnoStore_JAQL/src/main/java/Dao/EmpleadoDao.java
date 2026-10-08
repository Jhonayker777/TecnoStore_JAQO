package Dao;

import Model.entidad.Empleado;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class EmpleadoDao {

    DB_SQL c = new DB_SQL();

    public void crear(Empleado empleado) {
        try (Connection con = c.conexion()) {
            String sql = "call sp_crear_empleado(?,?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, empleado.getNombre());
            ps.setString(2, empleado.getIdentificacion());
            ps.setString(3, empleado.getCorreo());
            ps.setString(4, empleado.getTelefono());
            ps.setString(5, empleado.getCargo().getEtiqueta());
            ps.setDouble(6, empleado.getSalario().getMonto());
            ps.setDate(7, empleado.getFechaIngreso());
            ps.executeUpdate();
            System.out.println("Empleado creado correctamente! xD");
        } catch (Exception e) {
            System.out.println("Error en el ingreso de datos: " + e.getMessage());
        }
    }
}
