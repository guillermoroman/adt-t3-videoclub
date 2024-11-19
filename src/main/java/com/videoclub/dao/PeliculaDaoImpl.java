package com.videoclub.dao;

import com.videoclub.model.Pelicula;
import org.mariadb.jdbc.client.result.Result;

import java.sql.*;

import java.util.ArrayList;
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
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, pelicula.getTitulo());
            pstmt.setString(2, pelicula.getDirector());
            pstmt.setInt(3, pelicula.getAnio());
            pstmt.setString(4, pelicula.getGenero());
            pstmt.setFloat(5, pelicula.getPrecio());
            pstmt.executeUpdate();

            // Obtener el ID generado automáticamente
            try (ResultSet rs = pstmt.getGeneratedKeys()){
                if (rs.next()){
                    pelicula.setId(rs.getInt(1));
                }
            }

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
        List<Pelicula> peliculas =  new ArrayList<>();

        String sql = "SELECT * FROM peliculas";

        try( Connection conn = obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){
            while(rs.next()){
                Pelicula pelicula = new Pelicula(
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getString("director"),
                    rs.getInt("anio"),
                    rs.getString("genero"),
                    rs.getFloat("precio"));
                peliculas.add(pelicula);
            }

        }

        return peliculas;
    }

    @Override
    public void actualizarPelicula(Pelicula pelicula) throws SQLException {
        String sql = "UPDATE peliculas SET titulo = ?, director = ?,"+
                " anio = ?, genero = ?, precio = ? WHERE id = ?) ";

        try (Connection conn = obtenerConexion()){
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, pelicula.getTitulo());
            pstmt.setString(2, pelicula.getDirector());
            pstmt.setInt(3, pelicula.getAnio());
            pstmt.setString(4, pelicula.getGenero());
            pstmt.setFloat(5, pelicula.getPrecio());
            pstmt.setInt(6, pelicula.getId());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void eliminarPelicula(int id) throws SQLException {
        String sql = "DELETE FROM peliculas WHERE id = ?";
        try(Connection conn = obtenerConexion();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        }
    }
}
