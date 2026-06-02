/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;

import dto.Respuesta;
import java.util.ArrayList;
import java.util.List;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Alimento;
import utilidades.Constantes;

/**
 * Lógica de negocio para operaciones sobre alimentos.
 */
public class AlimentoImp {

    /**
     * T321: Registrar nuevo alimento en el catálogo.
     * Si la base de datos falla, se hace rollback y se retorna MSJ_ERROR_BD.
     * 
     * @param alimento objeto con nombre, porción y calorías
     * @return Respuesta con error=false si el registro fue exitoso
     */
    public static Respuesta registrarAlimento(Alimento alimento) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD == null) {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
            return respuesta;
        }

        try {
            int filasAfectadas = conexionBD.insert("alimento.registrarAlimento", alimento);
            if (filasAfectadas > 0) {
                conexionBD.commit();
                respuesta.setError(false);
                respuesta.setMensaje("Alimento registrado exitosamente.");
            } else {
                conexionBD.rollback();
                respuesta.setMensaje("No fue posible registrar el alimento.");
            }
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
     * T322: Buscar alimentos por nombre.
     * Retorna coincidencias parciales.
     * Sin resultados retorna lista vacía.
     * 
     * @param nombre texto a buscar
     * @return lista de objetos Alimento
     */
    public static List<Alimento> buscarPorNombre(String nombre) {
        List<Alimento> alimentos = new ArrayList<>();
        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD != null) {
            try {
                alimentos = conexionBD.selectList("alimento.buscarPorNombre", nombre);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        return alimentos;
    }

    /**
     * T323: Editar alimento.
     * Si las calorías cambian, se llama al SP-2 para recalcular las dietas.
     * 
     * @param alimentoModificado objeto con los datos actualizados
     * @return Respuesta con error=false si fue exitoso
     */
    public static Respuesta editarAlimento(Alimento alimentoModificado) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD == null) {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
            return respuesta;
        }

        try {
            // 1. Obtener alimento actual para comparar si cambiaron las calorías
            Alimento alimentoActual = conexionBD.selectOne("alimento.obtenerPorId", alimentoModificado.getIdAlimento());
            if (alimentoActual == null) {
                conexionBD.rollback();
                respuesta.setMensaje("El alimento no existe.");
                return respuesta;
            }
            
            boolean caloriasCambiaron = (alimentoActual.getCaloriasPorcion() != alimentoModificado.getCaloriasPorcion());

            // 2. Ejecutar UPDATE
            int filasAfectadas = conexionBD.update("alimento.editarAlimento", alimentoModificado);
            if (filasAfectadas <= 0) {
                conexionBD.rollback();
                respuesta.setMensaje("No fue posible editar el alimento.");
                return respuesta;
            }

            // 3. Llamar a SP-2 si las calorías cambiaron
            if (caloriasCambiaron) {
                conexionBD.update("dietaAlimento.recalcularDietasPorAlimento", alimentoModificado.getIdAlimento());
            }

            conexionBD.commit();
            respuesta.setError(false);
            respuesta.setMensaje("Alimento editado exitosamente.");
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
