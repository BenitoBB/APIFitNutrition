/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ws;

import dominio.DietaImp;
import dto.RQCrearDieta;
import dto.Respuesta;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import pojo.Dieta;
import pojo.DietaDetalle;
import pojo.DietaResumen;
import utilidades.Validaciones;

/**
 * Endpoint REST para la gestión de dietas.
 * Base path: /api/dieta
 */
@Path("dieta")
public class DietaWS {

    /**
     * POST /api/dieta/crear
     * Crea una nueva dieta con nombre, observaciones y categorías de horario.
     * RN-16: Mínimo 1 categoría requerida.
     * RN-17: Sin límite máximo de categorías.
     */
    @POST
    @Path("crear")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta crear(RQCrearDieta rqCrearDieta) {

        if (rqCrearDieta == null) {
            return new Respuesta(true, "Datos de la dieta no proporcionados.");
        }

        if (Validaciones.esVacio(rqCrearDieta.getNombreDieta())) {
            return new Respuesta(true, "El nombre de la dieta es obligatorio.");
        }

        if (rqCrearDieta.getIdMedico() <= 0) {
            return new Respuesta(true, "El médico es obligatorio.");
        }

        // RN-16: Mínimo 1 categoría requerida
        if (rqCrearDieta.getCategorias() == null || rqCrearDieta.getCategorias().isEmpty()) {
            return new Respuesta(true, "Se requiere al menos una categoría de horario.");
        }

        // Validar que ninguna categoría venga vacía
        for (String nombreCategoria : rqCrearDieta.getCategorias()) {
            if (Validaciones.esVacio(nombreCategoria)) {
                return new Respuesta(true, "El nombre de cada categoría de horario es obligatorio.");
            }
        }

        // Construir el objeto Dieta; total_calorias inicia en 0.00
        Dieta dieta = new Dieta(0, rqCrearDieta.getNombreDieta().trim(),
                0.00, rqCrearDieta.getObservaciones(), rqCrearDieta.getIdMedico());

        return DietaImp.crearDieta(dieta, rqCrearDieta.getCategorias());
    }

    // ── T318: Consultar dietas ────────────────────────────────────────────────

    /**
     * GET /api/dieta/obtener-todas
     * Retorna el listado de todas las dietas con el campo editable.
     * Sin resultados → lista vacía con error=false.
     */
    @GET
    @Path("obtener-todas")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DietaResumen> obtenerTodas() {
        return DietaImp.obtenerTodas();
    }

    /**
     * GET /api/dieta/{id}
     * Retorna el detalle de una dieta con sus categorías y alimentos.
     * Si no existe → Respuesta con error=true.
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Object obtenerDetalle(@PathParam("id") int idDieta) {
        if (idDieta <= 0) {
            return new Respuesta(true, "El ID de la dieta es obligatorio.");
        }

        DietaDetalle detalle = DietaImp.obtenerDetallePorId(idDieta);
        if (detalle == null) {
            return new Respuesta(true, "La dieta no existe.");
        }

        return detalle;
    }
}
