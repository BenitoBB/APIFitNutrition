/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;

import dto.Respuesta;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.AlimentoEnDieta;
import pojo.CategoriaConAlimentos;
import pojo.Dieta;
import pojo.DietaDetalle;
import pojo.DietaResumen;
import utilidades.Constantes;

/**
 * Lógica de negocio para operaciones sobre dietas y sus categorías de horario.
 */
public class DietaImp {

    /**
     * Crea una nueva dieta con sus categorías de horario.
     * T317: INSERT en dieta primero; usar id_dieta resultante para INSERTs en categoria_horario.
     * Si BD falla → rollback de todos los INSERTs + MSJ_ERROR_BD.
     *
     * @param dieta      objeto con nombre, observaciones e idMedico; total_calorias inicia en 0.00
     * @param categorias lista de nombres de categorías (mínimo 1, RN-16)
     * @return Respuesta con error=false si todo fue exitoso, error=true + mensaje en caso contrario
     */
    public static Respuesta crearDieta(Dieta dieta, List<String> categorias) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD == null) {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
            return respuesta;
        }

        try {
            // 1. INSERT en dieta — total_calorias inicia en 0.00 (definido en el mapper)
            int filasAfectadas = conexionBD.insert("dieta.crearDieta", dieta);
            if (filasAfectadas <= 0) {
                respuesta.setMensaje("No fue posible registrar la dieta.");
                return respuesta;
            }

            // 2. Usar id_dieta generado para los INSERTs en categoria_horario (RN-16, RN-17)
            int idDieta = dieta.getIdDieta();
            for (String nombreCategoria : categorias) {
                Map<String, Object> parametrosCategoria = new HashMap<>();
                parametrosCategoria.put("idDieta", idDieta);
                parametrosCategoria.put("nombreCategoria", nombreCategoria.trim());

                int filasCategoria = conexionBD.insert("categoriaHorario.crearCategoriaHorario", parametrosCategoria);
                if (filasCategoria <= 0) {
                    conexionBD.rollback();
                    respuesta.setMensaje("No fue posible registrar la categoría de horario: " + nombreCategoria);
                    return respuesta;
                }
            }

            conexionBD.commit();
            respuesta.setError(false);
            respuesta.setMensaje("Dieta creada exitosamente.");

        } catch (Exception e) {
            conexionBD.rollback();
            e.printStackTrace();
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        } finally {
            conexionBD.close();
        }

        return respuesta;
    }

    /**
     * Retorna el listado de todas las dietas disponibles.
     * T318: GET /api/dieta/obtener-todas
     * Sin resultados → lista vacía (no error).
     *
     * @return lista de DietaResumen; vacía si no hay dietas
     */
    public static List<DietaResumen> obtenerTodas() {
        List<DietaResumen> dietas = new ArrayList<>();
        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD != null) {
            try {
                dietas = conexionBD.selectList("dieta.obtenerTodas");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        return dietas;
    }

    /**
     * Retorna el detalle completo de una dieta: info base + categorías + alimentos por categoría.
     * T318: GET /api/dieta/{id}
     * Construye el árbol en Java reutilizando la misma sesión abierta.
     *
     * @param idDieta identificador de la dieta
     * @return DietaDetalle o null si no existe
     */
    public static DietaDetalle obtenerDetallePorId(int idDieta) {
        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD == null) {
            return null;
        }

        try {
            // 1. Datos base de la dieta (incluye editable de la vista)
            DietaResumen base = conexionBD.selectOne("dieta.obtenerPorId", idDieta);
            if (base == null) {
                return null;
            }

            // 2. Categorías de horario de la dieta
            List<CategoriaConAlimentos> categorias = conexionBD.selectList(
                    "categoriaHorario.obtenerCategoriasPorDieta", idDieta);

            // 3. Alimentos por cada categoría
            for (CategoriaConAlimentos categoria : categorias) {
                List<AlimentoEnDieta> alimentos = conexionBD.selectList(
                        "categoriaHorario.obtenerAlimentosPorCategoria", categoria.getIdCategoria());
                categoria.setAlimentos(alimentos);
            }

            // 4. Construir el objeto de respuesta
            DietaDetalle detalle = new DietaDetalle();
            detalle.setIdDieta(base.getIdDieta());
            detalle.setNombreDieta(base.getNombreDieta());
            detalle.setTotalCalorias(base.getTotalCalorias());
            detalle.setObservaciones(base.getObservaciones());
            detalle.setIdMedico(base.getIdMedico());
            detalle.setEditable(base.getEditable());
            detalle.setCategorias(categorias);

            return detalle;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            conexionBD.close();
        }
    }
}
