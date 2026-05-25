/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ws;

import dominio.CitaImp;
import dto.Respuesta;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import pojo.Cita;
import pojo.CitaDetalle;
import pojo.CitaMobil;
import utilidades.Validaciones;

/**
 *
 * @author julia
 */
@Path("cita")
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
    
    @PUT
    @Path("modificar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta modificar(Cita cita) {

        // ── Validación básica de entrada ──────────────────────────────────────
        if (cita == null || cita.getIdCita() <= 0) {
            return new Respuesta(true, "El ID de la cita es obligatorio.");
        }

        // Verificar que al menos un campo a modificar viene en el request
        boolean sinCampos = Validaciones.esVacio(cita.getFechaCita())
                && Validaciones.esVacio(cita.getHoraCita())
                && cita.getObservaciones() == null;
        if (sinCampos) {
            return new Respuesta(true, "Debe enviar al menos un campo a modificar.");
        }

        // ── Verificar que la cita existe ──────────────────────────────────────
        Cita citaExistente = CitaImp.buscarCitaPorId(cita.getIdCita());
        if (citaExistente == null) {
            return new Respuesta(true, "La cita no existe.");
        }

        // ── RF-08: Solo modificable si estatus es Confirmada o Reagendada ─────
        String estatus = citaExistente.getEstatus();
        if (!"Confirmada".equals(estatus) && !"Reagendada".equals(estatus)) {
            return new Respuesta(true,
                    "Solo se pueden modificar citas con estatus 'Confirmada' o 'Reagendada'.");
        }

        // ── Validaciones de fecha (solo si se envía) ──────────────────────────
        if (!Validaciones.esVacio(cita.getFechaCita())) {

            // RN-07: fecha >= hoy + 1
            if (!Validaciones.esFechaCitaValida(cita.getFechaCita())) {
                return new Respuesta(true,
                        "La cita debe agendarse con al menos 1 día de antelación.");
            }

            // Normalizar a yyyy-MM-dd
            String fechaFormateada = Validaciones.formatearFechaISO(cita.getFechaCita());
            if (fechaFormateada == null) {
                return new Respuesta(true, "Formato de fecha no válido.");
            }
            cita.setFechaCita(fechaFormateada);

            // RN-06: UNIQUE (id_paciente, fecha_cita) — solo si cambia la fecha
            if (!fechaFormateada.equals(citaExistente.getFechaCita())) {
                Cita citaDuplicada = CitaImp.buscarCitaPorPacienteYFecha(
                        citaExistente.getIdPaciente(), fechaFormateada);
                if (citaDuplicada != null) {
                    return new Respuesta(true,
                            "El paciente ya tiene una cita agendada en esa fecha.");
                }
            }
        }

        // ── Validaciones de hora (solo si se envía) ───────────────────────────
        if (!Validaciones.esVacio(cita.getHoraCita())) {

            // RN-08: rango 07:00 – 20:30
            if (!Validaciones.esHoraValida(cita.getHoraCita())) {
                return new Respuesta(true,
                        "Horario no válido. El rango permitido es 07:00 – 20:30.");
            }

            // RN-08: bloques de 30 minutos
            if (!Validaciones.esBloque30Minutos(cita.getHoraCita())) {
                return new Respuesta(true,
                        "Solo se permiten bloques de 30 minutos (ej. 09:00, 09:30).");
            }
        }

        // ── Todas las validaciones pasaron → modificar ────────────────────────
        return CitaImp.modificarCita(cita);
    }
    
    @PUT
    @Path("reagendar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta reagendar(Cita cita) {

        // ── Validación básica de entrada ──────────────────────────────────────
        if (cita == null || cita.getIdCita() <= 0) {
            return new Respuesta(true, "El ID de la cita es obligatorio.");
        }

        if (Validaciones.esVacio(cita.getFechaCita())) {
            return new Respuesta(true, "La nueva fecha de la cita es obligatoria.");
        }

        if (Validaciones.esVacio(cita.getHoraCita())) {
            return new Respuesta(true, "La nueva hora de la cita es obligatoria.");
        }

        // ── Verificar que la cita existe ──────────────────────────────────────
        Cita citaExistente = CitaImp.buscarCitaPorId(cita.getIdCita());
        if (citaExistente == null) {
            return new Respuesta(true, "La cita no existe.");
        }

        // ── RF-10: Solo reagendable si estatus es Cancelada ──────────────────
        if (!"Cancelada".equals(citaExistente.getEstatus())) {
            return new Respuesta(true,
                    "Solo se pueden reagendar citas con estatus 'Cancelada'.");
        }

        // ── RN-07: fecha >= hoy + 1 ───────────────────────────────────────────
        if (!Validaciones.esFechaCitaValida(cita.getFechaCita())) {
            return new Respuesta(true,
                    "La cita debe agendarse con al menos 1 día de antelación.");
        }

        // Normalizar a yyyy-MM-dd
        String fechaFormateada = Validaciones.formatearFechaISO(cita.getFechaCita());
        if (fechaFormateada == null) {
            return new Respuesta(true, "Formato de fecha no válido.");
        }
        cita.setFechaCita(fechaFormateada);

        // ── RN-06: UNIQUE (id_paciente, fecha_cita) ───────────────────────────
        Cita citaDuplicada = CitaImp.buscarCitaPorPacienteYFecha(
                citaExistente.getIdPaciente(), fechaFormateada);
        if (citaDuplicada != null) {
            return new Respuesta(true,
                    "El paciente ya tiene una cita agendada en esa fecha.");
        }

        // ── RN-08: rango 07:00 – 20:30 ───────────────────────────────────────
        if (!Validaciones.esHoraValida(cita.getHoraCita())) {
            return new Respuesta(true,
                    "Horario no válido. El rango permitido es 07:00 – 20:30.");
        }

        // ── RN-08: bloques de 30 minutos ─────────────────────────────────────
        if (!Validaciones.esBloque30Minutos(cita.getHoraCita())) {
            return new Respuesta(true,
                    "Solo se permiten bloques de 30 minutos (ej. 09:00, 09:30).");
        }

        // ── Todas las validaciones pasaron → reagendar ────────────────────────
        return CitaImp.reagendarCita(cita);
    }
    
    
    // ── Citas Móvil ───────────────────────────────────────────────────────────

    @GET
    @Path("citas/{idPaciente}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CitaMobil> obtenerCitasPaciente(@PathParam("idPaciente") int idPaciente) {
        return CitaImp.obtenerCitasPaciente(idPaciente);
    }

    @PUT
    @Path("cancelar-cita")
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta cancelarCitaMovil(
            @QueryParam("idCita")              int    idCita,
            @QueryParam("idPaciente")          int    idPaciente,
            @QueryParam("motivoCancelacion")   String motivoCancelacion) {

        // ── Validación básica de entrada ──────────────────────────────────────
        if (idCita <= 0 || idPaciente <= 0) {
            return new Respuesta(true, "El ID de la cita y del paciente son obligatorios.");
        }
        // ── RN-09: Desde móvil el motivo es obligatorio ───────────────────────
        if (Validaciones.esVacio(motivoCancelacion)) {
            return new Respuesta(true, "El motivo de cancelación es obligatorio.");
        }

        return CitaImp.cancelarCitaPaciente(idCita, idPaciente, motivoCancelacion.trim());
    }

    //T309 - Buscar y consultar citas
    @GET
    @Path("buscar")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CitaDetalle> buscar(
            @QueryParam("criterio") String criterio,
            @QueryParam("idMedico") Integer idMedico,
            @QueryParam("estatus") String estatus) {
        // idMedico = 0 o null → es administrador, ve todas las citas
        return CitaImp.buscarCitas(criterio, idMedico, estatus);
    }

    @PUT
    @Path("cancelar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta cancelar(Cita cita) {

        // ── Validación básica de entrada ──────────────────────────────────────
        if (cita == null || cita.getIdCita() <= 0) {
            return new Respuesta(true, "El ID de la cita es obligatorio.");
        }

        // ── Verificar que la cita existe ──────────────────────────────────────
        Cita citaExistente = CitaImp.buscarCitaPorId(cita.getIdCita());
        if (citaExistente == null) {
            return new Respuesta(true, "La cita no existe.");
        }

        // ── RF-09: Solo cancelable si estatus es Confirmada o Reagendada ──────
        String estatus = citaExistente.getEstatus();
        if (!"Confirmada".equals(estatus) && !"Reagendada".equals(estatus)) {
            return new Respuesta(true,
                    "Solo se pueden cancelar citas con estatus 'Confirmada' o 'Reagendada'.");
        }

        // ── motivo_cancelacion: opcional desde escritorio (RN-09) ─────────────
        // Se transfiere tal cual, puede llegar null o vacío — ambos son válidos
        cita.setMotivoCancelacion(
                Validaciones.esVacio(cita.getMotivoCancelacion())
                ? null
                : cita.getMotivoCancelacion().trim()
        );

        // ── Todas las validaciones pasaron → cancelar ─────────────────────────
        return CitaImp.cancelarCita(cita);
    }

}
