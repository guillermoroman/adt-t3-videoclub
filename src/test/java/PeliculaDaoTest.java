import com.videoclub.dao.PeliculaDAO;
import com.videoclub.dao.PeliculaDaoImpl;
import com.videoclub.model.Pelicula;
import org.junit.jupiter.api.*;
import java.sql.SQLException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PeliculaDaoTest {
    private PeliculaDAO peliculaDAO;

    @BeforeEach
    void setUp() {
        peliculaDAO = new PeliculaDaoImpl();
        try {
            // Limpiar la tabla antes de cada prueba
            for (Pelicula pelicula : peliculaDAO.obtenerTodasPeliculas()) {
                peliculaDAO.eliminarPelicula(pelicula.getId());
            }
        } catch (SQLException ignored) {
        }
    }

    @Test
    void testInsertarPelicula() throws SQLException {
        Pelicula pelicula = new Pelicula("Inception", "Christopher Nolan", 2010, "Ciencia Ficción", 3.99f);
        peliculaDAO.insertarPelicula(pelicula);

        assertTrue(pelicula.getId() > 0, "La película debería tener un ID válido después de la inserción");

        Pelicula peliculaObtenida = peliculaDAO.obtenerPelicula(pelicula.getId());
        assertNotNull(peliculaObtenida, "La película debería existir en la base de datos");
        assertEquals("Inception", peliculaObtenida.getTitulo());
        assertEquals("Ciencia Ficción", peliculaObtenida.getGenero());
    }

    @Test
    void testObtenerTodasPeliculas() throws SQLException {
        Pelicula pelicula1 = new Pelicula("Inception", "Christopher Nolan", 2010, "Ciencia Ficción", 3.99f);
        Pelicula pelicula2 = new Pelicula("The Matrix", "Wachowski Sisters", 1999, "Ciencia Ficción",  4.50f);

        peliculaDAO.insertarPelicula(pelicula1);
        peliculaDAO.insertarPelicula(pelicula2);

        // Verifica que se recuperen todas las películas
        List<Pelicula> peliculas = peliculaDAO.obtenerTodasPeliculas();
        assertEquals(2, peliculas.size(), "Debería haber dos películas en la base de datos");
    }

    @Test
    void testActualizarPelicula() throws SQLException {
        Pelicula pelicula = new Pelicula("Inception", "Christopher Nolan", 2010, "Ciencia Ficción", 3.99f);
        peliculaDAO.insertarPelicula(pelicula);

        pelicula.setGenero("Thriller");
        peliculaDAO.actualizarPelicula(pelicula);

        Pelicula peliculaActualizada = peliculaDAO.obtenerPelicula(pelicula.getId());
        assertNotNull(peliculaActualizada, "La película actualizada debería existir en la base de datos");
        assertEquals("Thriller", peliculaActualizada.getGenero(), "El género de la película debería haberse actualizado");
    }

    @Test
    void testEliminarPelicula() throws SQLException {
        Pelicula pelicula = new Pelicula(0, "Inception", "Christopher Nolan", 2010, "Ciencia Ficción", 3.99f);
        peliculaDAO.insertarPelicula(pelicula);

        peliculaDAO.eliminarPelicula(pelicula.getId());

        Pelicula peliculaEliminada = peliculaDAO.obtenerPelicula(pelicula.getId());
        assertNull(peliculaEliminada, "La película debería haber sido eliminada de la base de datos");
    }
}