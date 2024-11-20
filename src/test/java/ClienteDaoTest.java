import com.videoclub.dao.ClienteDAO;
import com.videoclub.dao.ClienteDaoImpl;
import com.videoclub.model.Cliente;
import org.junit.jupiter.api.*;
import java.sql.SQLException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ClienteDaoTest {
    private ClienteDAO clienteDAO;

    @BeforeEach
    void setUp() {
        clienteDAO = new ClienteDaoImpl();
        try {
            // Limpiar la tabla antes de cada prueba
            for (Cliente cliente : clienteDAO.obtenerTodosClientes()) {
                clienteDAO.eliminarCliente(cliente.getId());
            }
        } catch (SQLException ignored) {
        }
    }

    @Test
    void testInsertarCliente() throws SQLException {
        Cliente cliente = new Cliente(0, "Juan Pérez", "12345678A", 1500.50);
        clienteDAO.insertarCliente(cliente);

        // Verificar si el cliente fue insertado correctamente
        List<Cliente> clientes = clienteDAO.obtenerTodosClientes();
        assertEquals(1, clientes.size(), "Debería haber un cliente en la base de datos");
        assertEquals("Juan Pérez", clientes.get(0).getNombre());
    }

    @Test
    void testObtenerCliente() throws SQLException {
        Cliente cliente = new Cliente(0, "Ana Gómez", "87654321B", 2500.00);
        clienteDAO.insertarCliente(cliente);

        Cliente clienteObtenido = clienteDAO.obtenerCliente(cliente.getId());
        assertNotNull(clienteObtenido, "El cliente debería existir en la base de datos");
        assertEquals("Ana Gómez", clienteObtenido.getNombre());
    }

    @Test
    void testObtenerTodosClientes() throws SQLException {
        clienteDAO.insertarCliente(new Cliente(0, "Juan Pérez", "12345678A", 1500.50));
        clienteDAO.insertarCliente(new Cliente(0, "Ana Gómez", "87654321B", 2500.00));

        List<Cliente> clientes = clienteDAO.obtenerTodosClientes();
        assertEquals(2, clientes.size(), "Debería haber dos clientes en la base de datos");
    }

    @Test
    void testActualizarCliente() throws SQLException {
        Cliente cliente = new Cliente(0, "Juan Pérez", "12345678A", 1500.50);
        clienteDAO.insertarCliente(cliente);

        cliente.setSaldo(2000.00);
        clienteDAO.actualizarCliente(cliente);

        Cliente clienteActualizado = clienteDAO.obtenerCliente(cliente.getId());
        assertEquals(2000.00, clienteActualizado.getSaldo(), "El saldo del cliente debería haberse actualizado");
    }

    @Test
    void testEliminarCliente() throws SQLException {
        Cliente cliente = new Cliente(0, "Juan Pérez", "12345678A", 1500.50);
        clienteDAO.insertarCliente(cliente);

        clienteDAO.eliminarCliente(cliente.getId());

        Cliente clienteEliminado = clienteDAO.obtenerCliente(cliente.getId());
        assertNull(clienteEliminado, "El cliente debería haber sido eliminado de la base de datos");
    }
}