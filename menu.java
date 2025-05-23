import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.io.FileWriter;

public class menu {
    public static void main(String[] args) {
        String nombreArchivo = "recepcion.dat";
        File archivo = new File(nombreArchivo);
        try {
            if (!archivo.exists()) {
                archivo.createNewFile();
                FileOutputStream fos = new FileOutputStream(archivo);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                DataOutputStream dos = new DataOutputStream(bos);
                dos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // creacion de archivo 2
        String nombreArchivo2 = "reparacion.dat";
        File archivo2 = new File(nombreArchivo2);
        try {
            if (!archivo2.exists()) {
                archivo2.createNewFile();
                FileOutputStream fos = new FileOutputStream(archivo2);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                DataOutputStream dos = new DataOutputStream(bos);
                dos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // creacion de archivo 3
        String nombreArchivo3 = "control.dat";
        File archivo3 = new File(nombreArchivo3);
        try {
            if (!archivo3.exists()) {
                archivo3.createNewFile();
                FileOutputStream fos = new FileOutputStream(archivo3);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                DataOutputStream dos = new DataOutputStream(bos);
                dos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // creacion de archivo 4
        String nombreArchivo4 = "entrega.dat";
        File archivo4 = new File(nombreArchivo3);
        try {
            if (!archivo4.exists()) {
                archivo4.createNewFile();
                FileOutputStream fos = new FileOutputStream(archivo4);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                DataOutputStream dos = new DataOutputStream(bos);
                dos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        boolean continuar = true;
        Scanner scanner = new Scanner(System.in);
        while (continuar) {
            try {
                System.out.println("\n=== MENU ===");
                System.out.println("1. Recepcion de computadoras");
                System.out.println("2. Inspeccion de computadoras");
                System.out.println("3. Reparacion de computadoras");
                System.out.println("4. Control de calidad");
                System.out.println("5. Entrega de la computadora");
                System.out.println("6. Historial de computadoras");
                System.out.println("0. Salir");
                System.out.println("Ingrese una opcion: ");
                int opcion = scanner.nextInt();
                scanner.nextLine();
                switch (opcion) {
                    case 1:
                        RecepcionComputadora(archivo, scanner, archivo2);
                        break;
                    case 2:
                        ReparacionComputadora(scanner, "reparacion.dat", archivo3);
                        break;
                    case 3:
                        ControldeCalidad(scanner, nombreArchivo3, archivo3, archivo4);
                        break;
                    case 4:
                        EntregaEquipos(scanner, archivo4);
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 0:
                        continuar = false;
                        System.out.println("Saliendo del programa...");
                        break;
                    default:
                        System.out.println("Opción no válida. Por favor, elija una opción válida");
                }

            } catch (Exception e) {
                System.out.println("Error: Ingrese un dato validado.");
                scanner.nextLine();
            }
        }
    }

    public static void RecepcionComputadora(File archivo, Scanner scanner, File archivo2) {
        Queue<String> cola = new LinkedList<>();
        // cola de reparacion de computadoras
        Queue<String> cola2 = new LinkedList<>();
        int contadorID = 1;
        boolean continuar = true;

        // Cargar la cola existente si el archivo ya existe
        if (archivo.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
                cola = (Queue<String>) ois.readObject();
                contadorID = cola.size() + 1; // continuar numeración
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("No se pudo cargar la cola existente. Se iniciará una nueva.");
            }
        }

        // cargar la cola del archivo 2
        if (archivo2.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo2))) {
                cola2 = (Queue<String>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("No se pudo cargar la cola existente. Se iniciará una nueva.");
            }
        }

        while (continuar) {
            try {
                System.out.println("\n === RECEPCIÓN DE COMPUTADORAS ===");
                System.out.println("1. Visualizar computadoras en cola");
                System.out.println("2. Ingreso de computadora");
                System.out.println("3. Finalizar revisión");
                System.out.print("Seleccione una opción: ");
                int opcion = scanner.nextInt();
                scanner.nextLine(); // limpiar buffer

                switch (opcion) {
                    case 1:
                        System.out.println("\nComputadoras en cola:");
                        if (cola.isEmpty()) {
                            System.out.println("No hay computadoras en cola.");
                        } else {
                            for (String entrada : cola) {
                                System.out.println(entrada);
                            }
                        }
                        break;

                    case 2:
                        System.out.println("Ingrese la descripción del problema:");
                        String descripcion = scanner.nextLine();

                        String fecha = obtenerFechaActual();

                        System.out.println("Ingrese el nombre del cliente: ");
                        String nombre = scanner.nextLine();

                        System.out.println("Ingrese el correo electrónico del cliente: ");
                        String correo = scanner.nextLine();

                        System.out.println("Ingrese el número de teléfono del cliente (solo números): ");
                        int telefono = scanner.nextInt();
                        scanner.nextLine(); // limpiar buffer

                        if (telefono > 0) {
                            String cliente = contadorID + "      " + descripcion + "      " + fecha + "      " + nombre
                                    + "      " + correo + "      " + telefono;
                            cola.add(cliente);
                            // crear el archivo del historial individual por computador
                            String nombreArchivoTexto = "PC_" + contadorID + ".txt";
                            try (FileWriter fw = new FileWriter(nombreArchivoTexto)) {
                                fw.write("ID: " + contadorID + "\n");
                                fw.write("Descripción: " + descripcion + "\n");
                                fw.write("Fecha: " + fecha + "\n");
                                fw.write("Nombre del Cliente: " + nombre + "\n");
                                fw.write("Correo: " + correo + "\n");
                                fw.write("Teléfono: " + telefono + "\n");
                                System.out.println(
                                        "Historial de computador : '" + nombreArchivoTexto + "' creado correctamente.");
                            } catch (IOException e) {
                                System.out.println("Error al crear el archivo de texto: " + e.getMessage());
                            }
                            String idpc = String.valueOf(contadorID);
                            contadorID++;
                            // Guardar cola actualizada en archivo binario
                            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
                                oos.writeObject(cola);
                                System.out.println("Datos guardados correctamente en archivo binario.");
                            } catch (IOException e) {
                                System.out.println("Error al guardar los datos: " + e.getMessage());
                            }
                        } else {
                            limpiarConsola();
                            System.out.println("Error: Ingrese un número de teléfono válido.");
                        }
                        break;

                    case 3:
                        System.out.println("Seleccion la accion a realizar:");
                        System.out.println("1. Entrega de equipo");
                        System.out.println("2. Enviar computador a reparacion");
                        System.out.print("Seleccione una opción: ");
                        int opcionfinrevision = scanner.nextInt();
                        scanner.nextLine();
                        switch (opcionfinrevision) {
                            case 1:
                                System.out.println("¡Equipo entregado! " + cola.poll());
                                break;
                            case 2:
                                String equipo = cola.peek();
                                cola2.add(equipo);
                                // guardar la cola del equipo a reparar en archivo binario
                                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo2))) {
                                    oos.writeObject(cola2);
                                    System.out.println("Datos guardados correctamente en archivo binario.");
                                } catch (IOException e) {
                                    System.out.println("Error al guardar los datos: " + e.getMessage());
                                }
                                System.out.println("¡Equipo enviado a reparación! " + cola.poll());
                                break;
                        }
                        continuar = false;
                        System.out.println("Finalizando recepción...");
                        break;

                    default:
                        System.out.println("Opción inválida.");
                }

            } catch (Exception e) {
                System.out.println("Error en la entrada. Intente de nuevo.");
                scanner.nextLine(); // limpiar buffer de entrada
            }
        }
    }

    public static void ReparacionComputadora(Scanner scanner, String nombreArchivo, File archivo3) {
        File archivo = new File(nombreArchivo);
        Queue<String> cola = new LinkedList<>();
        Queue<String> cola3 = new LinkedList<>();

        // Cargar la cola existente
        if (archivo.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
                cola = (Queue<String>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("No se pudo cargar la cola de reparación.");
                return;
            }
        }

        // Cargar la cola 3 (control de calidad)
        if (archivo3.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo3))) {
                cola3 = (Queue<String>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("No se pudo cargar la cola existente. Se iniciará una nueva.");
            }
        }

