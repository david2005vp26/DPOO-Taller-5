package uniandes.dpoo.hamburguesas.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import uniandes.dpoo.hamburguesas.excepciones.*;
import uniandes.dpoo.hamburguesas.mundo.Ingrediente;
import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.ProductoAjustado;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;
import uniandes.dpoo.hamburguesas.mundo.Restaurante;

public class RestauranteTest {

    private Restaurante restaurante;

    @BeforeEach
    public void setUp() {
        restaurante = new Restaurante();
    }

    @Test
    public void testIniciarPedido() {
        String nombreCliente = "Paula Sepulveda";
        String direccionCliente = "Calle 97-70";

        try {
            if (restaurante.getPedidoEnCurso() != null) {
                throw new YaHayUnPedidoEnCursoException(restaurante.getPedidoEnCurso().getNombreCliente(), nombreCliente);
            }
            restaurante.iniciarPedido(nombreCliente, direccionCliente);
            assertEquals(nombreCliente, restaurante.getPedidoEnCurso().getNombreCliente());
        } catch (YaHayUnPedidoEnCursoException e) {
            System.err.println(e.getMessage());
            assertEquals("Ya existe un pedido en curso, para el cliente " + restaurante.getPedidoEnCurso().getNombreCliente()
                    + " así que no se puede crear un pedido para " + nombreCliente, e.getMessage());
        }
    }

    @Test
    public void testCerrarYGuardarPedido() throws IOException, NoHayPedidoEnCursoException, YaHayUnPedidoEnCursoException {
        // Configuración inicial del pedido en curso
        String nombreCliente = "David Parra";
        String direccionCliente = "Calle 17-24";
        restaurante.iniciarPedido(nombreCliente, direccionCliente);

        ProductoMenu producto = new ProductoMenu("corral queso", 16000);
        restaurante.getMenuBase().add(producto);
        restaurante.getPedidoEnCurso().agregarProducto(producto);

        // Cerrar y guardar el pedido
        assertDoesNotThrow(() -> {
            restaurante.cerrarYGuardarPedido();

            // Verificar que el pedido en curso sea null
            assertNull("El pedido en curso debe ser null después de cerrar y guardar el pedido", restaurante.getPedidoEnCurso());

            // Verificar que el archivo de factura se haya creado con el nombre correcto
            String nombreArchivoFactura = "facturas/factura_" + 1 + ".txt";
            File archivoFactura = new File(nombreArchivoFactura);
            assertTrue("El archivo de factura debería existir", archivoFactura.exists());
        });

        // Intentar cerrar un pedido cuando no hay uno en curso
        assertThrows(NoHayPedidoEnCursoException.class, () -> restaurante.cerrarYGuardarPedido());
    }

    @Test
    public void testCargarInformacionRestaurante() {
        File archivoIngredientes = new File("data/ingredientes.txt");
        File archivoMenu = new File("data/menu.txt");
        File archivoCombos = new File("data/combos.txt");

        assertDoesNotThrow(() -> {
            restaurante.cargarInformacionRestaurante(archivoIngredientes, archivoMenu, archivoCombos);

            // Verificar que se cargaron ingredientes
            assertFalse("La lista de ingredientes debería estar cargada", restaurante.getIngredientes().isEmpty());

            // Verificar que se cargaron productos del menú
            assertFalse("La lista de menú debería estar cargada", restaurante.getMenuBase().isEmpty());

            // Verificar que se cargaron combos
            assertFalse("La lista de combos debería estar cargada", restaurante.getMenuCombos().isEmpty());
        });
    }

    @Test
    public void testCargarIngredientes() {
        File archivoIngredientes = new File("data/ingredientes.txt");
        
        assertDoesNotThrow(() -> {
            restaurante.cargarIngredientes(archivoIngredientes);
            assertFalse(restaurante.getIngredientes().isEmpty());
            assertEquals("lechuga", restaurante.getIngredientes().get(0).getNombre());
        });

        // Cargar ingredientes con un ingrediente repetido
        File archivoIngredientesRepetidos = new File("data/ingredientes_repetidos.txt");
        assertThrows(IngredienteRepetidoException.class, () -> restaurante.cargarIngredientes(archivoIngredientesRepetidos));
    }

    @Test
    public void testCargarCombos() {
        File archivoIngredientes = new File("data/ingredientes.txt");
        File archivoMenu = new File("data/menu.txt");
        File archivoCombos = new File("data/combos.txt");

        // Prueba de carga correcta de combos
        assertDoesNotThrow(() -> {
            restaurante.cargarInformacionRestaurante(archivoIngredientes, archivoMenu, archivoCombos);
            assertFalse("La lista de combos no debería estar vacía después de cargar", restaurante.getMenuCombos().isEmpty());
            assertEquals("combo corral", restaurante.getMenuCombos().get(0).getNombre());
        });

        // Prueba de carga de combos con un producto repetido
        File archivoCombosConRepetido = new File("data/combos_con_producto_repetido.txt");
        assertThrows(ProductoRepetidoException.class, () -> {
            restaurante.cargarCombos(archivoCombosConRepetido);
        });

    }

}
