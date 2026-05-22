package ws;

import com.google.gson.Gson;
import dominio.MedicoImp;
import dto.Respuesta;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
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

}