        boolean continuar = true;
        while (continuar) {
            try {
                System.out.println("\n=== REPARACIÓN DE COMPUTADORAS ===");
                System.out.println("1. Ver computadoras en reparación");
                System.out.println("2. Reparar computadora");
                System.out.println("3. Salir");
                System.out.print("Seleccione una opción: ");
                int opcion = scanner.nextInt();
                scanner.nextLine(); // Limpiar buffer

                switch (opcion) {
                    case 1:
                        System.out.println("\nComputadoras en reparación:");
                        if (cola.isEmpty()) {
                            System.out.println("No hay computadoras en cola.");
                        } else {
                            for (String entrada : cola) {
                                System.out.println(entrada);
                            }
                        }
                        break;

                    case 2:
                        if (cola.isEmpty()) {
                            System.out.println("No hay computadoras para reparar.");
                            break;
                        }

                        String equipo = cola.peek();
                        System.out.println("Reparando equipo: " + equipo);

                        System.out.print("Ingrese el nombre del reparador: ");
                        String nombreReparador = scanner.nextLine();

                        System.out.print("Ingrese la descripción de la reparación realizada: ");
                        String procesoReparacion = scanner.nextLine();

                        String fecha = obtenerFechaActual();

                        String[] partes = equipo.split(" ");
                        String idEquipo = partes[0];
                        String archivoHistorial = "PC_" + idEquipo + ".txt";

                        try (FileWriter fw = new FileWriter(archivoHistorial, true)) {
                            fw.write("\n-----------------------------\n");
                            fw.write("===== ETAPA DE REPARACIÓN =====\n");
                            fw.write("Nombre del reparador: " + nombreReparador + "\n");
                            fw.write("Descripción: " + procesoReparacion + "\n");
                            fw.write("Fecha: " + fecha + "\n");

                            String ccalida = cola.peek();
                            cola3.add(ccalida);

                            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo3))) {
                                oos.writeObject(cola3);
                                System.out.println("Datos guardados correctamente en archivo binario.");
                            } catch (IOException e) {
                                System.out.println("Error al guardar los datos: " + e.getMessage());
                            }

                            System.out.println("¡Equipo enviado a control de calidad! " + cola.poll());
                        } catch (IOException e) {
                            System.out.println("Error al escribir en historial: " + e.getMessage());
                        }

