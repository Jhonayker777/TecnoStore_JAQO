package Dao;

import Model.SistemasOperativos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class SistemasOperativosDao {

    DB_SQL c = new DB_SQL();

    public void agregar(SistemasOperativos sistemasOperativos) {
        try (Connection con = c.conexion()) {
            String sql = "insert into sitemas_operativos (nombre) values (?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, sistemasOperativos.getNombre());
            ps.executeUpdate();
            System.out.println("Sistema operativo agregado correctamente! xD");
        } catch (Exception e) {
            System.out.println("Error al agregar un sistema operativo: " + e.getMessage());
        }
    }

    public SistemasOperativos buscar(long id) {
        try (Connection con = c.conexion()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from sistemas_operativos");
            while (rs.next()) {
                if (rs.getLong(1) == id) {
                    SistemasOperativos so = new SistemasOperativos(rs.getLong(1), rs.getString(2));
                    return so;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar el sistema operativo : " + e);
        }
        return null;
    }

    public List<SistemasOperativos> listar() {
        List<SistemasOperativos> lista = new ArrayList<>();
        try (Connection con = c.conexion()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from sistemas_operativos");
            while (rs.next()) {
                    lista.add(new SistemasOperativos(rs.getLong(1), rs.getString(2)));
                    return lista;
            }
            System.out.println("Se encontro el sistema operativo");
        } catch (SQLException e) {
            System.out.println("Error al buscar el sistema operativo : " + e);
        }
        return null;
    }

    public void actualizar(SistemasOperativos sistemasOperativos) {
        try (Connection con = c.conexion()) {
            String sql = "update sistemas_operativos set nombre=? where id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, sistemasOperativos.getNombre());
            ps.setLong(2, sistemasOperativos.getId());
            ps.executeUpdate();
            System.out.println("Producto actualizado correctamente! xD");
        } catch (Exception e) {
            System.out.println("Error en la actualizacion de precios: " + e);
        }
    }

    public void eliminar(long id) {
        try (Connection con = c.conexion()) {
            if (JOptionPane.showConfirmDialog(null, "¿Desea eliminar el sistema operativo?", null, JOptionPane.YES_NO_OPTION) == 0) {
                if (buscar(id) != null) {
                    String sql = "delete from sistemas_operativos where id=?";
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
