package uniandes.dpoo.hamburguesas.mundo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import uniandes.dpoo.hamburguesas.excepciones.HamburguesaException;
import uniandes.dpoo.hamburguesas.excepciones.IngredienteRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.NoHayPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoFaltanteException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.YaHayUnPedidoEnCursoException;

/**
 * La clase que administra toda la información del restaurante de hamburguesas
 */
public class Restaurante {
    /**
     * La ruta a la carpeta donde se almacenan las facturas
     */
    private static final String CARPETA_FACTURAS = "./facturas/";

    /**
     * La primera parte del nombre de los archivos de facturas
     */
    private static final String PREFIJO_FACTURAS = "factura_";

    // Otras propiedades
    private ArrayList<Pedido> pedidos;
    private ArrayList<Ingrediente> ingredientes;
    private ArrayList<ProductoMenu> menuBase;
    private ArrayList<Combo> menuCombos;
    private Pedido pedidoEnCurso;

    // Constructor
    public Restaurante() {
        pedidos = new ArrayList<Pedido>();
        ingredientes = new ArrayList<Ingrediente>();
        menuBase = new ArrayList<ProductoMenu>();
        menuCombos = new ArrayList<Combo>();
    }

    // Métodos
    public void iniciarPedido(String nombreCliente, String direccionCliente) throws YaHayUnPedidoEnCursoException {
        if (pedidoEnCurso != null)
            throw new YaHayUnPedidoEnCursoException(pedidoEnCurso.getNombreCliente(), nombreCliente);

        pedidoEnCurso = new Pedido(nombreCliente, direccionCliente);
    }

    /**
     * Este método cierra un pedido, creando la factura y guardándolo en el histórico de pedidos.
     * 
     * La factura queda guardada en un archivo cuyo nombre debe ser PREFIJO_FACTURAS, seguido del identificador del pedido,
     * y debe tener extensión 'txt'. El archivo debe quedar en la carpeta CARPETA_FACTURAS.
     * 
     * @throws NoHayPedidoEnCursoException Lanza esta excepción si no hay un pedido en curso
     * @throws IOException Lanza esta excepción si hay problemas guardando el archivo
     */
    public void cerrarYGuardarPedido() throws NoHayPedidoEnCursoException, IOException {
        if (pedidoEnCurso == null) {
            throw new NoHayPedidoEnCursoException();
        }

        // Verificar que la carpeta de facturas exista, si no, crearla
        File carpetaFacturas = new File(CARPETA_FACTURAS);
        if (!carpetaFacturas.exists()) {
            boolean carpetaCreada = carpetaFacturas.mkdir(); // Crea la carpeta si no existe
            if (!carpetaCreada) {
                throw new IOException("No se pudo crear la carpeta de facturas");
            }
        }
        
        
        int contador = 1;
        // Construir el nombre del archivo
        String nombreArchivo = PREFIJO_FACTURAS + contador + ".txt";
        File archivoFactura = new File(carpetaFacturas, nombreArchivo); // Crea el archivo en la carpeta

        // Asegurarse de que el archivo no exista ya

        
        while (archivoFactura.exists()) {
            // Cambiar el nombre del archivo para evitar sobrescritura
            nombreArchivo = PREFIJO_FACTURAS + contador + ".txt";
            archivoFactura = new File(carpetaFacturas, nombreArchivo);
            contador++;
        }
        
        // Guardar la factura en el archivo
        pedidoEnCurso.guardarFactura(archivoFactura);

        // Restablecer el pedido en curso
        pedidoEnCurso = null;
    }



    /**
     * Retorna el pedido actual en curso. Si no hay un pedido en curso, retorna null.
     * 
     * @return El pedido en curso o null
     */
    public Pedido getPedidoEnCurso( )
    {
        return pedidoEnCurso;
    }

    /**
     * Retorna la lista completa de pedidos cerrados en el restaurante.
     * 
     * @return La lista de pedidos cerrados
     */
    public ArrayList<Pedido> getPedidos( )
    {
        return pedidos;
    }

    /**
     * Retorna el menú de hamburguesas básicas del restaurante
     * 
     * @return
     */
    public ArrayList<ProductoMenu> getMenuBase( )
    {
        return menuBase;
    }

    /**
     * Retorna el menú de los combos del restaurante
     * 
     * @return
     */
    public ArrayList<Combo> getMenuCombos( )
    {
        return menuCombos;
    }

    /**
     * Retorna la lista completa de ingredientes del restaurante
     * 
     * @return
     */
    public ArrayList<Ingrediente> getIngredientes( )
    {
        return ingredientes;
    }

