package com.videoclub.dao;

import com.videoclub.model.Pelicula;

import java.sql.*;

import java.util.List;

public class PeliculaDaoImpl implements PeliculaDAO{
    private final String url = "jdbc:mariadb://localhost:3306/adt_t3_videoclub";
    private final String user = "root";
    private final String password = "";

    private Connection obtenerConexion() throws SQLException{
        return DriverManager.getConnection(url, user, password);
    }


    @Override
    public void insertarPelicula(Pelicula pelicula) throws SQLException {
        String sql = "INSERT INTO peliculas (titulo, director, anio, genero, precio) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = obtenerConexion()){
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, pelicula.getTitulo());
            pstmt.setString(2, pelicula.getDirector());
            pstmt.setInt(3, pelicula.getAnio());
            pstmt.setString(4, pelicula.getGenero());
            pstmt.setFloat(5, pelicula.getPrecio());
            pstmt.executeUpdate();
        }

    }

    @Override
    public Pelicula obtenerPelicula(int id) throws SQLException {
        String sql = "SELECT * FROM peliculas WHERE id = ?";

        try(Connection conn = obtenerConexion()){
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()){
               return new Pelicula(rs.getInt("id"),
                       rs.getString("titulo"),
                       rs.getString("director"),
                       rs.getInt("anio"),
                       rs.getString("genero"),
                       rs.getFloat("precio"));
            }
        }
        return null;
    }

    @Override
    public List<Pelicula> obtenerTodasPeliculas() throws SQLException {
        return null;
    }

    @Override
    public void actualizarPelicula(Pelicula pelicula) throws SQLException {

    }

    @Override
    public void eliminarPelicula(int id) throws SQLException {

    }
}
