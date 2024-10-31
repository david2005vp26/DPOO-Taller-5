package uniandes.dpoo.hamburguesas.tests;

import org.junit.jupiter.api.BeforeEach; 
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import uniandes.dpoo.hamburguesas.mundo.ProductoAjustado;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;
import uniandes.dpoo.hamburguesas.mundo.Ingrediente;

public class ProductoAjustadoTest {
    
    private ProductoAjustado productoAjustadoUno;
    private ProductoMenu productoBase;
    private ArrayList<Ingrediente> agregado;
    private ArrayList<Ingrediente> eliminado;
    
    @BeforeEach
    public void setUp() {
    	
    	agregado = new ArrayList<>();
        eliminado = new ArrayList<>();
    	
        // Crear el producto base
        productoBase = new ProductoMenu("corral", 14000);
        productoAjustadoUno = new ProductoAjustado(productoBase);
        
        // Crear y agregar ingredientes
        Ingrediente lechuga = new Ingrediente("Lechuga", 1000);
        Ingrediente tomate = new Ingrediente("Tomate", 1000);
        Ingrediente cebolla = new Ingrediente("Cebolla", 1000);
        
        // Agregar ingredientes a la lista de agregados
        productoAjustadoUno.agregarIngrediente(lechuga);
        productoAjustadoUno.agregarIngrediente(tomate);
        
        // Agregar ingrediente a la lista de eliminados
        productoAjustadoUno.eliminarIngrediente(cebolla);
    }
    
    @Test
    public void testGetNombre() {
        assertEquals("corral", productoAjustadoUno.getNombre());
    }
    
    @Test
    public void testGetPrecio() {
        // El precio debería ser el precio base más el costo de ingredientes agregados
        assertEquals(16000, productoAjustadoUno.getPrecio());
    }

    @Test
    public void testGenerarTextoFactura() {
    	String expected1 = "corral\n" +
    						"14000\n" + 
			    			"+ Lechuga\n" +
			    			"1000\n" + 
			    			"+ Tomate\n" +
			    			"1000\n" + 
			    			"- Cebolla\n" +
			                "Costo Final Producto Ajustado: " + productoAjustadoUno.getPrecio() + "\n"; 
 
        assertEquals(expected1, productoAjustadoUno.generarTextoFactura());
    }
    
}
