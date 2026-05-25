package ws;

import dominio.ConsultaImp;
import dto.Respuesta;
import java.util.List;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import pojo.Consulta;

@Path("consulta")
public class ConsultaWS {

    @Path("registrar")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta registrarConsulta(Consulta consulta) {

        Respuesta respuesta = new Respuesta();

        if (consulta == null) {

            respuesta.setError(true);
            respuesta.setMensaje("Datos de consulta inválidos");

            return respuesta;
        }

        // VALIDACIONES BASICAS
        if (consulta.getPeso() <= 0) {

            respuesta.setError(true);
            respuesta.setMensaje("El peso debe ser mayor a 0");

            return respuesta;
        }

        if (consulta.getTalla() <= 0) {

            respuesta.setError(true);
            respuesta.setMensaje("La talla debe ser mayor a 0");

            return respuesta;
        }

        if (consulta.getIdPaciente() <= 0) {

            respuesta.setError(true);
            respuesta.setMensaje("Paciente inválido");

            return respuesta;
        }

        if (consulta.getIdMedico() <= 0) {

            respuesta.setError(true);
            respuesta.setMensaje("Médico inválido");

            return respuesta;
        }

        return ConsultaImp.registrarConsulta(consulta);
    }
    
    @Path("modificar")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta modificarConsulta(Consulta consulta) {

        Respuesta respuesta = new Respuesta();

        if (consulta == null) {

            respuesta.setError(true);
            respuesta.setMensaje(
                    "La información de la consulta es obligatoria"
            );

            return respuesta;
        }

        if (consulta.getIdConsulta() <= 0) {

            respuesta.setError(true);
            respuesta.setMensaje(
                    "El idConsulta es obligatorio"
            );

            return respuesta;
        }

        /*
     * Recalcular IMC automáticamente
         */
        double imc = consulta.getPeso()
                / (consulta.getTalla() * consulta.getTalla());

        consulta.setImc(imc);

        boolean resultado
                = ConsultaImp.modificarConsulta(consulta);

        if (resultado) {

            respuesta.setError(false);
            respuesta.setMensaje(
                    "Consulta modificada correctamente"
            );

        } else {

            respuesta.setError(true);
            respuesta.setMensaje(
                    "Error al modificar la consulta o no existe esa consulta"
            );
        }

        return respuesta;
    }

    //T314 -  Buscar consultas de un paciente 
        @Path("buscar/{idPaciente}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Consulta> buscarConsultasPaciente(
            @PathParam("idPaciente") String idPaciente) {

        if (idPaciente == null || idPaciente.isEmpty()) {
            throw new BadRequestException();
        }

        int id = Integer.parseInt(idPaciente);

        return ConsultaImp.buscarConsultasPaciente(id);
    }

    @Path("cancelar/{idConsulta}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta cancelarConsulta(
            @PathParam("idConsulta") String idConsulta) {

        Respuesta respuesta = new Respuesta();

        if (idConsulta == null || idConsulta.isEmpty()) {

            respuesta.setError(true);
            respuesta.setMensaje(
                    "El idConsulta es obligatorio"
            );

            return respuesta;
        }

        int id = Integer.parseInt(idConsulta);

        boolean resultado
                = ConsultaImp.cancelarConsulta(id);

        if (resultado) {

            respuesta.setError(false);
            respuesta.setMensaje(
                    "Consulta cancelada correctamente"
            );

        } else {

            respuesta.setError(true);
            respuesta.setMensaje(
                    "La consulta no existe"
            );
        }

        return respuesta;
    }

}
