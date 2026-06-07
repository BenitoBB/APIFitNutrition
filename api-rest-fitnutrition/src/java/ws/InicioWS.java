package ws;

import dominio.InicioImp;
import pojo.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("inicio")
public class InicioWS {

    @GET
    @Path("resumen/{idPaciente}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResumenPaciente resumen(@PathParam("idPaciente") int idPaciente) {
        return InicioImp.obtenerResumen(idPaciente);
    }

    @GET
    @Path("proxima-cita/{idPaciente}")
    @Produces(MediaType.APPLICATION_JSON)
    public ProximaCita proximaCita(@PathParam("idPaciente") int idPaciente) {
        return InicioImp.obtenerProximaCita(idPaciente);
    }

    @GET
    @Path("progreso-peso/{idPaciente}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PuntoPeso> progresoPeso(@PathParam("idPaciente") int idPaciente) {
        return InicioImp.obtenerProgresoPeso(idPaciente);
    }

    @GET
    @Path("dieta-resumen/{idPaciente}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResumenDietaInicio dietaResumen(@PathParam("idPaciente") int idPaciente) {
        return InicioImp.obtenerResumenDieta(idPaciente);
    }

    @GET
    @Path("ultima-consulta/{idPaciente}")
    @Produces(MediaType.APPLICATION_JSON)
    public UltimaConsulta ultimaConsulta(@PathParam("idPaciente") int idPaciente) {
        return InicioImp.obtenerUltimaConsulta(idPaciente);
    }
}