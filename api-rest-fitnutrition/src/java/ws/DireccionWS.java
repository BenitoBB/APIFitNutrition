
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
}
