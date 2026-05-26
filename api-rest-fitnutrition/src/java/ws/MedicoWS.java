package ws;

import com.google.gson.Gson;
import dominio.AutenticacionImp;
import dominio.MedicoImp;
import dto.RQBajaMedico;
import dto.RSAutenticacionMedico;
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
import utilidades.Validaciones;

@Path("medico")
public class MedicoWS {

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RSAutenticacionMedico login(Medico credentials) {
        if (credentials == null || Validaciones.esVacio(credentials.getNoPersonal()) || Validaciones.esVacio(credentials.getContrasena())) {
            return new RSAutenticacionMedico(true, "El número de personal y la contraseña son obligatorios.", null, false);
        }

        // Validación: Máximo 9 caracteres alfanuméricos
        if (!Validaciones.esAlfanumericoConLongitudMaxima(credentials.getNoPersonal(), 9)) {
            return new RSAutenticacionMedico(true, "Número de personal inválido (debe ser alfanumérico de máximo 9 caracteres).", null, false);
        }

        return AutenticacionImp.loginMedico(credentials.getNoPersonal().trim(), credentials.getContrasena());
    }

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
    
    @POST
    @Path("baja")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta baja(RQBajaMedico request) {
        if (request == null) {
            return new Respuesta(true, "Datos de baja no proporcionados.");
        }

        if (request.getEsAdministrador() == null || request.getEsAdministrador() != 1) {
            return new Respuesta(true, "Acceso denegado.");
        }

        if (request.getIdMedicoBaja() == null || request.getIdMedicoBaja() <= 0) {
            return new Respuesta(true, "El medico a dar de baja es obligatorio.");
        }

        if (request.getIdMedicoNuevo() == null || request.getIdMedicoNuevo() <= 0) {
            return new Respuesta(true, "El medico destino es obligatorio.");
        }

        return MedicoImp.bajaMedicoReasignar(request.getIdMedicoBaja(), request.getIdMedicoNuevo());
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