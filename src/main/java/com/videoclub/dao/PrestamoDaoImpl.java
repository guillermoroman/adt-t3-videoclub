package com.videoclub.dao;

import com.videoclub.model.Prestamo;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PrestamoDaoImpl implements PrestamoDAO {

    private final String url = "jdbc:mariadb://localhost:3306/adt_t3_videoclub";
    private final String user = "root";
    private final String password = "";

    public Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    @Override
    public void registrarPrestamo(Prestamo prestamo) throws SQLException {
        String obtenerPrecioPelicula = "SELECT precio FROM peliculas WHERE id = ?";
        String verificarSaldo = "SELECT saldo FROM clientes WHERE id = ?";
        String registrarPrestamo = "INSERT INTO prestamos (cliente_id, pelicula_id, fecha_prestamo) VALUES (?, ?, ?)";
        String actualizarSaldoCliente = "UPDATE clientes SET saldo = saldo - ? WHERE id = ?";
        String marcarNoDisponible = "UPDATE peliculas SET disponible = FALSE WHERE id = ?";

        try (Connection conn = obtenerConexion()) {
            conn.setAutoCommit(false); // Inicia la transacción

            try {
                // Paso 1: Obtener el precio de la película
                float precioPelicula;
                try (PreparedStatement stmt = conn.prepareStatement(obtenerPrecioPelicula)) {
                    stmt.setInt(1, prestamo.getPeliculaId());
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        precioPelicula = rs.getFloat("precio");
                    } else {
                        throw new SQLException("Película no encontrada.");
                    }
                }

                // Paso 2: Verificar saldo del cliente
                float saldoActual;
                try (PreparedStatement stmt = conn.prepareStatement(verificarSaldo)) {
                    stmt.setInt(1, prestamo.getClienteId());
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        saldoActual = rs.getFloat("saldo");
                    } else {
                        throw new SQLException("Cliente no encontrado.");
                    }
                }

                if (saldoActual < precioPelicula) {
                    throw new SQLException("Saldo insuficiente para realizar el préstamo.");
                }

                // Paso 3: Registrar el préstamo
                try (PreparedStatement stmt = conn.prepareStatement(registrarPrestamo, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setInt(1, prestamo.getClienteId());
                    stmt.setInt(2, prestamo.getPeliculaId());
                    stmt.setDate(3, new java.sql.Date(prestamo.getFechaPrestamo().getTime()));
                    stmt.executeUpdate();

                    // Obtener el ID generado
                    try (ResultSet rs = stmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            prestamo.setId(rs.getInt(1));
                        }
                    }
                }

                // Paso 4: Actualizar saldo del cliente
                try (PreparedStatement stmt = conn.prepareStatement(actualizarSaldoCliente)) {
                    stmt.setFloat(1, precioPelicula);
                    stmt.setInt(2, prestamo.getClienteId());
                    stmt.executeUpdate();
                }

                // Paso 5: Marcar película como no disponible
                try (PreparedStatement stmt = conn.prepareStatement(marcarNoDisponible)) {
                    stmt.setInt(1, prestamo.getPeliculaId());
                    stmt.executeUpdate();
                }

                conn.commit(); // Confirma la transacción
            } catch (SQLException e) {
                conn.rollback(); // Deshace la transacción en caso de error
                throw e;
            } finally {
                conn.setAutoCommit(true); // Restaura el modo por defecto
            }
        }
    }

    @Override
    public void registrarDevolucion(int prestamoId) throws SQLException {
        String registrarDevolucion = "UPDATE prestamos SET fecha_devolucion = CURRENT_DATE WHERE id = ?";
        String marcarDisponible = "UPDATE peliculas SET disponible = TRUE WHERE id = ?";
        String obtenerPeliculaId = "SELECT pelicula_id FROM prestamos WHERE id = ?";

        try (Connection conn = obtenerConexion()) {
            conn.setAutoCommit(false); // Inicia la transacción

            try {
                // Paso 1: Obtener la película asociada al préstamo
                int peliculaId;
                try (PreparedStatement stmt = conn.prepareStatement(obtenerPeliculaId)) {
                    stmt.setInt(1, prestamoId);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        peliculaId = rs.getInt("pelicula_id");
                    } else {
                        throw new SQLException("Préstamo no encontrado.");
                    }
                }

                // Paso 2: Registrar la devolución
                try (PreparedStatement stmt = conn.prepareStatement(registrarDevolucion)) {
                    stmt.setInt(1, prestamoId);
                    int filasActualizadas = stmt.executeUpdate();
                    if (filasActualizadas == 0) {
                        throw new SQLException("No se encontró el préstamo con ID: " + prestamoId);
                    }
                }

                // Paso 3: Marcar la película como disponible
                try (PreparedStatement stmt = conn.prepareStatement(marcarDisponible)) {
                    stmt.setInt(1, peliculaId);
                    stmt.executeUpdate();
                }

                conn.commit(); // Confirma la transacción
            } catch (SQLException e) {
                conn.rollback(); // Deshace la transacción en caso de error
                throw e;
            } finally {
                conn.setAutoCommit(true); // Restaura el modo por defecto
            }
        }
    }

    @Override
    public Prestamo obtenerPrestamoPorId(int id) throws SQLException {
        String sql = "SELECT * FROM prestamos WHERE id = ?";
        try (Connection conn = obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Prestamo(
                        rs.getInt("id"),
                        rs.getInt("cliente_id"),
                        rs.getInt("pelicula_id"),
                        rs.getDate("fecha_prestamo"),
                        rs.getDate("fecha_devolucion")
                );
            }
        }
        return null; // Si no se encuentra el préstamo
    }

    @Override
    public List<Prestamo> obtenerPrestamosPorCliente(int clienteId) throws SQLException {
        String sql = "SELECT * FROM prestamos WHERE cliente_id = ?";
        List<Prestamo> prestamos = new ArrayList<>();
        try (Connection conn = obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, clienteId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Prestamo prestamo = new Prestamo(
                        rs.getInt("id"),
                        rs.getInt("cliente_id"),
                        rs.getInt("pelicula_id"),
                        rs.getDate("fecha_prestamo"),
                        rs.getDate("fecha_devolucion")
                );
                prestamos.add(prestamo);
            }
        }
        return prestamos;
    }


}