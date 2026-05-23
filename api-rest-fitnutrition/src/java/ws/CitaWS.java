/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ws;

import dominio.CitaImp;
import dto.Respuesta;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import pojo.Cita;
import utilidades.Validaciones;

/**
 *
 * @author julia
 */
public class CitaWS {
    @POST
    @Path("crear")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta crear(Cita cita) {
        if (cita == null) {
            return new Respuesta(true, "Datos de cita no proporcionados.");
        }

        if (Validaciones.esVacio(cita.getFechaCita())) {
            return new Respuesta(true, "La fecha de la cita es obligatoria.");
        }

        if (Validaciones.esVacio(cita.getHoraCita())) {
            return new Respuesta(true, "La hora de la cita es obligatoria.");
        }

        if (cita.getIdPaciente() <= 0) {
            return new Respuesta(true, "El paciente es obligatorio.");
        }

        if (cita.getIdMedico() <= 0) {
            return new Respuesta(true, "El médico asignado es obligatorio.");
        }

        // RN-07: La cita debe agendarse con al menos 1 día de antelación
        if (!Validaciones.esFechaCitaValida(cita.getFechaCita())) {
            return new Respuesta(true, "La cita debe agendarse con al menos 1 día de antelación.");
        }

        // Normalizar la fecha a formato ISO (yyyy-MM-dd)
        String fechaFormateada = Validaciones.formatearFechaISO(cita.getFechaCita());
        cita.setFechaCita(fechaFormateada);

        // RN-08: Horario válido (07:00 - 20:30)
        if (!Validaciones.esHoraValida(cita.getHoraCita())) {
            return new Respuesta(true, "Horario no válido.");
        }

        // RN-08: Bloques de 30 minutos
        if (!Validaciones.esBloque30Minutos(cita.getHoraCita())) {
            return new Respuesta(true, "Solo se permiten bloques de 30 minutos.");
        }

        // RN-06: El paciente ya tiene una cita ese día
        Cita citaExistente = CitaImp.buscarCitaPorPacienteYFecha(cita.getIdPaciente(), cita.getFechaCita());
        if (citaExistente != null) {
            return new Respuesta(true, "El paciente ya tiene una cita ese día.");
        }

        return CitaImp.crearCita(cita);
    }
}
