import java.io.*;
import java.util.Scanner;

//CARNE: 0900-24-4068
//NOMBRE: ANGEL JENNER GABRIEL PINEDA TERCERO
//CURSO PROGRAMACION 1

public class ejercicio1 {

    public static void main(String[] args) {
        String nombreArchivo = "clientes.txt";
        boolean continuar = true;
        Scanner scanner = new Scanner(System.in);

        while (continuar) {
            try {
                System.out.println("\n=== Menú ===");
                System.out.println("1. Ingresar cliente");
                System.out.println("2. Visualizar clientes");
                System.out.println("0. Salir");
                System.out.print("Ingrese una opción: ");

                int opcion = scanner.nextInt();
                scanner.nextLine();
                int edad = 0;
                switch (opcion) {
                    case 1:
                        System.out.print("Ingrese el nombre del cliente: ");
                        String nombre = scanner.nextLine();

                        System.out.print("Ingrese la edad del cliente: ");
                        int edad1 = scanner.nextInt();
                        scanner.nextLine();
                        if (edad1 > 0) {
                            edad = edad1;
                        } else {
                            limpiarConsola();
                            System.out.println("Porfavor ingresar datos validos!");
                            continue;
                        }

                        System.out.print("Ingrese la ciudad del cliente: ");
                        String ciudad = scanner.nextLine();

                        String cliente = nombre + ", " + edad + ", " + ciudad;

                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo, true))) {
                            writer.write(cliente);
                            writer.newLine();
                        }

                        System.out.println("Cliente guardado correctamente");
                        limpiarConsola();
                        break;

                    case 2:
                        System.out.println("=== Lista de clientes ===");
                        File archivo = new File(nombreArchivo);
                        if (!archivo.exists()) {
                            System.out.println("Favor verificar, no hay clientes registrados.");
                        } else {
                            try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
                                String linea;
                                while ((linea = reader.readLine()) != null) {
                                    System.out.println(linea);
                                }
                            }
                        }
                        break;

                    case 0:
                        continuar = false;
                        System.out.println("Saliendo del programa...");
                        break;

                    default:
                        limpiarConsola();
                        System.out.println("Opción no válida.");
                }

            } catch (Exception e) {
                limpiarConsola();
                System.out.println("Error: Ingrese un dato válido.");
                scanner.nextLine();
            }
        }

        scanner.close();
    }

    public static void limpiarConsola() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
}