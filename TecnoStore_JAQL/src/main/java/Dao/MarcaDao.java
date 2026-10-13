package Dao;

import Model.Marca;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class MarcaDao {

    DB_SQL c = new DB_SQL();

    public void agregar(Marca marca) {
        try (Connection con = c.conexion()) {
            String sql = "insert into marcas (nombre) values (?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, marca.getNombre());
            ps.executeUpdate();
            System.out.println("Sistema operativo agregado correctamente! xD");
        } catch (Exception e) {
            System.out.println("Error al agregar un sistema operativo: " + e.getMessage());
        }
    }

    public Marca buscar(long id) {
        try (Connection con = c.conexion()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from marcas");
            while (rs.next()) {
                if (rs.getLong(1) == id) {
                    Marca m = new Marca(rs.getLong(1), rs.getString(2));
                    return m;
                }
            }
            System.out.println("Se encontro el sistema operativo");
        } catch (SQLException e) {
            System.out.println("Error al buscar el sistema operativo : " + e);
        }
        return null;
    }

    public List<Marca> listar() {
        try (Connection con = c.conexion()) {
            List<Marca> lista = new ArrayList<>();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from marcas");
            while (rs.next()) {
                lista.add(new Marca(rs.getLong(1), rs.getString(2)));
            }
            return lista;
        } catch (SQLException e) {
            System.out.println("Error al listar marcas " + e);
        }
        return null;
    }

    public void actualizar(Marca marca) {
        try (Connection con = c.conexion()) {
            String sql = "update marcas set nombre=? where id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, marca.getNombre());
            ps.setLong(2, marca.getId());
            ps.executeUpdate();
            System.out.println("Producto actualizado correctamente! xD");
        } catch (Exception e) {
            System.out.println("Error en la actualizacion de precios: " + e);
        }
    }

    public void eliminar(long id) {
        try (Connection con = c.conexion()) {
            if (JOptionPane.showConfirmDialog(null, "¿Desea eliminar la marca?", null, JOptionPane.YES_NO_OPTION) == 0) {
                if (buscar(id) != null) {
                    String sql = "delete from marcas where id=?";
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
