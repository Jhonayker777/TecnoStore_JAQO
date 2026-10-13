package Dao;

import Model.Cliente;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ClientesDao {

    DB_SQL c = new DB_SQL();

    public void registrar(Cliente cliente) {

        try (Connection con = c.conexion()) {
            String sql = "call sp_crear_cliente(?,?,?,?,?,?)";
            CallableStatement cs = con.prepareCall(sql);
            cs.setString(1, cliente.getNombre());
            cs.setString(2, cliente.getIdentificacion());
            cs.setString(3, cliente.getCorreo());
            cs.setString(4, cliente.getTelefono());
            cs.setString(5, cliente.getContraseña());
            cs.registerOutParameter(6, Types.INTEGER);
            cs.execute();
            cliente.setId(cs.getInt(6));

            System.out.println("Cliente creado correctamente! xD");
        } catch (Exception e) {
            System.out.println("Error al registrar cliente : " + e.getMessage());
        }

    }

    public Cliente buscarId(long id) {
        try (Connection con = c.conexion()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("""
                                    SELECT p.id, p.nombre, p.identificacion,
                                    p.correo, p.telefono, c.fecha_registro, c.contraseña
                                    FROM clientes c INNER JOIN personas p
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
                            .fechaRegistro(rs.getDate(6))
                            .contraseña(rs.getString(7))
                            .build();
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar empleado:" + e.getMessage());
        }
        return null;
    }

    public Cliente buscarDocumento(String identificacion) {
        try (Connection con = c.conexion()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("""
                                    SELECT p.id, p.nombre, p.identificacion,
                                    p.correo, p.telefono, c.fecha_registro, c.contraseña
                                    FROM clientes c INNER JOIN personas p
                                    ON c.persona_id = p.id WHERE p.tipo = 'CLIENTE';
                                    """);
            while (rs.next()) {
                if (rs.getString(3).equals(identificacion.trim())) {
                    return new Cliente.Builder()
                            .id(rs.getInt(1))
                            .nombre(rs.getString(2))
                            .identificacion(rs.getString(3))
                            .correo(rs.getString(4))
                            .telefono(rs.getString(5))
                            .fechaRegistro(rs.getDate(6))
                            .contraseña(rs.getString(7))
                            .build();
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar empleado:" + e.getMessage());
        }
        return null;
    }
    
    public Cliente buscarCorreo(String correo) {
        try (Connection con = c.conexion()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("""
                                    SELECT p.id, p.nombre, p.identificacion,
                                    p.correo, p.telefono, c.fecha_registro, c.contraseña
                                    FROM clientes c INNER JOIN personas p
                                    ON c.persona_id = p.id WHERE p.tipo = 'CLIENTE';
                                    """);
            while (rs.next()) {
                if (rs.getString(4).equals(correo.trim())) {
                    return new Cliente.Builder()
                            .id(rs.getInt(1))
                            .nombre(rs.getString(2))
                            .identificacion(rs.getString(3))
                            .correo(rs.getString(4))
                            .telefono(rs.getString(5))
                            .fechaRegistro(rs.getDate(6))
                            .contraseña(rs.getString(7))
                            .build();
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar empleado:" + e.getMessage());
        }
        return null;
    }

    public List<Cliente> listar() {
        List<Cliente> lista = new ArrayList<>();
        try (Connection con = c.conexion()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("""
                                    SELECT p.id, p.nombre, p.identificacion,
                                    p.correo, p.telefono, c.fecha_registro, c.contraseña
                                    FROM clientes c INNER JOIN personas p
                                    ON c.persona_id = p.id WHERE p.tipo = 'CLIENTE';
                                    """);
            while (rs.next()) {
                lista.add(new Cliente.Builder()
                        .id(rs.getInt(1))
                        .nombre(rs.getString(2))
                        .identificacion(rs.getString(3))
                        .correo(rs.getString(4))
                        .telefono(rs.getString(5))
                        .fechaRegistro(rs.getDate(6))
                        .build());
            }
            return lista;
        } catch (SQLException e) {
            System.out.println("Error al listar clientes" + e);
        }
        return null;
    }

    public void actualizar(Cliente cliente) {
        try (Connection con = c.conexion()) {
            String sql = "call sp_actualizar_cliente(?,?,?,?,?);";
            CallableStatement cs = con.prepareCall(sql);
            cs.setLong(1, cliente.getId());
            cs.setString(2, cliente.getNombre());
            cs.setString(3, cliente.getIdentificacion());
            cs.setString(4, cliente.getCorreo());
            cs.setString(5, cliente.getTelefono());
            cs.execute();
            System.out.println("Cliente actualizado correctamente! xD");

        } catch (SQLException e) {
            System.out.println("Error al actualizar Cliente" + e);
        }
    }

    public Cliente login(String correo, String contraseña) {
        try (Connection con = c.conexion()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("""
                                           SELECT p.correo, c.contraseña, p.id FROM
                                           clientes c INNER JOIN personas p
                                           ON c.persona_id = p.id
                                           WHERE p.tipo = 'CLIENTE';
                                        """);
            while (rs.next()) {
                if (rs.getString(1).equalsIgnoreCase(correo) && rs.getString(2).equals(contraseña)) {
                    return buscarId(rs.getLong(3));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en el login" + e);
        }
        return null;
    }

    public void Contraseña(long id, String contraseña) {
        try (Connection con = c.conexion()) {
            String sql = "update clientes set contraseña=? where persona_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, contraseña);
            ps.setLong(2, id);
            ps.executeUpdate();
            System.out.println("Contraseña actualizada correctamente! xD");
        } catch (Exception e) {
            System.out.println("Error en contraseña: " + e);
        }
    }
}
