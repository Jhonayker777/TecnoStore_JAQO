package Dao;

import Model.entidad.Cargo;
import Model.entidad.Empleado;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

public class EmpleadoDao {

    DB_SQL c = new DB_SQL();

    public void registrar(Empleado empleado) {

        try (Connection con = c.conexion()) {
            String sql = "call sp_crear_empleado(?,?,?,?,?,?,?,?)";
            CallableStatement ps = con.prepareCall(sql);
            ps.setString(1, empleado.getNombre());
            ps.setString(2, empleado.getIdentificacion());
            ps.setString(3, empleado.getCorreo());
            ps.setString(4, empleado.getTelefono());
            ps.setString(5, empleado.getCargo().getEtiqueta());
            ps.setDouble(6, empleado.getSalario());
            ps.setDate(7, empleado.getFechaIngreso());
            ps.setString(8, empleado.getContraseña());
            ps.registerOutParameter(9, Types.INTEGER);
            ps.execute();
            empleado.setId(ps.getInt(9));

            System.out.println("Empleado creado correctamente! xD");
        } catch (Exception e) {
            System.out.println("Error al regegistrar empleado: " + e.getMessage());

        }

    }
    
    public void activar(long id){
        try(Connection con = c.conexion()) {
            String sql = "update empleados set activo=1 where id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al activar empleado: " + e.getMessage());
        }
    }

    public void desactivar(long id){
        try(Connection con = c.conexion()) {
            String sql = "update empleados set activo=0 where id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al desacactivar empleado: " + e.getMessage());
        }
    }
    
    public void actualizar(Empleado empleado) {
        try (Connection con = c.conexion()) {
            String sql = "update empleados set nombre=?, cargo=?, salario=?, activo=?, contraseña=? where id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, empleado.getNombre());
            ps.setString(2, empleado.getCargo().getEtiqueta());
            ps.setDouble(3, empleado.getSalario());
            ps.setBoolean(4, empleado.getActivo());
            ps.setString(5, empleado.getContraseña());
            ps.setLong(6, empleado.getId());

            ps.executeUpdate();
            System.out.println("Empleado actualizado correctamente! xD");
        } catch (Exception e) {
            System.out.println("Error en el ingreso de datos: " + e.getMessage());
        }
    }

   
    
    public Empleado buscar(long id) {
        try (Connection con = c.conexion()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("""
                                    SELECT p.id, p.nombre, p.identificacion,
                                    p.correo, p.telefono, e.cargo, e.salario,
                                    e.fecha_ingreso, e.activo
                                    FROM personas p INNER JOIN empleados e
                                    ON e.persona_id = p.id WHERE p.tipo = 'EMPLEADO';
                                    """);
            while (rs.next()) {
                if (rs.getLong(1) == id) {
                    return new Empleado.Builder()
                            .id(rs.getInt(1))
                            .nombre(rs.getString(2))
                            .identificacion(rs.getString(3))
                            .correo(rs.getString(4))
                            .telefono(rs.getString(5))
                            .cargo(Cargo.fromEtiqueta(rs.getString(6)))
                            .salario(rs.getDouble(7))
                            .fechaIngreso(rs.getDate(8))
                            .activo(rs.getBoolean(9))
                            .build();
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar empleado:"+ e.getMessage());
        }
        return null;
    }
}
