package Dao;

import Model.entidad.Empleado;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;

public class EmpleadoDao {

    DB_SQL c = new DB_SQL();

    public void crear(Empleado empleado) {

        try (Connection con = c.conexion()) {
            String sql = "call sp_crear_empleado(?,?,?,?,?,?,?,?)";
            CallableStatement ps = con.prepareCall(sql);
            ps.setString(1, empleado.getNombre());
            ps.setString(2, empleado.getIdentificacion());
            ps.setString(3, empleado.getCorreo());
            ps.setString(4, empleado.getTelefono());
            ps.setString(5, empleado.getCargo().getEtiqueta());
            ps.setDouble(6, empleado.getSalario().getMonto());
            ps.setDate(7, empleado.getFechaIngreso());
            ps.setString(8, empleado.getContraseña());
            ps.registerOutParameter(9, Types.INTEGER);
            ps.execute();
            empleado.setId(ps.getInt(9));

            System.out.println("Empleado creado correctamente! xD");
        } catch (Exception e) {
            System.out.println("Error en el ingreso de datos: " + e.getMessage());

        }

    }

    public void actualizar(Empleado empleado) {
        try (Connection con = c.conexion()) {
            String sql = "update empleados set nombre=?, cargo=?, salario=?, activo=?, contraseña=? where id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, empleado.getNombre());
            ps.setString(2, empleado.getCargo().getEtiqueta());
            ps.setDouble(3, empleado.getSalario().getMonto());
            ps.setBoolean(4, empleado.isActivo());
            ps.setString(5, empleado.getContraseña());
            ps.setLong(5, empleado.getId());

            ps.executeUpdate();
            System.out.println("Empleado actualizado correctamente! xD");
        } catch (Exception e) {
            System.out.println("Error en el ingreso de datos: " + e.getMessage());
        }
    }

}
