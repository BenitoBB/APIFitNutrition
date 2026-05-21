/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ws;

import com.google.gson.Gson;
import dominio.PacienteImp;
import dto.PacienteUpdateRequest;
import dto.Respuesta;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import pojo.Paciente;

@Path("paciente")
public class PacienteWS {

    @Path("registrar")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Respuesta registrarPaciente(String json) {
        Gson gson = new Gson();
        Paciente paciente = gson.fromJson(json, Paciente.class);
        return PacienteImp.registrarPaciente(paciente);
    }
    
    @Path("actualizar")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Respuesta actualizarPaciente(String json) {
        Gson gson = new Gson();
        PacienteUpdateRequest request = gson.fromJson(json, PacienteUpdateRequest.class);
        return PacienteImp.actualizarPaciente(request);
    }
}