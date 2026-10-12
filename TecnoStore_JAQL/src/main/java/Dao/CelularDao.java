package Dao;

import Model.entidad.CategoriaGama;
import Model.entidad.Celular;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import javax.swing.JOptionPane;

public class CelularDao {

    DB_SQL c = new DB_SQL();
    MarcaDao marca = new MarcaDao();
    SistemasOperativosDao so = new SistemasOperativosDao();
    PreciosDao precio = new PreciosDao();

    public void agregar(Celular celular) {

        try (Connection con = c.conexion()) {
            String sql = "call sp_crear_celular(?,?,?,?,?,?,?,?)";
            CallableStatement cs = con.prepareCall(sql);
            cs.setLong(1, celular.getMarca().getId());
            cs.setString(2, celular.getModelo());
            cs.setDouble(3, celular.getPrecio().getId());
            cs.setLong(4, celular.getSistemaOperativo().getId());
            cs.setLong(5, celular.getGama().getId());
            cs.setDouble(6, celular.getStock());
            cs.setLong(7, celular.getStock_minimo());
            cs.registerOutParameter(8, Types.INTEGER);
            cs.execute();
            celular.setId(cs.getInt(8));

            System.out.println("Empleado creado correctamente! xD");
        } catch (Exception e) {
            System.out.println("Error al registrar celular: " + e.getMessage());

        }

    }

    public void actualizar(Celular celular) {
        try (Connection con = c.conexion()) {
            String sql = "call sp_actualizar_celular(?,?,?,?,?,?,?,?)";
            CallableStatement cs = con.prepareCall(sql);
            cs.setLong(1, celular.getId());
            cs.setLong(2, celular.getMarca().getId());
            cs.setString(3, celular.getModelo());
            cs.setDouble(4, celular.getPrecio().getId());
            cs.setLong(5, celular.getSistemaOperativo().getId());
            cs.setLong(6, celular.getGama().getId());
            cs.setDouble(7, celular.getStock());
            cs.setLong(8, celular.getStock_minimo());
            cs.execute();
        } catch (Exception e) {
        }
    }

    public Celular buscar(long id) {
        try (Connection con = c.conexion()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from celulares");
            while (rs.next()) {
                if (rs.getLong(1) == id) {
                    Celular celular = new Celular.Builder()
                            .id(rs.getLong(1))
                            .marca(marca.buscar(rs.getLong(2)))
                            .modelo(rs.getString(3))
                            .sistemaOperativo(so.buscar(rs.getLong(5)))
                            .gama(CategoriaGama.fromId(rs.getLong(6)))
                            .precio(precio.buscar(rs.getLong(4)))
                            .stock(rs.getInt(7))
                            .stock_minimo(rs.getInt(8))
                            .build();
                    return celular;
                }
            }
            System.out.println("Se encontro el sistema operativo");
        } catch (SQLException e) {
            System.out.println("Error al buscar el sistema operativo : " + e);
        }
        return null;
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
