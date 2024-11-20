import com.videoclub.dao.PrestamoDaoImpl;
import com.videoclub.model.Prestamo;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PrestamoDaoTest {

    private PrestamoDaoImpl prestamoDao;

    @BeforeEach
    void setUp() throws SQLException {
        prestamoDao = new PrestamoDaoImpl();

        // Limpieza previa de datos para asegurar un entorno limpio para las pruebas
        limpiarTabla("prestamos");
        limpiarTabla("clientes");
        limpiarTabla("peliculas");

        // Crear datos iniciales
        insertarCliente(1, "Juan Pérez", "12345678A", 100.0f);
        insertarPelicula(1, "Inception", "Christopher Nolan", 2010, "Ciencia Ficción", 3.99f, true);
    }

    @Test
    void testRegistrarPrestamo() throws SQLException {
        Prestamo prestamo = new Prestamo(1, 1, new Date());
        prestamoDao.registrarPrestamo(prestamo);

        // Verificar que el préstamo fue registrado
        Prestamo prestamoGuardado = prestamoDao.obtenerPrestamoPorId(prestamo.getId());
        assertNotNull(prestamoGuardado, "El préstamo debería haber sido registrado.");
        assertEquals(1, prestamoGuardado.getClienteId());
        assertEquals(1, prestamoGuardado.getPeliculaId());
    }

    @Test
    void testRegistrarDevolucion() throws SQLException {
        // Registrar un préstamo
        Prestamo prestamo = new Prestamo(1, 1, new Date());
        prestamoDao.registrarPrestamo(prestamo);

        // Registrar devolución
        prestamoDao.registrarDevolucion(prestamo.getId());

        // Verificar que la devolución fue registrada
        Prestamo prestamoActualizado = prestamoDao.obtenerPrestamoPorId(prestamo.getId());
        assertNotNull(prestamoActualizado.getFechaDevolucion(), "La fecha de devolución debería estar registrada.");
    }

    @Test
    void testSaldoActualizadoAlRegistrarPrestamo() throws SQLException {
        Prestamo prestamo = new Prestamo(1, 1, new Date());
        prestamoDao.registrarPrestamo(prestamo);

        // Verificar que el saldo del cliente fue actualizado
        float saldoActual = obtenerSaldoCliente(1);
        assertEquals(96.01f, saldoActual, 0.01, "El saldo del cliente debería haberse reducido.");
    }

    @Test
    void testRegistrarPrestamoSaldoInsuficiente() {
        // Crear un cliente con saldo insuficiente
        try {
            insertarCliente(2, "Ana Gómez", "87654321B", 2.0f);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Prestamo prestamo = new Prestamo(2, 1, new Date());

        SQLException exception = assertThrows(SQLException.class, () -> prestamoDao.registrarPrestamo(prestamo));
        assertTrue(exception.getMessage().contains("Saldo insuficiente"));
    }

    @Test
    void testObtenerPrestamosPorCliente() throws SQLException {
        // Registrar dos préstamos para el cliente
        Prestamo prestamo1 = new Prestamo(1, 1, new Date());
        prestamoDao.registrarPrestamo(prestamo1);

        Prestamo prestamo2 = new Prestamo(1, 1, new Date());
        prestamoDao.registrarPrestamo(prestamo2);

        // Obtener préstamos por cliente
        List<Prestamo> prestamos = prestamoDao.obtenerPrestamosPorCliente(1);
        assertEquals(2, prestamos.size(), "El cliente debería tener dos préstamos registrados.");
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Limpiar tablas después de cada prueba
        limpiarTabla("prestamos");
        limpiarTabla("clientes");
        limpiarTabla("peliculas");
    }

    // Métodos auxiliares para pruebas
    private void limpiarTabla(String tabla) throws SQLException {
        try (Connection conn = prestamoDao.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM " + tabla)) {
            stmt.executeUpdate();
        }
    }

    private void insertarCliente(int id, String nombre, String dni, float saldo) throws SQLException {
        String sql = "INSERT INTO clientes (id, nombre, dni, saldo) VALUES (?, ?, ?, ?)";
        try (Connection conn = prestamoDao.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setString(2, nombre);
            stmt.setString(3, dni);
            stmt.setFloat(4, saldo);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertarPelicula(int id, String titulo, String director, int anio, String genero, float precio, boolean disponible) throws SQLException {
        String sql = "INSERT INTO peliculas (id, titulo, director, anio, genero, precio, disponible) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = prestamoDao.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setString(2, titulo);
            stmt.setString(3, director);
            stmt.setInt(4, anio);
            stmt.setString(5, genero);
            stmt.setFloat(6, precio);
            stmt.setBoolean(7, disponible);
            stmt.executeUpdate();
        }
    }

    private float obtenerSaldoCliente(int clienteId) throws SQLException {
        String sql = "SELECT saldo FROM clientes WHERE id = ?";
        try (Connection conn = prestamoDao.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, clienteId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getFloat("saldo");
            }
        }
        throw new SQLException("Cliente no encontrado.");
    }
}