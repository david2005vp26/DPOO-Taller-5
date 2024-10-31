package Aplicacion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import uniandes.dpoo.hamburguesas.excepciones.IngredienteRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.NoHayPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoFaltanteException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.YaHayUnPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;
import uniandes.dpoo.hamburguesas.mundo.Restaurante;

public class Aplicacion {
    private Restaurante restaurante;
    private Scanner scanner;

    public Aplicacion() throws IngredienteRepetidoException, ProductoRepetidoException, ProductoFaltanteException, IOException {
        this.restaurante = new Restaurante();
        this.scanner = new Scanner(System.in);
        cargarDatos();
    }

    private void cargarDatos() throws IngredienteRepetidoException, IOException, ProductoRepetidoException, ProductoFaltanteException {
        // Cargar ingredientes, productos y combos desde archivos de texto
    	File archivoIngredientes = new File("data/ingredientes.txt"); 
        File archivoMenu = new File("data/menu.txt"); 
        File archivoCombos = new File("data/combos.txt");
    	
    	restaurante.cargarIngredientes(archivoIngredientes);
        restaurante.cargarMenu(archivoMenu);
        restaurante.cargarCombos(archivoCombos);
    }

    public void iniciar() throws YaHayUnPedidoEnCursoException, NoHayPedidoEnCursoException, IOException {
        int opcion;
        do {
            mostrarMenuPrincipal();
            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

            switch (opcion) {
                case 1:
                    mostrarMenu();
                    break;
                case 2:
                    iniciarNuevoPedido();
                    break;
                case 3:
                    agregarElementoAlPedido();
                    break;
                case 4:
                    CerrarYGuardarPedido();
                    break;
                case 5:
                	consultarPedido();
                	break;
                case 0:
                    System.out.println("Saliendo de la aplicación.");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (opcion != 0);
    }

    private void mostrarMenuPrincipal() {
        System.out.println("=== Menú Principal ===");
        System.out.println("1. Mostrar el menú del restaurante incluyendo los productos básicos y los combos");
        System.out.println("2. Iniciar nuevo pedido");
        System.out.println("3. Agregar un elemento a un pedido, incluyendo ajustes");
        System.out.println("4. Cerrar un pedido y guardar la factura");
        System.out.println("5. Consultar la información de un pedido dado su identificador");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private void mostrarMenu() {
        System.out.println("=== Menú del Restaurante ===");
        System.out.println("=== Menú Base ===");
        for (ProductoMenu producto : restaurante.getMenuBase()) {
            System.out.println(producto.getNombre()); 
        }

        System.out.println("=== Menú Combos ===");
        for (Combo combo : restaurante.getMenuCombos()) {
            System.out.println(combo.getNombre()); 
        }
        
    }

    private void iniciarNuevoPedido() throws YaHayUnPedidoEnCursoException {
        System.out.print("Ingrese el nombre del cliente: ");
        String nombreCliente = scanner.nextLine();
        System.out.print("Ingrese la dirección del cliente: ");
        String direccionCliente = scanner.nextLine();
        restaurante.iniciarPedido(nombreCliente, direccionCliente);
        System.out.println("Pedido iniciado con éxito.");
    }

    private void agregarElementoAlPedido() {
        if (restaurante.getPedidoEnCurso() == null) {
            System.out.println("No hay un pedido en curso. Debe iniciar un pedido antes de agregar elementos.");
            return;
        }
        
        System.out.print("Ingrese el nombre del producto que desea agregar: ");
        String nombreProducto = scanner.nextLine().trim();
        
        // Inicializa el precio como -1 o algún valor que indique que no se encontró el producto
        int precio = -1;
        
        // Buscar el producto en el menú base
        for (ProductoMenu producto : restaurante.getMenuBase()) {
            if (producto.getNombre().equals(nombreProducto)) {
                precio = producto.getPrecio();
                break; // Salimos del bucle una vez que encontramos el producto
            }
        }
        
        // Si el precio sigue siendo -1, significa que el producto no fue encontrado
        if (precio == -1) {
            System.out.println("El producto " + nombreProducto + " no se encuentra en el menú.");
            return;
        }
        
        // Crear el producto con el precio encontrado
        ProductoMenu producto = new ProductoMenu(nombreProducto, precio);
        
        // Agregar el producto al pedido
        restaurante.getPedidoEnCurso().agregarProducto(producto);
        System.out.println("Producto agregado al pedido: " + producto.getNombre());
    }

    

    
    
    
    private void CerrarYGuardarPedido() throws NoHayPedidoEnCursoException, IOException {
    	restaurante.cerrarYGuardarPedido();
    }

    private void consultarPedido() {
    	System.out.print("Ingrese el número de la factura del pedido que quiere buscar bajo el formato 'factura_#ID': ");
        Scanner scanner = new Scanner(System.in);
        String nombreFactura = scanner.nextLine().trim(); // Obtener la entrada del usuario

        // Ruta de la carpeta 'facturas'
        String rutaCarpeta = "facturas/";
        // Construir la ruta completa del archivo
        File archivo = new File(rutaCarpeta + nombreFactura + ".txt");

        try {
            // Crear un objeto Scanner para leer el archivo
            Scanner lectorArchivo = new Scanner(archivo);
            
            // Leer el contenido del archivo y mostrarlo
            System.out.println("Contenido de la factura " + nombreFactura + ":");
            while (lectorArchivo.hasNextLine()) {
                String linea = lectorArchivo.nextLine();
                System.out.println(linea);
            }

            // Cerrar el lector de archivos
            lectorArchivo.close();
        } catch (FileNotFoundException e) {
            System.out.println("El archivo no fue encontrado: " + e.getMessage());
        }
        
    }

    public static void main(String[] args) throws IngredienteRepetidoException, ProductoRepetidoException, ProductoFaltanteException, IOException, YaHayUnPedidoEnCursoException, NoHayPedidoEnCursoException {
        Aplicacion app = new Aplicacion();
        app.iniciar();
    }
}
