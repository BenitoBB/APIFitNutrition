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
}
