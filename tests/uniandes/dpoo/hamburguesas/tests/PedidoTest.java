package uniandes.dpoo.hamburguesas.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;

import uniandes.dpoo.hamburguesas.mundo.*;

public class PedidoTest {

    private Pedido pedido;
    private ProductoMenu productoBase1;
    private ProductoMenu productoBase2;
    private Ingrediente lechuga;
    private Ingrediente tomate;
    private Ingrediente cebolla;
    private ProductoAjustado productoAjustadoDos;

    @BeforeEach
    public void setUp() {
        // Inicializar ingredientes
        lechuga = new Ingrediente("Lechuga", 1000);
        tomate = new Ingrediente("Tomate", 1000);
        cebolla = new Ingrediente("Cebolla", 1000);
        
        // Crear productos base
        productoBase1 = new ProductoMenu("costeña", 20000);
        productoBase2 = new ProductoMenu("corral", 14000);
        
        // Crear un producto ajustado
        productoAjustadoDos = new ProductoAjustado(productoBase2);
        productoAjustadoDos.agregados.add(lechuga);
        productoAjustadoDos.agregados.add(tomate);
        productoAjustadoDos.eliminados.add(cebolla);
        
        // Crear un nuevo pedido
        pedido = new Pedido("Paula Sepulveda", "Calle 97-70");
    }

    @Test
    public void testCrearPedido() {
        assertNotNull(pedido);
        assertEquals("Paula Sepulveda", pedido.getNombreCliente());
        assertEquals("Calle 97-70", pedido.getDireccionCliente());
    }

    @Test
    public void testAgregarProducto() {
        // Agregar productos al pedido
        pedido.agregarProducto(productoBase1);
        pedido.agregarProducto(productoAjustadoDos);
        
        assertEquals(2, pedido.productos.size()); // Verificar que se hayan agregado los productos
    }

    @Test
    public void testGetPrecioTotalPedido() {
    	
    	pedido.agregarProducto(productoBase1);
        pedido.agregarProducto(productoAjustadoDos);
        
    	int precioEsperado = (int)((20000 + 14000+2000) * (1+0.19));
        assertEquals(precioEsperado, pedido.getPrecioTotalPedido());
    }

    @Test
    public void testGenerarTextoFactura() {
    	
    	pedido.agregarProducto(productoBase1);
        pedido.agregarProducto(productoAjustadoDos);
        
        String expected = "Cliente: Paula Sepulveda\n" +
                          "Dirección: Calle 97-70\n" +
                          "----------------\n" +
                          "costeña\n" +
                          "20000\n" + // Precio del producto base
                          "corral\n" +
                          "14000\n" +  // Precio del producto ajustado
			    		  "+ Lechuga\n" +
			    		  "1000\n" + 
			    		  "+ Tomate\n" +
			    		  "1000\n" + 
			    	      "- Cebolla\n" +
			              "Costo Final Producto Ajustado: " + productoAjustadoDos.getPrecio() + "\n" +
			              "----------------\n" +
                          "Precio Neto: 36000\n" + // Precio total de los productos
                          "IVA: " + (int)(36000 * 0.19) + "\n" +
                          "Precio Total: " + pedido.getPrecioTotalPedido() + "\n"; // Total con IVA

        assertEquals(expected, pedido.generarTextoFactura());
    }

    @Test
    public void testGuardarFactura() throws FileNotFoundException {
        // Agregar productos al pedido
        pedido.agregarProducto(productoBase1);
        
        // Guardar la factura en un archivo temporal
        File archivo = new File("factura_test.txt");
        pedido.guardarFactura(archivo);

        assertTrue(archivo.exists()); // Verificar que el archivo se haya creado

        // Limpiar después de la prueba
        archivo.delete();
    }
}
