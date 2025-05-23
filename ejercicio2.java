import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
//CARNE: 0900-24-4068
//NOMBRE: ANGEL JENNER GABRIEL PINEDA TERCERO
// CURSO PROGRAMACION 1



public class ejercicio2 {
    public static void main(String[] args) {
        boolean continuar = true;
        Scanner scanner = new Scanner(System.in);
        Queue<String> cola = new LinkedList<>();
        int contadorID = 1;
        while (continuar) {
            try {
                System.out.println("\n=== Menú ===");
                System.out.println("1. Ingresar cliente");
                System.out.println("2. Atender Cliente");
                System.out.println("3. Lista de clientes en espera");
                System.out.println("Ingrese una opcion: ");
                int opcion = scanner.nextInt();
                scanner.nextLine();
                int articulos = 0;
                switch (opcion) {
                    case 1:
                        System.out.print("Ingrese el nombre del cliente: ");
                        String nombre = scanner.nextLine();
                        System.out.print("Ingrese articulos del cliente: ");
                        int art = scanner.nextInt();
                        scanner.nextLine();
                        if (art > 0) {
                            articulos = art;
                            String cliente = "ID " + contadorID + ": " + nombre + " | Artículos: " + art;
                            cola.offer(cliente);
                            System.out.println("Cliente agregado con éxito.");
                            contadorID++;
                        } else {
                            limpiarConsola();
                            System.out.println("Porfavor ingresar datos validos!");
                            continue;
                        }
                        break;
                    case 2:
                        if (cola.isEmpty()) {
                            System.out.println("No hay clientes para atender.");
                        } else {
                            System.out.println("Cliente atendido: " + cola.poll());
                            if (cola.isEmpty()) {
                                System.out.println("No hay clientes en espera.");
                            } else {
                                System.out.println("Clientes en espera: " + cola);
                            }
                        }
                        break;
                    case 3:
                        if (cola.isEmpty()) {
                            System.out.println("No hay clientes en espera.");
                        } else {
                            System.out.println("Clientes en espera: " + cola);
                        }
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                limpiarConsola();
                System.out.println("Error: Ingrese un dato válido.");
                scanner.nextLine();
            }
        }
    }

    public static void limpiarConsola() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
}
