package uniandes.dpoo.hamburguesas.mundo;

import java.util.ArrayList;

/**
 * Un producto ajustado es un producto para el cual el cliente solicitó alguna modificación.
 */
public class ProductoAjustado implements Producto
{
    /**
     * El producto base que el cliente sobre el cual el cliente quiere hacer ajustes
     */
    private ProductoMenu productoBase;

    /**
     * La lista de ingrediente que el usuario quiere agregar. El mismo ingrediente puede aparecer varias veces.
     */
    public ArrayList<Ingrediente> agregados;

    /**
     * La lista de ingrediente que el usuario quiere eliminar.
     */
    public ArrayList<Ingrediente> eliminados;

    /**
     * Construye un nuevo producto ajustado a partir del producto base y sin modificaciones
     * @param productoBase El producto base que se va a ajustar
     */
    public ProductoAjustado( ProductoMenu productoBase )
    {
        this.productoBase = productoBase; 
        agregados = new ArrayList<Ingrediente>( );
        eliminados = new ArrayList<Ingrediente>( );
    }

    @Override
    public String getNombre( )
    {
        return productoBase.getNombre( );
    }

    /**
     * Retorna el precio del producto ajustado, que debe ser igual al del producto base, sumándole el precio de los ingredientes adicionales.
     */
    @Override
    public int getPrecio()
    {
    	int precioTotal = productoBase.getPrecio(); 

        for (Ingrediente ing : agregados) {
            precioTotal += ing.getCostoAdicional();
        }

        return precioTotal;
    }

    /**
     * Genera el texto que debe aparecer en la factura.
     * 
     * El texto incluye el producto base, los ingredientes adicionales con su costo, los ingredientes eliminados, y el precio total
     */
    @Override
    public String generarTextoFactura( )
    {
        StringBuffer sb = new StringBuffer( );
        sb.append( productoBase.getNombre() + "\n");
        sb.append( productoBase.getPrecio() + "\n");
        for( Ingrediente ing : agregados )
        {
            sb.append("+ " + ing.getNombre( ) + "\n"); 
            sb.append(ing.getCostoAdicional( ) + "\n");
        }
        for( Ingrediente ing : eliminados )
        {
            sb.append("- " + ing.getNombre( ) + "\n");
        }

        sb.append("Costo Final Producto Ajustado: " + getPrecio( ) + "\n");

        return sb.toString( );
    }
    
    /**
     * Agrega un ingrediente a la lista de ingredientes adicionales.
     * @param ingrediente El ingrediente a agregar.
     */
    public void agregarIngrediente(Ingrediente ingrediente) {
        agregados.add(ingrediente);
    }

    /**
     * Elimina un ingrediente de la lista de ingredientes a eliminar.
     * @param ingrediente El ingrediente a eliminar.
     */
    public void eliminarIngrediente(Ingrediente ingrediente) {
        eliminados.add(ingrediente);
    }
    
}