    /**
     * Carga la información de los ingredientes, los productos y los combos del restaurante
     * 
     * @param archivoIngredientes El archivo que tiene la información de los ingredientes
     * @param archivoMenu El archivo que tiene la información de los productos base
     * @param archivoCombos El archivo que tiene la información de los combos
     * @throws IOException
     * @throws NumberFormatException
     */
    public void cargarInformacionRestaurante( File archivoIngredientes, File archivoMenu, File archivoCombos ) throws HamburguesaException, NumberFormatException, IOException
    {
        cargarIngredientes( archivoIngredientes );
        cargarMenu( archivoMenu );
        cargarCombos( archivoCombos );
    }

    public void cargarIngredientes( File archivoIngredientes ) throws IngredienteRepetidoException, IOException
    {
        BufferedReader reader = new BufferedReader( new java.io.FileReader( archivoIngredientes ) );
        try
        {
            String linea = reader.readLine( );
            while( linea != null )
            {
                if( !linea.isEmpty( ) )
                {
                    String[] ingredientesStr = linea.split( ";" );
                    String nombreIngrediente = ingredientesStr[ 0 ];
                    int costoIngrediente = Integer.parseInt( ingredientesStr[ 1 ] );
                    Ingrediente ingrediente = new Ingrediente( nombreIngrediente, costoIngrediente );

                    for( Ingrediente i : this.ingredientes )
                    {
                        if( i.getNombre( ).equals( nombreIngrediente ) )
                        {
                            throw new IngredienteRepetidoException( nombreIngrediente );
                        }
                    }
                    this.ingredientes.add( ingrediente );
                }
                linea = reader.readLine( );
            }
        }
        catch( Exception e )
        {
            throw e;
        }
        finally
        {
            reader.close( );
        }
    }

    public void cargarMenu( File archivoMenu ) throws ProductoRepetidoException, IOException
    {
        BufferedReader reader = new BufferedReader( new java.io.FileReader( archivoMenu ) );
        try
        {
            String linea = reader.readLine( );
            while( linea != null )
            {
                if( !linea.isEmpty( ) )
                {
                    String[] productoStr = linea.split( ";" );
                    String nombreProducto = productoStr[ 0 ];
                    int costoProducto = Integer.parseInt( productoStr[ 1 ] );
                    ProductoMenu producto = new ProductoMenu( nombreProducto, costoProducto );

                    for( ProductoMenu prod : this.menuBase )
                    {
                        if( prod.getNombre( ).equals( nombreProducto ) )
                        {
                            throw new ProductoRepetidoException( nombreProducto );
                        }
                    }
                    this.menuBase.add( producto );
                }
                linea = reader.readLine( );
            }
        }
        catch( Exception e )
        {
            throw e;
        }
        finally
        {
            reader.close( );
        }
    }

    public void cargarCombos( File archivoCombos ) throws ProductoRepetidoException, ProductoFaltanteException, IOException
    {
        BufferedReader reader = new BufferedReader( new java.io.FileReader( archivoCombos ) );
        try
        {
            String linea = reader.readLine( );
            while( linea != null )
            {
                if( !linea.isEmpty( ) )
                {
                    String[] comboStr = linea.split( ";" );
                    String nombreCombo = comboStr[ 0 ];
                    double descuento = Double.parseDouble( comboStr[ 1 ].replace( "%", "" ) ) / 100;

                    for( Combo c : this.menuCombos )
                    {
                        if( c.getNombre( ).equals( nombreCombo ) )
                        {
                            throw new ProductoRepetidoException( nombreCombo );
                        }
                    }

                    ArrayList<ProductoMenu> itemsCombo = new ArrayList<>( );
                    for( int i = 2; i < comboStr.length; i++ )
                    {
                        String nombreProducto = comboStr[ i ];
                        ProductoMenu productoItem = null;

                        int index = 0;
                        boolean found = false;
                        while( index < this.menuBase.size( ) && !found )
                        {
                            if( this.menuBase.get( index ).getNombre( ).equals( nombreProducto ) )
                            {
                                productoItem = this.menuBase.get( index );
                                found = true;
                            }
                            index++;
                        }

                        if( productoItem == null )
                        {
                            throw new ProductoFaltanteException( nombreProducto );
                        }

                        itemsCombo.add( productoItem );
                    }

                    Combo combo = new Combo( nombreCombo, descuento, itemsCombo );
                    this.menuCombos.add( combo );
                }
                linea = reader.readLine( );
            }
        }
        catch( Exception e )
        {
            throw e;
        }
        finally
        {
            reader.close( );
        }
    }

}
