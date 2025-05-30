import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.io.FileWriter;

public class menu {

    // --------------------------------------------------------------------------------------------
    public class ManejadorColas {

        private static final String ARCHIVO_COLAS = "colas.dat";

        private static Queue<String> recepcion = new LinkedList<>();
        private static Queue<String> inspeccion = new LinkedList<>();
        private static Queue<String> reparacion = new LinkedList<>();
        private static Queue<String> control = new LinkedList<>();
        private static Queue<String> entrega = new LinkedList<>();
        static {
            cargarColas(); // Cargar colas al iniciar
        }

        private static void cargarColas() {
            File archivo = new File(ARCHIVO_COLAS);
            if (archivo.exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
                    recepcion = (Queue<String>) ois.readObject();
                    inspeccion = (Queue<String>) ois.readObject();
                    reparacion = (Queue<String>) ois.readObject();
                    control = (Queue<String>) ois.readObject();
                    entrega = (Queue<String>) ois.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Error al cargar colas: " + e.getMessage());
                }
            }
        }

        public static void guardarColas() {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_COLAS))) {
                oos.writeObject(recepcion);
                oos.writeObject(inspeccion);
                oos.writeObject(reparacion);
                oos.writeObject(control);
                oos.writeObject(entrega);
            } catch (IOException e) {
                System.out.println("Error al guardar colas: " + e.getMessage());
            }
        }

        // Métodos de acceso directo

        public static Queue<String> getRecepcion() {
            return recepcion;
        }

        public static Queue<String> getInspeccion() {
            return inspeccion;
        }

        public static Queue<String> getReparacion() {
            return reparacion;
        }

        public static Queue<String> getControl() {
            return control;
        }

        public static Queue<String> getEntrega() {
            return control;
        }

        // Métodos para agregar directamente
        public static void encolarRecepcion(String dato) {
            recepcion.add(dato);
            guardarColas();
        }

        public static void encolarInspeccion(String dato) {
            inspeccion.add(dato);
            guardarColas();
        }

        public static void encolarReparacion(String dato) {
            reparacion.add(dato);
            guardarColas();
        }

        public static void encolarControl(String dato) {
            control.add(dato);
            guardarColas();
        }

        public static void encolarEntrega(String dato) {
            control.add(dato);
            guardarColas();
        }
    }

    // --------------------------------------------------------------------------------------------
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;

        while (continuar) {
            try {
                System.out.println("\n=== SISTEMA DE GESTIÓN DE GARANTÍAS ===");
                System.out.println("1. Recepción de computadoras");
                System.out.println("2. Inspección de computadoras");
                System.out.println("3. Reparación de computadoras");
                System.out.println("4. Control de calidad");
                System.out.println("5. Entrega de computadoras");
                System.out.println("6. Consultar Historial de computadora");
                System.out.println("7. Ver historial completo de computadoras");
                System.out.println("0. Salir");
                System.out.print("Ingrese una opción: ");

                int opcion = scanner.nextInt();
                scanner.nextLine(); // limpiar buffer

                switch (opcion) {
                    case 1:
                        RecepcionComputadora(scanner);
                        break;
                    case 2:
                        InspeccionComputadora(scanner);
                        break;
                    case 3:
                        ReparacionComputadora(scanner);
                        break;
                    case 4:
                        ControldeCalidad(scanner);
                        break;
                    case 5:
                        EntregarComputadora(scanner);
                        break;
                    case 6:
                        mostrarHistorialPorServiceTag(scanner);
                        break;
                    case 7:
                        mostrarHistorialCompleto();
                        break;
                    case 0:
                        continuar = false;
                        System.out.println("Saliendo del sistema...");
                        break;
                    default:
                        System.out.println("Opción no válida. Por favor, intente nuevamente.");
                }

            } catch (Exception e) {
                System.out.println("Error: Ingrese un dato válido.");
                scanner.nextLine(); // limpiar error de entrada
            }
        }

        scanner.close();
    }

    private static void registrarEvento(String serviceTag, String evento) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("historial_computadoras.txt", true))) {
            String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            bw.write("[" + fecha + "] [" + serviceTag + "] " + evento + "\n");
        } catch (IOException e) {
            System.out.println("Error al registrar en historial general: " + e.getMessage());
        }
    }

    private static String leerDescripcionProblema(String serviceTag) {
        try (BufferedReader br = new BufferedReader(new FileReader("historial_computadoras.txt"))) {
            String linea;
            boolean encontrada = false;
            while ((linea = br.readLine()) != null) {
                if (linea.contains("Service Tag: " + serviceTag)) {
                    encontrada = true;
                }
                if (encontrada && linea.startsWith("Descripción del problema:")) {
                    return linea.replace("Descripción del problema:", "").trim();
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer historial global.");
        }
        return null;
    }

    public static void RecepcionComputadora(Scanner scanner) {
        boolean continuar = true;
        while (continuar) {
            try {
                System.out.println("\n=== RECEPCIÓN DE COMPUTADORAS ===");

                String serviceTag = solicitarDato(scanner, "Service Tag");
                String modelo = solicitarDato(scanner, "Modelo");
                String nombre = solicitarDato(scanner, "Nombre del cliente");
                String correo = solicitarDato(scanner, "Correo");
                String telefono = solicitarDato(scanner, "Teléfono");
                String descripcion = solicitarDato(scanner, "Descripción del problema");

                String cdatos = serviceTag + " | " + nombre;
                ManejadorColas.encolarInspeccion(cdatos);

                // Guardar datos en archivo
                try (BufferedWriter bw = new BufferedWriter(new FileWriter("historial_computadoras.txt", true))) {
                    bw.write("=== Registro inicial ===\n");
                    bw.write("Service Tag: " + serviceTag + "\n");
                    bw.write("Modelo: " + modelo + "\n");
                    bw.write("Cliente: " + nombre + "\n");
                    bw.write("Correo: " + correo + "\n");
                    bw.write("Teléfono: " + telefono + "\n");
                    bw.write("Descripción del problema: " + descripcion + "\n");
                    bw.write("Fecha de recepción: " + java.time.LocalDate.now() + "\n");
                    bw.write("----------------------------\n");
                }

                registrarEvento(serviceTag, "Recepcionada y encolada a inspección.");
                System.out.println("Registrado exitosamente.");

                System.out.print("¿Registrar otra computadora? (s/n): ");
                String respuesta = scanner.nextLine().trim();
                continuar = respuesta.equalsIgnoreCase("s");

            } catch (Exception e) {
                System.out.println("Error durante la recepción: " + e.getMessage());
            }
        }
    }

    private static String solicitarDato(Scanner scanner, String campo) {
        String dato;
        do {
            System.out.print(campo + ": ");
            dato = scanner.nextLine().trim();
            if (dato.isEmpty()) {
                System.out.println("El campo \"" + campo + "\" no puede estar vacío. Intente nuevamente.");
            }
        } while (dato.isEmpty());
        return dato;
    }

    public static void InspeccionComputadora(Scanner scanner) {
        boolean continuar = true;
        while (continuar) {
            try {
                System.out.println("\n=== INSPECCIÓN DE COMPUTADORAS ===");
                System.out.println("1. Inspección de computadoras");
                System.out.println("2. Verificar estado de la cola de inspección");
                System.out.println("3. Priorizar inspección de computador");
                System.out.println("4. Salir");
                System.out.print("Elija una opción: ");
                int opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {
                    case 1:
                        String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                        Queue<String> colaInspeccion = ManejadorColas.getInspeccion();
                        if (colaInspeccion.isEmpty()) {
                            System.out.println("No hay computadoras en la cola de inspección.");
                            break;
                        }

                        String computadora = colaInspeccion.poll(); // sacar la primera
                        String serviceTag = computadora.split("\\|")[0].trim();

                        System.out.println("Inspeccionando computadora: " + computadora);
                        System.out.println("Problema encontrado :");
                        String descripcion = leerDescripcionProblema(serviceTag);
                        if (descripcion != null) {
                            System.out.println("\n" + descripcion);
                        }

                        System.out.print("¿La computadora es reparable? (s/n): ");
                        String respuesta = scanner.nextLine();

                        if (respuesta.equalsIgnoreCase("s")) {
                            ManejadorColas.encolarReparacion(computadora);
                            registrarEvento(serviceTag, "Inspección: Reparación necesaria. Enviada a reparación.");
                            System.out.println("Computadora encolada en reparación.");
                        } else {
                            registrarEvento(serviceTag,
                                    "Inspección: Computadora sin reparación. Entregada al cliente el: " + fecha);
                            System.out.println("Computadora no reparable. Se entregó directamente al cliente.");
                        }

                        ManejadorColas.guardarColas();
                        break;

                    case 2:
                        System.out.println("Cola de inspección:");
                        for (String comp : ManejadorColas.getInspeccion()) {
                            System.out.println("- " + comp);
                        }
                        break;

                    case 3:
                        Queue<String> colaInspeccion3 = ManejadorColas.getInspeccion();
                        if (colaInspeccion3.isEmpty()) {
                            System.out.println("No hay computadoras en la cola de inspección.");
                            break;
                        }

                        System.out.print("Ingrese el Service Tag de la computadora a inspeccionar: ");
                        String inputTag3 = scanner.nextLine().trim();
                        String computadora3 = null;

                        for (String comp : colaInspeccion3) {
                            String tag = comp.split("\\|")[0].trim();
                            if (tag.equalsIgnoreCase(inputTag3)) {
                                computadora3 = comp;
                                break;
                            }
                        }

                        if (computadora3 == null) {
                            System.out.println("No se encontró la computadora con Service Tag: " + inputTag3);
                            break;
                        }

                        colaInspeccion3.remove(computadora3);
                        String serviceTag3 = inputTag3;
                        String fecha3 = java.time.LocalDate.now().toString();

                        System.out.println("Inspeccionando computadora: " + computadora3);
                        System.out.println("Problema encontrado:");
                        String descripcion3 = leerDescripcionProblema(serviceTag3);
                        if (descripcion3 != null) {
                            System.out.println("\n" + descripcion3);
                        }

                        System.out.print("¿La computadora es reparable? (s/n): ");
                        String respuesta3 = scanner.nextLine();

                        if (respuesta3.equalsIgnoreCase("s")) {
                            ManejadorColas.encolarReparacion(computadora3);
                            registrarEvento(serviceTag3,
                                    "Inspección: Reparación necesaria. Enviada a reparación el: " + fecha3);
                            System.out.println("Computadora encolada en reparación.");
                        } else {
                            registrarEvento(serviceTag3,
                                    "Inspección: Computadora sin reparación. Entregada al cliente el: " + fecha3);
                            System.out.println("Computadora no reparable. Se entregó directamente al cliente.");
                        }

                        ManejadorColas.guardarColas();
                        break;

                    case 4:
                        continuar = false;
                        break;

                    default:
                        System.out.println("Opción no válida. Por favor, ingrese una opción válida");
                        break;
                }

            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un número para la opción del menú.");
            } catch (Exception e) {
                System.out.println("Ocurrió un error durante la inspección: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void ReparacionComputadora(Scanner scanner) {
        boolean continuar = true;
        while (continuar) {
            try {
                System.out.println("\n=== REPARACIÓN DE COMPUTADORAS ===");
                System.out.println("1. Reparar siguiente computadora");
                System.out.println("2. Verificar estado de la cola de reparación");
                System.out.println("3. Priorizar reparación por Service Tag");
                System.out.println("4. Salir");
                System.out.print("Elija una opción: ");
                int opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {
                    case 1: {
                        Queue<String> cola = ManejadorColas.getReparacion();
                        if (cola.isEmpty()) {
                            System.out.println("No hay computadoras en la cola de reparación.");
                            break;
                        }

                        String computadora = cola.poll();
                        String serviceTag = computadora.split("\\|")[0].trim();
                        procesarReparacion(computadora, serviceTag, scanner);
                        break;
                    }

                    case 2: {
                        Queue<String> cola = ManejadorColas.getReparacion();
                        if (cola.isEmpty()) {
                            System.out.println("No hay computadoras en la cola de reparación.");
                        } else {
                            System.out.println("Cola de reparación:");
                            for (String comp : cola) {
                                System.out.println("- " + comp);
                            }
                        }
                        break;
                    }

                    case 3: {
                        Queue<String> cola = ManejadorColas.getReparacion();
                        if (cola.isEmpty()) {
                            System.out.println("No hay computadoras en la cola de reparación.");
                            break;
                        }

                        System.out.print("Ingrese el Service Tag de la computadora a reparar: ");
                        String inputTag = scanner.nextLine().trim();
                        String computadora = null;

                        for (String comp : cola) {
                            String tag = comp.split("\\|")[0].trim();
                            if (tag.equalsIgnoreCase(inputTag)) {
                                computadora = comp;
                                break;
                            }
                        }

                        if (computadora == null) {
                            System.out.println("No se encontró la computadora con Service Tag: " + inputTag);
                            break;
                        }

                        cola.remove(computadora);
                        procesarReparacion(computadora, inputTag, scanner);
                        break;
                    }

                    case 4:
                        continuar = false;
                        break;

                    default:
                        System.out.println("Opción inválida.");
                        break;
                }

            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un número válido para la opción del menú.");
            } catch (Exception e) {
                System.out.println("Ocurrió un error durante el proceso de reparación: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static void procesarReparacion(String computadora, String serviceTag, Scanner scanner) {
        String descripcion = leerDescripcionProblema(serviceTag);
        String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        System.out.println("Computadora a reparar: " + computadora);
        System.out.println("Problema encontrado:");
        if (descripcion != null) {
            System.out.println("\n" + descripcion);
        }

        boolean datosConfirmados = false;
        while (!datosConfirmados) {
            System.out.print("Ingrese nombre del proceso de reparación: ");
            String proceso = scanner.nextLine();

            System.out.print("Ingrese nombre del técnico: ");
            String tecnico = scanner.nextLine();

            System.out.print("¿Están correctos los datos ingresados? (s/n): ");
            String respuesta = scanner.nextLine();

            if (respuesta.equalsIgnoreCase("s")) {
                ManejadorColas.encolarControl(computadora);
                registrarEvento(serviceTag, "Reparación: Reparación realizada por " + tecnico +
                        ". Proceso: " + proceso + ". Enviada a control de calidad el: " + fecha);
                System.out.println("Computadora encolada en control de calidad.");
                datosConfirmados = true;
            } else if (respuesta.equalsIgnoreCase("n")) {
                System.out.println("Reingresando datos de reparación...");
            } else {
                System.out.println("Opción inválida. Intente nuevamente.");
            }
        }
        ManejadorColas.guardarColas();
    }

    public static void ControldeCalidad(Scanner scanner) {
        boolean continuar = true;

        while (continuar) {
            try {
                System.out.println("\n=== CONTROL DE CALIDAD DE COMPUTADORAS ===");
                System.out.println("1. Controlar calidad de la siguiente computadora (FIFO)");
                System.out.println("2. Verificar estado de la cola de control de calidad");
                System.out.println("3. Priorizar control de calidad por Service Tag");
                System.out.println("4. Salir");
                System.out.print("Elija una opción: ");
                int opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1: {
                        Queue<String> colaControl = ManejadorColas.getControl();
                        if (colaControl.isEmpty()) {
                            System.out.println("No hay computadoras en la cola de control de calidad.");
                            break;
                        }

                        String computadora = colaControl.poll();
                        String serviceTag = computadora.split("\\|")[0].trim();
                        procesarControlDeCalidad(computadora, serviceTag, scanner);
                        break;
                    }

                    case 2: {
                        Queue<String> colaControl = ManejadorColas.getControl();
                        if (colaControl.isEmpty()) {
                            System.out.println("La cola de control de calidad está vacía.");
                        } else {
                            System.out.println("Computadoras en control de calidad:");
                            for (String comp : colaControl) {
                                System.out.println("- " + comp);
                            }
                        }
                        break;
                    }

                    case 3: {
                        Queue<String> colaControl = ManejadorColas.getControl();
                        if (colaControl.isEmpty()) {
                            System.out.println("No hay computadoras en la cola de control de calidad.");
                            break;
                        }

                        System.out.print("Ingrese el Service Tag de la computadora a verificar: ");
                        String inputTag = scanner.nextLine().trim();
                        String computadora = null;

                        for (String comp : colaControl) {
                            String tag = comp.split("\\|")[0].trim();
                            if (tag.equalsIgnoreCase(inputTag)) {
                                computadora = comp;
                                break;
                            }
                        }

                        if (computadora == null) {
                            System.out.println("No se encontró la computadora con Service Tag: " + inputTag);
                            break;
                        }

                        colaControl.remove(computadora);
                        procesarControlDeCalidad(computadora, inputTag, scanner);
                        break;
                    }

                    case 4:
                        continuar = false;
                        break;

                    default:
                        System.out.println("Opción inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un número válido para la opción del menú.");
            } catch (Exception e) {
                System.out.println("Ocurrió un error durante el proceso de reparación: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static void procesarControlDeCalidad(String computadora, String serviceTag, Scanner scanner) {
        String descripcion = leerDescripcionProblema(serviceTag);
        String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        System.out.println("Verificando calidad de reparación de: " + computadora);
        if (descripcion != null) {
            System.out.println("Problema original:\n" + descripcion);
        }

        System.out.print("¿La computadora pasó el control de calidad? (s/n): ");
        String respuesta = scanner.nextLine();

        if (respuesta.equalsIgnoreCase("s")) {
            ManejadorColas.encolarEntrega(computadora);
            registrarEvento(serviceTag, "Control de calidad: Aprobada. Enviada a entrega el: " + fecha);
            System.out.println("Computadora encolada a entrega.");
        } else if (respuesta.equalsIgnoreCase("n")) {
            ManejadorColas.encolarReparacion(computadora);
            registrarEvento(serviceTag, "Control de calidad: Fallida. Reenviada a reparación el: " + fecha);
            System.out.println("Computadora reenviada a reparación.");
        } else {
            System.out.println("Entrada inválida. Debe ingresar 's' o 'n'.");
        }

        ManejadorColas.guardarColas();
    }

    public static void EntregarComputadora(Scanner scanner) {
        boolean continuar = true;

        while (continuar) {
            try {
                System.out.println("\n=== ENTREGA DE COMPUTADORAS ===");
                System.out.println("1. Entregar siguiente computadora (FIFO)");
                System.out.println("2. Verificar estado de la cola de entrega");
                System.out.println("3. Priorizar entrega por Service Tag");
                System.out.println("4. Salir");
                System.out.print("Elija una opción: ");
                int opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {
                    case 1: {
                        Queue<String> colaEntrega = ManejadorColas.getEntrega();
                        if (colaEntrega.isEmpty()) {
                            System.out.println("No hay computadoras en la cola de entrega.");
                            break;
                        }

                        String computadora = colaEntrega.poll();
                        String serviceTag = computadora.split("\\|")[0].trim();
                        String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

                        registrarEvento(serviceTag, "Entrega: Computadora entregada al cliente el: " + fecha);
                        System.out.println("Computadora entregada exitosamente.");
                        ManejadorColas.guardarColas();
                        break;
                    }

                    case 2: {
                        Queue<String> colaEntrega = ManejadorColas.getEntrega();
                        if (colaEntrega.isEmpty()) {
                            System.out.println("La cola de entrega está vacía.");
                        } else {
                            System.out.println("Computadoras en cola de entrega:");
                            for (String comp : colaEntrega) {
                                System.out.println("- " + comp);
                            }
                        }
                        break;
                    }

                    case 3: {
                        Queue<String> colaEntrega = ManejadorColas.getEntrega();
                        if (colaEntrega.isEmpty()) {
                            System.out.println("No hay computadoras en la cola de entrega.");
                            break;
                        }

                        System.out.print("Ingrese el Service Tag de la computadora a entregar: ");
                        String inputTag = scanner.nextLine().trim();
                        String computadora = null;

                        for (String comp : colaEntrega) {
                            String tag = comp.split("\\|")[0].trim();
                            if (tag.equalsIgnoreCase(inputTag)) {
                                computadora = comp;
                                break;
                            }
                        }

                        if (computadora == null) {
                            System.out.println("No se encontró la computadora con Service Tag: " + inputTag);
                            break;
                        }

                        colaEntrega.remove(computadora);
                        String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                        registrarEvento(inputTag, "Entrega: Computadora entregada al cliente el: " + fecha);
                        System.out.println("Computadora entregada exitosamente.");
                        ManejadorColas.guardarColas();
                        break;
                    }

                    case 4:
                        continuar = false;
                        break;

                    default:
                        System.out.println("Opción inválida.");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un número válido para la opción del menú.");
            } catch (Exception e) {
                System.out.println("Ocurrió un error durante la entrega: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void mostrarHistorialPorServiceTag(Scanner scanner) {
        System.out.print("Ingrese el Service Tag de la computadora a consultar: ");
        String serviceTag = scanner.nextLine().trim();
        boolean mostrarBloque = false;
        boolean encontrado = false;

        try (BufferedReader br = new BufferedReader(new FileReader("historial_computadoras.txt"))) {
            String linea;
            System.out.println("\n=== HISTORIAL DE " + serviceTag.toUpperCase() + " ===\n");
            while ((linea = br.readLine()) != null) {
                if (linea.contains("Service Tag: " + serviceTag)) {
                    mostrarBloque = true;
                    System.out.println("=== Registro inicial ===");
                    System.out.println(linea);
                    encontrado = true;
                    continue;
                }
                if (mostrarBloque) {
                    if (linea.startsWith("===") || linea.startsWith("[")) {
                        mostrarBloque = false;
                    } else {
                        System.out.println(linea);
                    }
                }
                if (linea.contains("[" + serviceTag + "]")) {
                    System.out.println(linea);
                    encontrado = true;
                }
            }

        } catch (IOException e) {
            System.out.println("Error al leer el archivo de historial: " + e.getMessage());
        }

        if (!encontrado) {
            System.out.println("No se encontró historial para el Service Tag: " + serviceTag);
        }
    }

    public static void mostrarHistorialCompleto() {
        File archivoHistorial = new File("historial_computadoras.txt");
        if (!archivoHistorial.exists()) {
            System.out.println("No existe el archivo de historial.");
            return;
        }

        Map<String, List<String>> datosIniciales = new LinkedHashMap<>();
        Map<String, List<String>> eventos = new LinkedHashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(archivoHistorial))) {
            String linea;
            String currentTag = null;
            List<String> buffer = new ArrayList<>();
            boolean leyendoRegistroInicial = false;

            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty())
                    continue;
                if (linea.startsWith("=== Registro inicial ===")) {
                    if (currentTag != null && !buffer.isEmpty()) {
                        datosIniciales.put(currentTag, new ArrayList<>(buffer));
                    }
                    buffer.clear();
                    buffer.add(linea);
                    currentTag = null;
                    leyendoRegistroInicial = true;
                } else if (leyendoRegistroInicial && linea.startsWith("Service Tag:")) {
                    buffer.add(linea);
                    currentTag = linea.split(":", 2)[1].trim().toLowerCase();
                } else if (leyendoRegistroInicial && !linea.startsWith("[")) {
                    buffer.add(linea);
                } else if (linea.startsWith("[")) {
                    int primerCorchete = linea.indexOf('[');
                    int segundoCorchete = linea.indexOf(']', primerCorchete);
                    int tercerCorchete = linea.indexOf('[', segundoCorchete);
                    int cuartoCorchete = linea.indexOf(']', tercerCorchete);
                    if (primerCorchete >= 0 && segundoCorchete > primerCorchete &&
                            tercerCorchete > segundoCorchete && cuartoCorchete > tercerCorchete) {

                        String tag = linea.substring(tercerCorchete + 1, cuartoCorchete).trim().toLowerCase();
                        eventos.computeIfAbsent(tag, k -> new ArrayList<>()).add(linea);
                    } else {
                        int start = linea.indexOf('[') + 1;
                        int end = linea.indexOf(']', start);
                        if (start > 0 && end > start) {
                            String tag = linea.substring(start, end).trim().toLowerCase();
                            eventos.computeIfAbsent(tag, k -> new ArrayList<>()).add(linea);
                        }
                    }
                    leyendoRegistroInicial = false;
                } else {
                    // Cualquier otra línea que no encaje, la ignoramos o se maneja aca
                }
            }
            if (currentTag != null && !buffer.isEmpty()) {
                datosIniciales.put(currentTag, new ArrayList<>(buffer));
            }

            System.out.println("\n===== HISTORIAL COMPLETO DE COMPUTADORAS =====");

            for (String tag : datosIniciales.keySet()) {
                List<String> registro = datosIniciales.get(tag);
                List<String> eventosTag = eventos.getOrDefault(tag, new ArrayList<>());

                for (String lineaRegistro : registro) {
                    if (!lineaRegistro.trim().equals("----------------------------")) {
                        System.out.println(lineaRegistro);
                    }
                }

                System.out.println("------------------------------------------");

                for (String evento : eventosTag) {
                    System.out.println(evento);
                }

                System.out.println("------------------------------------------");
            }

            System.out.println("Fin del historial.");

        } catch (IOException e) {
            System.out.println("Error al leer el historial completo: " + e.getMessage());
        }
    }

    public static void limpiarConsola() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    public static String obtenerFechaActual() {
        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return fechaActual.format(formato);
    }
}