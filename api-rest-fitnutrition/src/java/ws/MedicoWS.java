package ws;

import com.google.gson.Gson;
import dominio.MedicoImp;
import dto.Respuesta;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import pojo.Medico;

@Path("medico")
public class MedicoWS {

    @Path("registrar")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Respuesta registrarMedico(String json) {
        return MedicoImp.registrarMedico(new Gson().fromJson(json, Medico.class));
    }

    @Path("editar")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Respuesta editarMedico(String json) {
        return MedicoImp.editarMedico(new Gson().fromJson(json, Medico.class));
    }
    
    @GET
    @Path("buscar")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Medico> buscar(
            @QueryParam("criterio") String criterio,
            @QueryParam("esAdministrador") Integer esAdministrador) {

        // Solo el administrador puede ejecutar este endpoint
        // esAdministrador=1 viene del token/sesión en el frontend
        if (esAdministrador == null || esAdministrador != 1) {
            return new ArrayList<>();
        }

        return MedicoImp.buscarMedicos(criterio);
    }
    
    @Path("cambiar-contrasena")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta cambiarContrasena(
            @FormParam("idMedico")          int    idMedico,
            @FormParam("contrasenaActual")  String contrasenaActual,
            @FormParam("contrasenaNueva")   String contrasenaNueva) {
        return MedicoImp.cambiarContrasena(idMedico, contrasenaActual, contrasenaNueva);
    }

}