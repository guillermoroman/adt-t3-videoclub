import com.videoclub.dao.PeliculaDAO;
import com.videoclub.dao.PeliculaDaoImpl;
import com.videoclub.model.Pelicula;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PeliculaDAOTest {
    PeliculaDAO peliculaDAO = new PeliculaDaoImpl();

    @BeforeEach
    void setUp() throws SQLException{
        // Limpiar la tabla antes de cada prueba
        for  (Pelicula pelicula: peliculaDAO.obtenerTodasPeliculas()){
            peliculaDAO.eliminarPelicula(pelicula.getId());
        }
    }

    @Test
    void testInsertarPelicula() throws SQLException {
        Pelicula pelicula = new Pelicula("Inception",
                "Christopher Nolan",
                2010,
                "Ciencia Ficción",
                3.99f);
        peliculaDAO.insertarPelicula(pelicula);

        Pelicula peliculaObtenida = peliculaDAO.obtenerPelicula(pelicula.getId());
        assertNotNull(peliculaObtenida);
        assertEquals("Inception", peliculaObtenida.getTitulo());
        assertEquals("Ciencia Ficción", peliculaObtenida.getGenero());

    }

    @Test
    void testObtenerTodasPeliculas() throws SQLException{
        Pelicula pelicula1 = new Pelicula("Inception",
                "Christopher Nolan",
                2010,
                "Ciencia Ficción",
                3.99f);
        Pelicula pelicula2 = new Pelicula("The Matrix",
                "Wachowski Sisters",
                1999,
                "Ciencia Ficción",
                4.50f);

        peliculaDAO.insertarPelicula(pelicula1);
        peliculaDAO.insertarPelicula(pelicula2);

        List<Pelicula> peliculas = peliculaDAO.obtenerTodasPeliculas();
        assertEquals(2, peliculas.size(), "Debería haber dos películas en la base de datos");
    }
}
