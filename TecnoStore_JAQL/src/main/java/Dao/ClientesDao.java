package Dao;


import Model.entidad.Cargo;
import Model.entidad.Cliente;
import Model.entidad.Empleado;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

public class ClientesDao {

    public void registrar(Cliente cliente) {

        DB_SQL c = new DB_SQL();

        try (Connection con = c.conexion()) {
            String sql = "call sp_crear_cliente(?,?,?,?,?)";
            CallableStatement ps = con.prepareCall(sql);
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getIdentificacion());
            ps.setString(3, cliente.getCorreo());
            ps.setString(4, cliente.getTelefono());
            ps.setString(5, cliente.getContraseña());
            ps.registerOutParameter(6, Types.INTEGER);
            ps.execute();
            cliente.setId(ps.getInt(6));

            System.out.println("Cliente creado correctamente! xD");
        } catch (Exception e) {
            System.out.println("Error al registrar cliente : " + e.getMessage());
        }

    }
    
    public Cliente buscar(long id) {
        try (Connection con = c.conexion()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("""
                                    SELECT p.id, p.nombre, p.identificacion,
                                    p.correo, p.telefono, c.fecha_registro, c.contraseña
                                    FROM clientes c INNER JOIN empleados e
                                    ON c.persona_id = p.id WHERE p.tipo = 'CLIENTE';
                                    """);
            while (rs.next()) {
                if (rs.getLong(1) == id) {
                    return new Cliente.Builder()
                            .id(rs.getInt(1))
                            .nombre(rs.getString(2))
                            .identificacion(rs.getString(3))
                            .correo(rs.getString(4))
                            .telefono(rs.getString(5))
                            .build();
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar empleado:" + e.getMessage());
        }
        return null;
    }
}
