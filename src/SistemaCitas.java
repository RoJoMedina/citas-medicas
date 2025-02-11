import java.io.*;
import java.util.*;

abstract class Usuario {
    protected String id;
    protected String nombre;

    public Usuario(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}

class Doctor extends Usuario {
    private String especialidad;

    public Doctor(String id, String nombre, String especialidad) {
        super(id, nombre);
        this.especialidad = especialidad;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
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
        this.doctor = null;
        this.paciente = null;
    }

    public void asignarDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public void asignarPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public String getDetallesCita() {
        return "Cita ID: " + id + ", Fecha: " + fecha + ", Hora: " + hora +
                ", Motivo: " + motivo + ", Doctor: " + (doctor != null ? doctor.getNombre() : "No asignado") +
                ", Paciente: " + (paciente != null ? paciente.getNombre() : "No asignado");
    }
}

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

    private void cargarDoctores() {
        try (BufferedReader br = new BufferedReader(new FileReader(directorioDB + "doctores.csv"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 3) {
                    listaDoctores.add(new Doctor(datos[0], datos[1], datos[2]));
                }
            }
        } catch (IOException e) {
            System.out.println("No se encontraron datos de doctores.");
        }
    }

    private void cargarPacientes() {
        try (BufferedReader br = new BufferedReader(new FileReader(directorioDB + "pacientes.csv"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 2) {
                    listaPacientes.add(new Paciente(datos[0], datos[1]));
                }
            }
        } catch (IOException e) {
            System.out.println("No se encontraron datos de pacientes.");
        }
    }

    private void cargarCitas() {
        try (BufferedReader br = new BufferedReader(new FileReader(directorioDB + "citas.csv"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length >= 4) {
                    Cita cita = new Cita(datos[0], datos[1], datos[2], datos[3]);
                    listaCitas.add(cita);
                }
            }
        } catch (IOException e) {
            System.out.println("No se encontraron datos de citas.");
        }
    }

    public void registrarDoctor(Doctor doctor) {
        listaDoctores.add(doctor);
        guardarDatos("doctores.csv", doctor.getId() + "," + doctor.getNombre() + "," + doctor.getEspecialidad());
        System.out.println("Doctor registrado con éxito.");
    }

    public void registrarPaciente(Paciente paciente) {
        listaPacientes.add(paciente);
        guardarDatos("pacientes.csv", paciente.getId() + "," + paciente.getNombre());
        System.out.println("Paciente registrado con éxito.");
    }

    public void crearCita(Cita cita) {
        listaCitas.add(cita);
        guardarDatos("citas.csv", cita.getDetallesCita());
        System.out.println("Cita registrada con éxito.");
    }

    private void guardarDatos(String archivo, String datos) {
        try (FileWriter fw = new FileWriter(directorioDB + archivo, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(datos);
        } catch (IOException e) {
            System.out.println("Error al guardar datos en " + archivo);
        }
    }

    public void asignarCita(String citaId, String doctorId, String pacienteId) {
        Cita cita = buscarCitaPorId(citaId);
        Doctor doctor = buscarDoctorPorId(doctorId);
        Paciente paciente = buscarPacientePorId(pacienteId);

        if (cita != null && doctor != null && paciente != null) {
            cita.asignarDoctor(doctor);
            cita.asignarPaciente(paciente);
            System.out.println("Cita asignada con éxito.");
        } else {
            System.out.println("Error: Doctor, paciente o cita no encontrados.");
        }
    }

    private Cita buscarCitaPorId(String id) {
        for (Cita c : listaCitas) {
            if (c.getDetallesCita().contains(id)) return c;
        }
        return null;
    }

    private Doctor buscarDoctorPorId(String id) {
        for (Doctor d : listaDoctores) {
            if (d.getId().equals(id)) return d;
        }
        return null;
    }

    private Paciente buscarPacientePorId(String id) {
        for (Paciente p : listaPacientes) {
            if (p.getId().equals(id)) return p;
        }
        return null;
    }
}
public class SistemaCitas {
    public static void main(String[] args) {
        GestorCitas gestor = new GestorCitas();

        System.out.println("Sistema de Administración de Citas Médicas");
        System.out.println("Cargando datos...");

        // Verificación de datos cargados
        System.out.println("Doctores cargados: " + gestor.listaDoctores.size());
        System.out.println("Pacientes cargados: " + gestor.listaPacientes.size());
        System.out.println("Citas cargadas: " + gestor.listaCitas.size());
    }
}
