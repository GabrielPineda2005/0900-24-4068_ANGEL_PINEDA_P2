import java.time.LocalDate;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.*;
import java.io.*;




public class menuchat {

    public static void main(String[] args) {
        CentroDeServicio centro = new CentroDeServicio();
        Scanner sc = new Scanner(System.in);

        int opcion;
        do {
            System.out.println("\n=== SISTEMA DE GESTIÓN DE GARANTÍAS ===");
            System.out.println("1. Registrar computadora nueva");
            System.out.println("2. Mover computadora entre fases");
            System.out.println("3. Entregar computadora al cliente");
            System.out.println("4. Mostrar historial de computadoras");
            System.out.println("5. Mostrar estado actual");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = sc.nextInt();
            sc.nextLine(); // Limpiar buffer

            switch (opcion) {
                case 1:
                    System.out.print("Service Tag: ");
                    String tag = sc.nextLine();

                    System.out.print("Descripción del problema: ");
                    String problema = sc.nextLine();

                    System.out.print("Fecha de recepción (YYYY-MM-DD): ");
                    String fecha = sc.nextLine();
                    LocalDate fechaRecepcion = LocalDate.parse(fecha);

                    System.out.print("Nombre del cliente: ");
                    String nombre = sc.nextLine();

                    System.out.print("Correo del cliente: ");
                    String correo = sc.nextLine();

                    System.out.print("Teléfono del cliente: ");
                    String telefono = sc.nextLine();

                    Computadora nueva = new Computadora(tag, problema, fechaRecepcion, nombre, correo, telefono);
                    centro.registrarComputadora(nueva);
                    break;

                case 2:
                    centro.moverEntreFases();
                    break;

                case 3:
                    centro.entregarComputadora();
                    break;

                case 4:
                    centro.mostrarHistorial();
                    break;

                case 5:
                    centro.mostrarEstadoActual();
                    break;

                case 6:
                    System.out.println("Saliendo del sistema. ¡Hasta luego!");
                    break;

                default:
                    System.out.println("Opción inválida. Intente de nuevo.");
                    break;
            }
        } while (opcion != 6);

        sc.close();
    }
    public class Computadora {
    private String serviceTag;
    private String descripcionProblema;
    private LocalDate fechaRecepcion;
    private String nombreCliente;
    private String correoCliente;
    private String telefonoCliente;

    // Estado actual (etapa del proceso)
    private String estadoActual;

    public Computadora(String serviceTag, String descripcionProblema, LocalDate fechaRecepcion,
                       String nombreCliente, String correoCliente, String telefonoCliente) {
        this.serviceTag = serviceTag;
        this.descripcionProblema = descripcionProblema;
        this.fechaRecepcion = fechaRecepcion;
        this.nombreCliente = nombreCliente;
        this.correoCliente = correoCliente;
        this.telefonoCliente = telefonoCliente;
        this.estadoActual = "Recepción";
    }

    // Getters y Setters
    public String getServiceTag() {
        return serviceTag;
    }

    public String getDescripcionProblema() {
        return descripcionProblema;
    }

    public LocalDate getFechaRecepcion() {
        return fechaRecepcion;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public String getCorreoCliente() {
        return correoCliente;
    }

    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    public String getEstadoActual() {
        return estadoActual;
    }

    public void setEstadoActual(String estadoActual) {
        this.estadoActual = estadoActual;
    }

    @Override
    public String toString() {
        return "Service Tag: " + serviceTag +
               "\nCliente: " + nombreCliente +
               "\nProblema: " + descripcionProblema +
               "\nFecha de recepción: " + fechaRecepcion +
               "\nCorreo: " + correoCliente +
               "\nTeléfono: " + telefonoCliente +
               "\nEstado actual: " + estadoActual;
    }
}


public class CentroDeServicio {

    private Queue<Computadora> recepcion = new LinkedList<>();
    private Queue<Computadora> inspeccion = new LinkedList<>();
    private Queue<Computadora> reparacion = new LinkedList<>();
    private Queue<Computadora> controlCalidad = new LinkedList<>();
    private Queue<Computadora> entrega = new LinkedList<>();

