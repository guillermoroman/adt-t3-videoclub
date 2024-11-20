package com.videoclub.dao;

import com.videoclub.model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDaoImpl implements ClienteDAO {
    private final String url = "jdbc:mariadb://localhost:3306/adt_t3_videoclub";
    private final String user = "root";
    private final String password = "";

    private Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    @Override
    public void insertarCliente(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO clientes (nombre, dni, saldo) VALUES (?, ?, ?)";
        try (Connection conn = obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) { // Agrega RETURN_GENERATED_KEYS
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getDni());
            stmt.setDouble(3, cliente.getSaldo());
            stmt.executeUpdate();

            // Recuperar el ID generado automáticamente
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    cliente.setId(rs.getInt(1)); // Establecer el ID en el objeto cliente
                }
            }
        }
    }

    @Override
    public Cliente obtenerCliente(int id) throws SQLException {
        String sql = "SELECT * FROM clientes WHERE id = ?";
        try (Connection conn = obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Cliente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("dni"),
                        rs.getDouble("saldo")
                );
            }
        }
        return null;
    }

    @Override
    public List<Cliente> obtenerTodosClientes() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes";
        try (Connection conn = obtenerConexion(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("dni"),
                        rs.getDouble("saldo")
                );
                clientes.add(cliente);
            }
        }
        return clientes;
    }

    @Override
    public void actualizarCliente(Cliente cliente) throws SQLException {
        String sql = "UPDATE clientes SET nombre = ?, dni = ?, saldo = ? WHERE id = ?";
        try (Connection conn = obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getDni());
            stmt.setDouble(3, cliente.getSaldo());
            stmt.setInt(4, cliente.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void eliminarCliente(int id) throws SQLException {
        String sql = "DELETE FROM clientes WHERE id = ?";
        try (Connection conn = obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}