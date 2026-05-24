/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;

import dto.Respuesta;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.CategoriaHorario;
import pojo.Dieta;
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
}
