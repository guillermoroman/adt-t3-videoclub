package com.videoclub.dao;

import com.videoclub.model.Prestamo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class PrestamoDaoImpl implements PrestamoDAO {
    private final String url = "jdbc:mariadb://localhost:3306/adt_t3_videoclub";
    private final String user = "root";
    private final String password = "";

    private Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    @Override
    public void registrarPrestamo(Prestamo prestamo) throws SQLException {
        // Sentencias SQL
        String obtenerPrecioPelicula ="SELECT precio FROM peliculas WHERE id = ?";
        String verificarSaldo = "SELECT saldo FROM clientes WHERE id = ?";
        String registrarPrestamo = "INSERT INTO prestamos (cliente_id, pelicula_id, fecha_prestamo) VALUES (?, ?, ?)";
        String actualizarSaldoCliente = "UPDATE clientes SET saldo = saldo - ? WHERE id = ?";
        String marcarNoDisponible = "UPDATE peliculas SET disponible = FALSE WHERE id = ?";

        try(Connection conn = obtenerConexion()){
            // Paso 1: Iniciar transacción
            conn.setAutoCommit(false);
            try{
                // Paso 2: Operaciones

                // Paso 3: Cerrar la transacción
                conn.commit();
            } catch (SQLException e){

                // Paso 4
                conn.rollback();
            } finally {

                // Paso 5
                conn.setAutoCommit(true);
            }
        }
    }

    @Override
    public Prestamo obtenerPrestamoPorId(int id) throws SQLException {
        return null;
    }

    @Override
    public List<Prestamo> obtenerPrestamosPorCliente(int clienteId) throws SQLException {
        return List.of();
    }

    @Override
    public void registrarDevolucion(int prestamoId) throws SQLException {

    }
}
