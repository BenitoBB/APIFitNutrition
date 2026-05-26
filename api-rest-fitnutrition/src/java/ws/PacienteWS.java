/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ws;

import com.google.gson.Gson;
import dominio.PacienteImp;
import dto.RSAutenticacionPaciente;
import dto.Respuesta;
import java.util.List;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import pojo.Dieta;
import pojo.Paciente;
import pojo.ProgresoPaciente;

@Path("paciente")
public class PacienteWS {

    // ── Admin / Portal ────────────────────────────────────────────────────────
    @Path("registrar")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Respuesta registrarPaciente(String json) {
        return PacienteImp.registrarPaciente(new Gson().fromJson(json, Paciente.class));
    }

    @Path("editar")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Respuesta actualizarPaciente(String json) {
        return PacienteImp.actualizarPaciente(new Gson().fromJson(json, Paciente.class));
    }

    @Path("baja/{idPaciente}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta darDeBajaPaciente(@PathParam("idPaciente") int idPaciente) {
        return PacienteImp.darDeBajaPaciente(idPaciente);
    }

    @Path("buscar")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Paciente> buscarPacientes(
            @QueryParam("criterio") String criterio,
            @QueryParam("idMedico") Integer idMedico) {
        return PacienteImp.buscarPacientes(criterio, idMedico);
    }

    // ── Móvil ─────────────────────────────────────────────────────────────────
    @Path("perfil/{idPaciente}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Paciente obtenerPerfilPaciente(@PathParam("idPaciente") int idPaciente) {
        return PacienteImp.obtenerPerfilPaciente(idPaciente);
    }

    @Path("editar-perfil")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Respuesta editarPerfilPaciente(String json) {
        return PacienteImp.editarPerfilPaciente(new Gson().fromJson(json, Paciente.class));
    }

    @Path("actualizar-email")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta actualizarEmail(
            @FormParam("idPaciente") int idPaciente,
            @FormParam("emailActual") String emailActual,
            @FormParam("nuevoEmail") String nuevoEmail) {
        return PacienteImp.actualizarEmail(idPaciente, emailActual, nuevoEmail);
    }

    @Path("actualizar-nip")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta actualizarNip(
            @FormParam("idPaciente") int idPaciente,
            @FormParam("nipActual") String nipActual,
            @FormParam("nuevoNip") String nuevoNip) {
        return PacienteImp.actualizarNip(idPaciente, nipActual, nuevoNip);
    }

    @Path("login")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public RSAutenticacionPaciente loginPaciente(
            @FormParam("email") String email,
            @FormParam("nip") String nip) {

        return PacienteImp.loginPaciente(email, nip);
    }

    //T330 - Consultar dietas y progreso (app móvil)
    @Path("progreso/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProgresoPaciente> obtenerProgresoPaciente(
            @PathParam("id") String id) {

        if (id == null || id.isEmpty()) {
            throw new BadRequestException();
        }

        int idPaciente = Integer.parseInt(id);

        return PacienteImp.obtenerProgresoPaciente(
                idPaciente
        );
    }

    @Path("dietas/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Dieta> obtenerDietasPaciente(
            @PathParam("id") String id) {

        if (id == null || id.isEmpty()) {
            throw new BadRequestException();
        }

        int idPaciente = Integer.parseInt(id);

        return PacienteImp.obtenerDietasPaciente(
                idPaciente
        );
    }
    
    @Path("subir-fotografia/{idPaciente}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta subirFotografia(
            @PathParam("idPaciente") Integer idPaciente,
            byte[] fotografia) {
 
        if (idPaciente != null && idPaciente > 0
                && fotografia != null && fotografia.length > 0) {
            return PacienteImp.guardarFotografia(idPaciente, fotografia);
        }
        throw new BadRequestException();
    }

    @Path("obtener-fotografia/{idPaciente}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Paciente obtenerFotografia(
            @PathParam("idPaciente") Integer idPaciente) {
 
        if (idPaciente != null && idPaciente > 0) {
            return PacienteImp.obtenerFotografia(idPaciente);
        }
        throw new BadRequestException();
    }
}
