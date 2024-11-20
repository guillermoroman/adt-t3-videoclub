package com.videoclub.dao;

import com.videoclub.model.Prestamo;

import java.sql.SQLException;
import java.util.List;

public interface PrestamoDAO {
    void registrarPrestamo(Prestamo prestamo) throws SQLException;
    Prestamo obtenerPrestamoPorId(int id) throws SQLException;
    List<Prestamo> obtenerPrestamosPorCliente(int clienteId) throws SQLException;
    void registrarDevolucion(int prestamoId) throws SQLException;

}
