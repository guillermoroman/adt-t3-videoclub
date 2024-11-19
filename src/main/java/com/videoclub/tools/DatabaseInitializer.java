package com.videoclub.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    private static final String URL = "jdbc:mariadb://localhost:3306/adt_t3_videoclub";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        crearTablaClientes();
    }

    private static void crearTablaClientes(){
       String sql = """
               CREATE TABLE IF NOT EXISTS clientes(
               id INT AUTO_INCREMENT PRIMARY KEY,
               nombre VARCHAR(100) NOT NULL,
               dni VARCHAR(20) UNIQUE NOT NULL,
               saldo DECIMAL(10,2) NOT NULL
               );
               """;

       try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
           Statement stmt = conn.createStatement()){
           stmt.execute(sql);
           System.out.println("Creada tabla clientes");

       } catch (SQLException e) {
           throw new RuntimeException(e);
       }

    }
    //

}
