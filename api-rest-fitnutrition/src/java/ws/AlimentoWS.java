/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ws;

import dominio.AlimentoImp;
import dto.Respuesta;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import pojo.Alimento;
import utilidades.Validaciones;

/**
 * Endpoint REST para la gestiÃ³n de alimentos.
 * Base path: /api/alimento
 */
@Path("alimento")
public class AlimentoWS {

    /**
     * POST /api/alimento/registrar
     * Registra un nuevo alimento en el catÃ¡logo.
     * Valida que la porciÃ³n sea vÃ¡lida y las calorÃ­as sean mayores a 0.
     */
    @POST
    @Path("registrar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta registrar(Alimento alimento) {
        if (alimento == null) {
            return new Respuesta(true, "Datos del alimento no proporcionados.");
        }

        if (Validaciones.esVacio(alimento.getNombreAlimento())) {
            return new Respuesta(true, "El nombre del alimento es obligatorio.");
        }

        if (Validaciones.esVacio(alimento.getPorcion())) {
            return new Respuesta(true, "La porciÃ³n del alimento es obligatoria.");
        }

        // ValidaciÃ³n de porciones permitidas (case-insensitive)
        List<String> porcionesValidas = Arrays.asList("Pieza", "Gramos", "Porciones", "Mililitros");
        boolean porcionValida = false;
        
        for (String p : porcionesValidas) {
            if (p.equalsIgnoreCase(alimento.getPorcion().trim())) {
                alimento.setPorcion(p); // Se estandariza como se guardarÃ¡
                porcionValida = true;
                break;
            }
        }
        
        if (!porcionValida) {
            return new Respuesta(true, "La porciÃ³n ingresada no es vÃ¡lida. Opciones permitidas: Pieza, Gramos, Porciones, Mililitros.");
        }

        if (alimento.getCaloriasPorcion() <= 0) {
            return new Respuesta(true, "Las calorÃ­as por porciÃ³n deben ser mayores a 0.");
        }

        return AlimentoImp.registrarAlimento(alimento);
    }

    // â”€â”€ T322: Buscar alimentos â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    /**
     * GET /api/alimento/buscar?nombre={texto}
     * Busca alimentos por nombre (coincidencias parciales).
     * Requiere mÃ­nimo 2 caracteres.
     */
    @GET
    @Path("buscar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscar(@QueryParam("nombre") String nombre) {
        if (nombre != null && nombre.trim().length() == 1) {
            return Response.ok(new Respuesta(true, "La busqueda debe tener al menos 2 caracteres.")).build();
        }

        List<Alimento> resultado = AlimentoImp.buscarPorNombre(nombre != null ? nombre.trim() : "");
        GenericEntity<List<Alimento>> entity = new GenericEntity<List<Alimento>>(resultado) {};
        return Response.ok(entity).build();
    }

    // â”€â”€ T323: Editar alimento â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    /**
     * PUT /api/alimento/editar
     * Edita los datos de un alimento.
     * Si las calorÃ­as cambian, se propaga el cambio a todas las dietas asociadas.
     */
    @PUT
    @Path("editar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta editar(Alimento alimento) {
        if (alimento == null || alimento.getIdAlimento() <= 0) {
            return new Respuesta(true, "El ID del alimento es obligatorio.");
        }

        if (Validaciones.esVacio(alimento.getNombreAlimento())) {
            return new Respuesta(true, "El nombre del alimento es obligatorio.");
        }

        if (Validaciones.esVacio(alimento.getPorcion())) {
            return new Respuesta(true, "La porciÃ³n del alimento es obligatoria.");
        }

        // ValidaciÃ³n de porciones permitidas (case-insensitive)
        List<String> porcionesValidas = Arrays.asList("Pieza", "Gramos", "Porciones", "Mililitros");
        boolean porcionValida = false;
        
        for (String p : porcionesValidas) {
            if (p.equalsIgnoreCase(alimento.getPorcion().trim())) {
                alimento.setPorcion(p); 
                porcionValida = true;
                break;
            }
        }
        
        if (!porcionValida) {
            return new Respuesta(true, "La porciÃ³n ingresada no es vÃ¡lida. Opciones permitidas: Pieza, Gramos, Porciones, Mililitros.");
        }

        if (alimento.getCaloriasPorcion() <= 0) {
            return new Respuesta(true, "Las calorÃ­as por porciÃ³n deben ser mayores a 0.");
        }

        return AlimentoImp.editarAlimento(alimento);
    }
}
