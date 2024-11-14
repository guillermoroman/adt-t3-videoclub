package com.videoclub;

import com.videoclub.dao.PeliculaDAO;
import com.videoclub.dao.PeliculaDaoImpl;
import com.videoclub.model.Pelicula;

import java.sql.SQLException;

public class PruebaPeliculaDAO {
    public static void main(String[] args) {
        PeliculaDAO peliculaDAO = new PeliculaDaoImpl();

        // Insertar nueva pelicula
        Pelicula pelicula = new Pelicula("La Vida es Bella",
                                         "Roberto Benigni",
                                         1999,
                                         "drama",
                                         2.5f);

        try {
            peliculaDAO.insertarPelicula(pelicula);
            System.out.println("Pelicula guardada");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Leer pelicula

        try {
            Pelicula peliculaLeida = peliculaDAO.obtenerPelicula(1);
            System.out.println(peliculaLeida);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
