
package Dao;

import Model.Celular;
import Model.CelularVenta;
import Model.Precio;
import Model.Venta;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class VentaDao {

    DB_SQL c = new DB_SQL();
    ClientesDao clienteDao = new ClientesDao();
    CelularDao celularDao = new CelularDao();

    
    public void registrar(Venta venta) {
        try (Connection con = c.conexion()) {
            String sql = "call sp_registrar_venta(?,?,?,?)";
            CallableStatement cs = con.prepareCall(sql);
            cs.setLong(1, venta.getCliente().getId());
            cs.setString(2, itemsToJson(venta.getItems()));
            cs.registerOutParameter(3, Types.INTEGER);
            cs.registerOutParameter(4, Types.DOUBLE);
            cs.execute();

            venta.setId(cs.getInt(3));
            venta.setTotal(cs.getDouble(4));

            System.out.println("Venta registrada correctamente! xD");
            System.out.println("Total: $" + venta.getTotal());

        } catch (Exception e) {
            System.out.println("Error al registrar venta: " + e.getMessage());
        }
    }

    public Venta buscar(long id) {
        try (Connection con = c.conexion()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from ventas");

            while (rs.next()) {
                if (rs.getLong("id") == id) {
                    Venta venta = new Venta();
                    venta.setId(rs.getInt("id"));
                    venta.setCliente(clienteDao.buscarId(rs.getLong("cliente_id")));

                    Timestamp ts = rs.getTimestamp("fecha");
                    if (ts != null) venta.setFecha(ts.toLocalDateTime());

                    venta.setTotal(rs.getDouble("total"));
                    venta.setItems(cargarItems(venta.getId()));
                    return venta;
                }
            }
            System.out.println("No se encontró la venta con id: " + id);

        } catch (SQLException e) {
            System.out.println("Error al buscar venta: " + e);
        }
        return null;
    }

   
    public List<Venta> listar() {
        List<Venta> lista = new ArrayList<>();

        try (Connection con = c.conexion()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from ventas order by fecha desc");

            while (rs.next()) {
                Venta venta = new Venta();
                venta.setId(rs.getInt("id"));
                venta.setCliente(clienteDao.buscarId(rs.getLong("cliente_id")));

                Timestamp ts = rs.getTimestamp("fecha");
                if (ts != null) venta.setFecha(ts.toLocalDateTime());

                venta.setTotal(rs.getDouble("total"));
                venta.setItems(cargarItems(venta.getId()));
                lista.add(venta);
            }
            return lista;

        } catch (SQLException e) {
            System.out.println("Error al listar ventas: " + e);
        }
        return lista;
    }
    
    public void anular(long id) {
        try (Connection con = c.conexion()) {
            if (JOptionPane.showConfirmDialog(null, "¿Desea anular la venta?",
                    null, JOptionPane.YES_NO_OPTION) == 0) {

                Venta venta = buscar(id);
                if (venta == null) {
                    System.out.println("Operación cancelada, el id no existe");
                    return;
                }

                // Llamar al procedure de anulación
                String sql = "call sp_anular_venta(?)";
                CallableStatement cs = con.prepareCall(sql);
                cs.setLong(1, id);
                cs.execute();

                System.out.println("Venta anulada correctamente! xD");
            } else {
                System.out.println("Operación cancelada correctamente!");
            }
        } catch (Exception e) {
            System.out.println("Error al anular la venta: " + e.getMessage());
        }
    }

    private List<CelularVenta> cargarItems(long ventaId) {
        List<CelularVenta> items = new ArrayList<>();

        try (Connection con = c.conexion()) {
            String sql = "select * from detalle_ventas where venta_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, ventaId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                CelularVenta item = new CelularVenta();
                item.setId(rs.getInt("id"));
                item.setCantidad(rs.getInt("cantidad"));

                Precio precio = new Precio();
                precio.setMonto(rs.getDouble("precio_unitario"));
                item.setPrecioUnitario(precio);

                Celular celular = celularDao.buscar(rs.getLong("celular_id"));
                item.setCelular(celular);

                items.add(item);
            }

        } catch (SQLException e) {
            System.out.println("Error al cargar items: " + e);
        }
        return items;
    }

    private String itemsToJson(List<CelularVenta> items) {
        StringBuilder sb = new StringBuilder("[");

        for (int i = 0; i < items.size(); i++) {
            CelularVenta item = items.get(i);

            sb.append("{")
                    .append("\"celular_id\":").append(item.getCelular().getId()).append(",")
                    .append("\"cantidad\":").append(item.getCantidad())
                    .append("}");

            if (i < items.size() - 1) {
                sb.append(",");
            }
        }

        sb.append("]");
        return sb.toString();
    }
}