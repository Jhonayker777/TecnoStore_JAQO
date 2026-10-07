package Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB_SQL {
     public Connection conexion() {
        Connection c = null;
        try {
            c = DriverManager.getConnection("jdbc:mysql://localhost:3307/TecnoStore_JAQO", "root", "Incorrecta1.");
            System.out.println("DB CONNECTED");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return c;
    }
}
