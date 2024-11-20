package com.videoclub.dao;

import com.videoclub.model.Cliente;

import java.sql.SQLException;
import java.util.List;

public interface ClienteDAO {
    void insertarCliente(Cliente cliente) throws SQLException;
    Cliente obtenerCliente(int id) throws SQLException;
    List<Cliente> obtenerTodosClientes() throws SQLException;
    void actualizarCliente(Cliente cliente) throws SQLException;
    void eliminarCliente(int id) throws SQLException;
}