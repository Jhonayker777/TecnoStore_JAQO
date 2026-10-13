package Dao;

import Model.Precio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class PreciosDao {

    DB_SQL c = new DB_SQL();

    public void agregar(Precio precio) {
        try (Connection con = c.conexion()) {
            String sql = "insert into precios (nombre, monto) values (?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, precio.getNombre());
            ps.setDouble(2, precio.getMonto());
            ps.executeUpdate();
            System.out.println("Precio creado correctamente! xD");
        } catch (Exception e) {
            System.out.println("Error en el ingreso de precios: " + e.getMessage());
        }
    }

    public Precio buscar(long id) {
        try (Connection con = c.conexion()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from precios");
            while (rs.next()) {
                if (rs.getLong(1) == id) {
                    Precio p = new Precio(rs.getLong(1), rs.getString(2), rs.getDouble(3));
                    return p;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar precio: " + e);
        }
        return null;
    }

    public List<Precio> listar() {
        List<Precio> lista = new ArrayList<>();

        try (Connection con = c.conexion()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from precios");
            while (rs.next()) {
                lista.add(new Precio(rs.getLong(1), rs.getString(2), rs.getDouble(3)));
                return lista;
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar precio: " + e);
        }
        return null;
    }

    public void actualizar(Precio precio) {
        try (Connection con = c.conexion()) {
            String sql = "update precios set nombre=?, monto=? where id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, precio.getNombre());
            ps.setDouble(2, precio.getMonto());
            ps.setLong(3, precio.getId());
            ps.executeUpdate();
            System.out.println("Producto actualizado correctamente! xD");
        } catch (Exception e) {
            System.out.println("Error en la actualizacion de precios: " + e);
        }
    }

    public void eliminar(long id) {
        try (Connection con = c.conexion()) {
            if (JOptionPane.showConfirmDialog(null, "¿Desea eliminar el precio?", null, JOptionPane.YES_NO_OPTION) == 0) {
                if (buscar(id) != null) {
                    String sql = "delete from precios where id=?";
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setLong(1, id);
                    ps.executeUpdate();
                    System.out.println("Producto eliminado correctamente! xD");
                } else {
                    System.out.println("Operacion cancelada, el id no existe");
                }
            } else {
                System.out.println("Operacion cancelada correctamente!");
            }
        } catch (Exception e) {
            System.out.println("Error en la eliminacion de precios: " + e.getMessage());
        }
    }
}
