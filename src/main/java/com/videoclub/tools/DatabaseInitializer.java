package com.videoclub.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    private static final String url = "jdbc:mariadb://localhost:3306/adt_t3_videoclub";
    private static final String user = "root";
    private static final String password = "";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            // Crear tabla clientes
            stmt.execute("CREATE TABLE IF NOT EXISTS clientes (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "nombre VARCHAR(100) NOT NULL, " +
                    "dni VARCHAR(20) UNIQUE NOT NULL, " +
                    "saldo DECIMAL(10, 2) NOT NULL)");

            // Crear tabla peliculas con el campo disponible por defecto TRUE
            stmt.execute("CREATE TABLE IF NOT EXISTS peliculas (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "titulo VARCHAR(100) NOT NULL, " +
                    "director VARCHAR(100) NOT NULL, " +
                    "anio INT NOT NULL, " +
                    "genero VARCHAR(50) NOT NULL, " +
                    "precio DECIMAL(5, 2) NOT NULL, " +
                    "disponible BOOLEAN DEFAULT TRUE)");

            // Crear tabla prestamos
            stmt.execute("CREATE TABLE IF NOT EXISTS prestamos (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "cliente_id INT NOT NULL, " +
                    "pelicula_id INT NOT NULL, " +
                    "fecha_prestamo DATE NOT NULL DEFAULT CURRENT_DATE, " +
                    "fecha_devolucion DATE DEFAULT NULL, " +
                    "FOREIGN KEY (cliente_id) REFERENCES clientes(id), " +
                    "FOREIGN KEY (pelicula_id) REFERENCES peliculas(id))");

            System.out.println("Base de datos inicializada correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void crearTablaClientes() {
        String sql = """
            CREATE TABLE IF NOT EXISTS clientes (
                id INT AUTO_INCREMENT PRIMARY KEY,
                nombre VARCHAR(100) NOT NULL,
                dni VARCHAR(20) UNIQUE NOT NULL,
                saldo DECIMAL(10, 2) NOT NULL
            );
            """;

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabla 'clientes' creada o ya existente.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al crear la tabla 'clientes'", e);
        }
    }
}