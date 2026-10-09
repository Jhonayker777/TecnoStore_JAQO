package Dao;


import Model.entidad.Cliente;
import java.sql.CallableStatement;
import java.sql.Connection;
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
    
    
}
