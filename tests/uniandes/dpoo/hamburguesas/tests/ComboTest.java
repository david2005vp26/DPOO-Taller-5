package uniandes.dpoo.hamburguesas.tests;

import org.junit.Before;
import org.junit.Test;

import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

public class ComboTest {
    
    private Combo combo;
    private ProductoMenu hamburguesa;
    private ProductoMenu papas;
    private ProductoMenu gaseosa;

    @Before
    public void setUp() {

    	hamburguesa = new ProductoMenu("corral queso", 16000);
    	papas = new ProductoMenu("papas medianas", 5500);
    	gaseosa = new ProductoMenu("gaseosa", 5000);
    	

        ArrayList<ProductoMenu> itemsCombo = new ArrayList<>();
        itemsCombo.add(hamburguesa);
        itemsCombo.add(papas);
        itemsCombo.add(gaseosa);

        combo = new Combo("combo corral queso", 0.15, itemsCombo);
    }

    @Test
    public void testGetNombre() {
        assertEquals("combo corral queso", combo.getNombre());
    }

    @Test
    public void testGetPrecio() {
    	
        assertEquals(22525, combo.getPrecio());
    }

    @Test
    public void testGenerarTextoFactura() {
        String expectedFactura = "Combo combo corral queso\n" +
                                 " Descuento: 0.15\n" +
                                 "            22525\n";
        assertEquals(expectedFactura, combo.generarTextoFactura());
    }
}
