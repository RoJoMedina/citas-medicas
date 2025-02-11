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
    private List<Doctor> listaDoctores;
    private List<Paciente> listaPacientes;
    private List<Cita> listaCitas;

    public GestorCitas() {
        this.listaDoctores = new ArrayList<>();
        this.listaPacientes = new ArrayList<>();
        this.listaCitas = new ArrayList<>();
    }

    public void registrarDoctor(Doctor doctor) {
        listaDoctores.add(doctor);
        System.out.println("Doctor registrado con éxito.");
    }

    public void registrarPaciente(Paciente paciente) {
        listaPacientes.add(paciente);
        System.out.println("Paciente registrado con éxito.");
    }

    public void crearCita(Cita cita) {
        listaCitas.add(cita);
        System.out.println("Cita registrada con éxito.");
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
