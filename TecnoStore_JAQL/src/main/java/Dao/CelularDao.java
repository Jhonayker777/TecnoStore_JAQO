package Dao;

import Model.entidad.Celular;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;

public class CelularDao {

    DB_SQL c = new DB_SQL();

    public void agregar(Celular celular) {

        try (Connection con = c.conexion()) {
            String sql = "call sp_crear_celular(?,?,?,?,?,?,?,?)";
            CallableStatement cs = con.prepareCall(sql);
            cs.setLong(1, celular.getMarca().getId());
            cs.setString(2, celular.getModelo());
            cs.setDouble(3, celular.getPrecio().getMonto());
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

}
