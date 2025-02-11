import java.io.*;
import java.util.*;

class Administrador {
    private String usuario;
    private String contraseña;

    public Administrador(String usuario, String contraseña) {
        this.usuario = usuario;
        this.contraseña = contraseña;
    }

    public boolean verificarCredenciales(String usuario, String contraseña) {
        return this.usuario.equals(usuario) && this.contraseña.equals(contraseña);
    }
}

abstract class Usuario {
    protected String id;
    protected String nombre;

    public Usuario(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
}

class Doctor extends Usuario {
    private String especialidad;

    public Doctor(String id, String nombre, String especialidad) {
        super(id, nombre);
        this.especialidad = especialidad;
    }

    public String getEspecialidad() { return especialidad; }
}

class Paciente extends Usuario {
    public Paciente(String id, String nombre) {
        super(id, nombre);
    }
}

class Cita {
    private String id;
    private String fecha;
    private String hora;
    private String motivo;
    private Doctor doctor;
    private Paciente paciente;

    public Cita(String id, String fecha, String hora, String motivo) {
        this.id = id;
        this.fecha = fecha;
        this.hora = hora;
        this.motivo = motivo;
    }

    public String getId() { return id; }
    public String getFecha() { return fecha; }
    public String getHora() { return hora; }
    public String getMotivo() { return motivo; }

    public void asignarDoctor(Doctor doctor) { this.doctor = doctor; }
    public void asignarPaciente(Paciente paciente) { this.paciente = paciente; }
}

class GestorCitas {
    public List<Doctor> listaDoctores;
    public List<Paciente> listaPacientes;
    public List<Cita> listaCitas;
    private final String directorioDB = "db/";

    public GestorCitas() {
        this.listaDoctores = new ArrayList<>();
        this.listaPacientes = new ArrayList<>();
        this.listaCitas = new ArrayList<>();
        verificarCarpetaDB();
        cargarDatos();
    }

    private void verificarCarpetaDB() {
        File carpeta = new File(directorioDB);
        if (!carpeta.exists()) {
            carpeta.mkdir();
        }
    }

    private void cargarDatos() {
        cargarDoctores();
        cargarPacientes();
        cargarCitas();
    }

    public void registrarDoctor(Doctor doctor) {
        listaDoctores.add(doctor);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(directorioDB + "doctores.csv", true))) {
            // Separa los campos por comas
            bw.write(doctor.getId() + "," + doctor.getNombre() + "," + doctor.getEspecialidad());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error al guardar doctor: " + e.getMessage());
        }
        System.out.println("Doctor registrado exitosamente.");
    }

    public void registrarPaciente(Paciente paciente) {
        listaPacientes.add(paciente);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(directorioDB + "pacientes.csv", true))) {
            bw.write(paciente.getId() + "," + paciente.getNombre());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error al guardar paciente: " + e.getMessage());
        }
        System.out.println("Paciente registrado exitosamente.");
    }

    public void crearCita(Cita cita) {
        listaCitas.add(cita);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(directorioDB + "citas.csv", true))) {
            bw.write(cita.getId() + "," + cita.getFecha() + "," + cita.getHora() + "," + cita.getMotivo());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error al guardar cita: " + e.getMessage());
        }
        System.out.println("Cita creada exitosamente.");
    }

    private void cargarDoctores() {
        File file = new File(directorioDB + "doctores.csv");
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    Doctor doctor = new Doctor(parts[0], parts[1], parts[2]);
                    listaDoctores.add(doctor);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar doctores: " + e.getMessage());
        }
    }

    private void cargarPacientes() {
        File file = new File(directorioDB + "pacientes.csv");
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    Paciente paciente = new Paciente(parts[0], parts[1]);
                    listaPacientes.add(paciente);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar pacientes: " + e.getMessage());
        }
    }

    private void cargarCitas() {
        File file = new File(directorioDB + "citas.csv");
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    Cita cita = new Cita(parts[0], parts[1], parts[2], parts[3]);
                    listaCitas.add(cita);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar citas: " + e.getMessage());
        }
    }

    public void mostrarMenu() {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\nMenú de Administración de Citas Médicas");
            System.out.println("1. Registrar Doctor");
            System.out.println("2. Registrar Paciente");
            System.out.println("3. Crear Cita");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir salto de línea

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese ID del doctor: ");
                    String idDoctor = scanner.nextLine();
                    System.out.print("Ingrese nombre del doctor: ");
                    String nombreDoctor = scanner.nextLine();
                    System.out.print("Ingrese especialidad del doctor: ");
                    String especialidadDoctor = scanner.nextLine();
                    registrarDoctor(new Doctor(idDoctor, nombreDoctor, especialidadDoctor));
                    break;
                case 2:
                    System.out.print("Ingrese ID del paciente: ");
                    String idPaciente = scanner.nextLine();
                    System.out.print("Ingrese nombre del paciente: ");
                    String nombrePaciente = scanner.nextLine();
                    registrarPaciente(new Paciente(idPaciente, nombrePaciente));
                    break;
                case 3:
                    System.out.print("Ingrese ID de la cita: ");
                    String idCita = scanner.nextLine();
                    System.out.print("Ingrese fecha de la cita (YYYY-MM-DD): ");
                    String fechaCita = scanner.nextLine();
                    System.out.print("Ingrese hora de la cita (HH:MM AM/PM): ");
                    String horaCita = scanner.nextLine();
                    System.out.print("Ingrese motivo de la cita: ");
                    String motivoCita = scanner.nextLine();
                    crearCita(new Cita(idCita, fechaCita, horaCita, motivoCita));
                    break;
                case 4:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción no válida, intente de nuevo.");
            }
        } while (opcion != 4);
        scanner.close();
    }
}
public class SistemaCitas {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Credenciales genéricas para el administrador
        Administrador admin = new Administrador("admin", "1234");

        System.out.println("=== Acceso de Administrador ===");
        System.out.print("Usuario: ");
        String usuario = scanner.nextLine();
        System.out.print("Contraseña: ");
        String contraseña = scanner.nextLine();

        if (admin.verificarCredenciales(usuario, contraseña)) {
            System.out.println("Acceso autorizado.");
            GestorCitas gestor = new GestorCitas();
            System.out.println("Sistema de Administración de Citas Médicas");
            gestor.mostrarMenu();
        } else {
            System.out.println("Credenciales incorrectas. Acceso denegado.");
        }

        scanner.close();
    }
}