    private List<Computadora> historial = new ArrayList<>();

    // Registrar computadora (entra por recepción)
    public void registrarComputadora(Computadora comp) {
        recepcion.add(comp);
        comp.setEstadoActual("Recepción");
        guardarHistorial(comp, "Recepción");
        System.out.println("Computadora registrada correctamente.");
    }

    // Mover de una cola a otra (según la lógica de fases)
    public void moverEntreFases() {
        if (!recepcion.isEmpty()) {
            Computadora comp = recepcion.poll();
            comp.setEstadoActual("Inspección");
            inspeccion.add(comp);
            guardarHistorial(comp, "Inspección");
            System.out.println("Movida de recepción a inspección: " + comp.getServiceTag());
        } else if (!inspeccion.isEmpty()) {
            Computadora comp = inspeccion.poll();
            Scanner sc = new Scanner(System.in);
            System.out.print("Diagnóstico para " + comp.getServiceTag() + ": ¿Es reparable? (s/n): ");
            String diagnostico = sc.nextLine();
            if (diagnostico.equalsIgnoreCase("s")) {
                comp.setEstadoActual("Reparación");
                reparacion.add(comp);
                guardarHistorial(comp, "Reparación");
            } else {
                comp.setEstadoActual("Entrega");
                entrega.add(comp);
                guardarHistorial(comp, "Entrega directa sin reparación");
            }
        } else if (!reparacion.isEmpty()) {
            Computadora comp = reparacion.poll();
            Scanner sc = new Scanner(System.in);
            System.out.print("Nombre del técnico que reparó " + comp.getServiceTag() + ": ");
            String tecnico = sc.nextLine();
            System.out.print("Descripción de la reparación: ");
            String detalle = sc.nextLine();
            guardarHistorial(comp, "Reparado por: " + tecnico + " | Detalle: " + detalle);

            comp.setEstadoActual("Control de Calidad");
            controlCalidad.add(comp);
            guardarHistorial(comp, "Control de Calidad");
        } else if (!controlCalidad.isEmpty()) {
            Computadora comp = controlCalidad.poll();
            Scanner sc = new Scanner(System.in);
            System.out.print("¿Pasó el control de calidad " + comp.getServiceTag() + "? (s/n): ");
            String ok = sc.nextLine();
            if (ok.equalsIgnoreCase("s")) {
                comp.setEstadoActual("Entrega");
                entrega.add(comp);
                guardarHistorial(comp, "Entrega");
            } else {
                comp.setEstadoActual("Reparación");
                reparacion.add(comp);
                guardarHistorial(comp, "Reingresada a reparación");
            }
        } else {
            System.out.println("No hay computadoras pendientes en ninguna fase.");
        }
    }

    // Entregar computadora al cliente
    public void entregarComputadora() {
        if (!entrega.isEmpty()) {
            Computadora comp = entrega.poll();
            comp.setEstadoActual("Entregada");
            guardarHistorial(comp, "Entregada al cliente");
            System.out.println("Computadora entregada: " + comp.getServiceTag());
        } else {
            System.out.println("No hay computadoras listas para entrega.");
        }
    }

    // Mostrar estado actual de todas las colas
    public void mostrarEstadoActual() {
        System.out.println("Recepción: " + recepcion.size());
        System.out.println("Inspección: " + inspeccion.size());
        System.out.println("Reparación: " + reparacion.size());
        System.out.println("Control de Calidad: " + controlCalidad.size());
        System.out.println("Entrega: " + entrega.size());
    }

    // Mostrar historial de todas las computadoras procesadas
    public void mostrarHistorial() {
        try (BufferedReader br = new BufferedReader(new FileReader("historial.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }
        } catch (IOException e) {
            System.out.println("No se pudo leer el historial.");
        }
    }

    // Guardar historial en archivo
    private void guardarHistorial(Computadora comp, String evento) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("historial.txt", true))) {
            bw.write("[" + comp.getServiceTag() + "] - " + evento + " (" + java.time.LocalDate.now() + ")\n");
        } catch (IOException e) {
            System.out.println("Error al guardar historial.");
        }
    }
}
}