                        cola.poll(); // eliminar equipo reparado

                        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
                            oos.writeObject(cola);
                            System.out.println("Cola de reparación actualizada correctamente.");
                        } catch (IOException e) {
                            System.out.println("Error al guardar la cola: " + e.getMessage());
                        }
                        break;

                    case 3:
                        continuar = false;
                        break;

                    default:
                        System.out.println("Opción inválida. Intente nuevamente.");
                }

            } catch (Exception e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número válido.");
                scanner.nextLine(); // limpiar el buffer para evitar bucles
            }
        }
    }

    public static void ControldeCalidad(Scanner scanner, String nombreArchivo3, File archivo3, File archivo4) {
        Queue<String> cola3 = new LinkedList<>();
        if (archivo3.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo3))) {
                cola3 = (Queue<String>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("No se pudo cargar la cola existente. Se iniciará una nueva.");
            }
        }
        // crear cola para entrega y cargarla en el archivo
        Queue<String> cola4 = new LinkedList<>();
        if (archivo4.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo4))) {
                cola4 = (Queue<String>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("No se pudo cargar la cola existente. Se iniciará una nueva.");
            }
        }
        boolean continuar = true;
        while (continuar) {
            try {
                System.out.println("\n====Control de Calidad====");
                System.out.println("1. Listar equipos para revision");
                System.out.println("2. Enviar a entrega");
                System.out.println("3. Retornar equipo a reparacion");
                System.out.println("4. Salir");
                System.out.println("Ingrese la opcion para realizar: ");
                int opcion = scanner.nextInt();
                scanner.nextLine();
                switch (opcion) {
                    case 1:
                        System.out.println("\nComputadoras en Control de Calidad:");
                        if (cola3.isEmpty()) {
                            System.out.println("No hay computadoras en cola.");
                        } else {
                            for (String entrada : cola3) {
                                System.out.println(entrada);
                            }
                        }
                        break;
                    case 2:
                        if (cola3.isEmpty()) {
                            System.out.println("No hay computadoras para entregar.");
                            String equipo = cola3.peek();

                            System.out.println("Ingrese nombre de certificador: ");
                            String nombreCertificador = scanner.nextLine();
                            String fecha = obtenerFechaActual();

                            String[] partes = equipo.split(" ");
                            String idEquipo = partes[0];
                            String archivoHistorial = "PC_" + idEquipo + ".txt";

                            try (FileWriter fw = new FileWriter(archivoHistorial, true)) {
                                fw.write("\n-----------------------------\n");
                                fw.write("===== ETAPA DE CONTROL DE CALIDAD =====\n");
                                fw.write("Fecha de control de calidad: " + fecha + "\n");
                                fw.write("Nombre de quien certifica calidad: " + nombreCertificador + "\n");
                                String entrega = cola3.peek();
                                cola4.add(entrega);
                                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo4))) {
                                    oos.writeObject(cola4);
                                    System.out.println("Datos guardados correctamente en archivo binario.");
                                } catch (IOException e) {
                                    System.out.println("Error al guardar los datos: " + e.getMessage());
                                }
                                System.out.println("¡Equipo enviado a control de calidad! " + cola3.poll());
                            } catch (IOException e) {
                                System.out.println("Error al escribir en historial: " + e.getMessage());
                            }
                        }
                        break;
                    case 3:
                        if (cola3.isEmpty()) {
                            System.out.println("No hay computadoras para retornar.");
                        } else {
                            // Leer la cola de reparación desde archivo
                            Queue<String> cola = new LinkedList<>();
                            File archivoReparacion = new File(nombreArchivo3); // Usa el archivo correcto de reparación
                            if (archivoReparacion.exists()) {
                                try (ObjectInputStream ois = new ObjectInputStream(
                                        new FileInputStream(archivoReparacion))) {
                                    cola = (Queue<String>) ois.readObject();
                                } catch (IOException | ClassNotFoundException e) {
                                    System.out.println(
                                            "No se pudo cargar la cola de reparación existente. Se iniciará una nueva.");
                                }
                            }
                            // Retornar el equipo
                            String equipoRetornado = cola3.poll();
                            cola.add(equipoRetornado);
                            System.out.println("Equipo retornado a reparación: " + equipoRetornado);

                            // Guardar cola3 actualizada (sin ese equipo)
                            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo3))) {
                                oos.writeObject(cola3);
                                System.out.println("Cola de control de calidad actualizada.");
                            } catch (IOException e) {
                                System.out.println("Error al guardar la cola de control de calidad: " + e.getMessage());
                            }
                            // Guardar cola de reparación actualizada
                            try (ObjectOutputStream oos = new ObjectOutputStream(
                                    new FileOutputStream(archivoReparacion))) {
                                oos.writeObject(cola);
                                System.out.println("Cola de reparación actualizada.");
                            } catch (IOException e) {
                                System.out.println("Error al guardar la cola de reparación: " + e.getMessage());
                            }
                            String[] partes = equipoRetornado.split(" ");
                            String idEquipo = partes[0];
                            String archivoHistorial = "PC_" + idEquipo + ".txt";
                            String fecha = obtenerFechaActual();
                            try (FileWriter fw = new FileWriter(archivoHistorial, true)) {
                                fw.write("\n-----------------------------\n");
                                fw.write("===== RETORNO A REPARACIÓN DESDE CALIDAD =====\n");
                                fw.write("Fecha de retorno: " + fecha + "\n");
                            } catch (IOException e) {
                                System.out.println("Error al escribir en historial: " + e.getMessage());
                            }
                        }
                        break;
                    case 4:
                        continuar = false;
                        break;
                    default:
                        System.out.println("Opción inválida. Intente nuevamente.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número válido.");
                scanner.nextLine(); // limpiar el buffer para evitar bucles
            }
        }
    }

    public static void EntregaEquipos(Scanner scanner, File archivo4) {
        Queue<String> cola4 = new LinkedList<>();
        if (archivo4.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo4))) {
                cola4 = (Queue<String>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("No se pudo cargar la cola existente. Se iniciará una nueva.");
            }
        }

        boolean continuar = true;
        while (continuar) {
            try {
                System.out.println("\n====Entrega de equipos====");
                System.out.println("1. Listar equipos en cola para entrega");
                System.out.println("2. Entrega de equipos");
                System.out.println("3. Salir");
                int opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        System.out.println("\nComputadoras en Control de Calidad:");
                        if (cola4.isEmpty()) {
                            System.out.println("No hay computadoras en cola.");
                        } else {
                            for (String entrada : cola4) {
                                System.out.println(entrada);
                            }
                        }
                        break;
                    case 2:
                        if (cola4.isEmpty()) {
                            System.out.println("No hay equipos para entregar.");
                            break;
                        }

                        String fecha = obtenerFechaActual();
                        String equipoRetornado = cola4.poll();
                        String[] partes = equipoRetornado.split(" ");
                        String idEquipo = partes[0];
                        String archivoHistorial = "PC_" + idEquipo + ".txt";

                        try (FileWriter fw = new FileWriter(archivoHistorial, true)) {
                            fw.write("\n-----------------------------\n");
                            fw.write("===== ETAPA DE ENTREGA =====\n");
                            fw.write("Fecha de entrega: " + fecha + "\n");
                            System.out.println("¡Equipo entregado! ");
                        } catch (IOException e) {
                            System.out.println("Error al escribir en historial: " + e.getMessage());
                        }

                        // Guardar la cola actualizada
                        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo4))) {
                            oos.writeObject(cola4);
                        } catch (IOException e) {
                            System.out.println("Error al guardar la cola actualizada: " + e.getMessage());
                        }
                        break;
                    case 3:
                        continuar = false;
                        break;
                    default:
                        System.out.println("Opción inválida. Intente nuevamente.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
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