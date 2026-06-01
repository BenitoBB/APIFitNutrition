
package ws;

import dominio.DireccionImp;
import java.util.List;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import pojo.Direccion;
import utilidades.Validaciones;

@Path("direccion")
public class DireccionWS {
    @Path("obtener-direccion-codigo-postal/{codigoPostal}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Direccion> obtenerDireccionCodigoPostal(@PathParam("codigoPostal") String codigoPostal){
         if (Validaciones.esVacio(codigoPostal)) {
             throw new BadRequestException();
         }

         if (!Validaciones.esNumericoConLongitud(codigoPostal, 5)) {
             throw new BadRequestException();
         }

         int cp = Integer.parseInt(codigoPostal);

         return DireccionImp.obtenerDireccionCodigoPostal(cp);
    }

    @Path("crear-direccion")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public dto.Respuesta crearDireccion(String json) {
        com.google.gson.Gson gson = new com.google.gson.Gson();
        Direccion direccion = gson.fromJson(json, Direccion.class);
        if (Validaciones.esVacio(direccion.getCalle()))
            throw new BadRequestException("La calle es obligatoria");
        if (direccion.getIdColonia() == null || direccion.getIdColonia() <= 0)
            throw new BadRequestException("El idColonia es obligatorio");
        return DireccionImp.crearDireccion(direccion);
    }

    @Path("editar")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public dto.Respuesta editar(String json) {
        com.google.gson.Gson gson = new com.google.gson.Gson();
        Direccion direccion = gson.fromJson(json, Direccion.class);
        if (direccion.getIdDireccion() == null || direccion.getIdDireccion() <= 0) {
            throw new BadRequestException("El idDireccion es obligatorio");
        }
        return DireccionImp.editar(direccion);
    }

    @Path("obtener-direccion-id/{idDireccion}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Direccion obtenerDireccionPorId(@PathParam("idDireccion") Integer idDireccion){
        if (idDireccion == null || idDireccion <= 0) {
            throw new BadRequestException("El idDireccion es inválido");
        }
        return DireccionImp.obtenerDireccionPorId(idDireccion);
    }
}
