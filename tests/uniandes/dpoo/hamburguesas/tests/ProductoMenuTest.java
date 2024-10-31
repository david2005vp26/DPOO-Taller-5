package uniandes.dpoo.hamburguesas.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ProductoMenuTest {
	
	private ProductoMenu productoMenuUno;

	@BeforeEach
	public void setUp() {
		productoMenuUno = new ProductoMenu("corral", 14000);
	}
	

    @Test
    public void testGetNombre() {
        assertEquals("corral", productoMenuUno.getNombre());
    }
    
    @Test
    public void testGetPrecio() {
        assertEquals(14000, productoMenuUno.getPrecio());
    }

    @Test
    public void testGenerarTextoFactura() {
        String expected1 = "corral\n"
        				+ "14000\n";
        assertEquals(expected1, productoMenuUno.generarTextoFactura());
    }
}
