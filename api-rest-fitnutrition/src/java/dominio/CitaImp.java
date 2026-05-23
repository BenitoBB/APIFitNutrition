/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;

import dto.Respuesta;
import java.util.HashMap;
import java.util.Map;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Cita;
import utilidades.Constantes;

/**
 *
 * @author julia
 */
public class CitaImp {
    public static Cita buscarCitaPorPacienteYFecha(int idPaciente, String fechaCita) {
        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD != null) {
            try {
                Map<String, Object> parametros = new HashMap<>();
                parametros.put("idPaciente", idPaciente);
                parametros.put("fechaCita", fechaCita);
                return conexionBD.selectOne("cita.buscarCitaPorPacienteYFecha", parametros);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        return null;
    }
    
    public static Respuesta crearCita(Cita cita) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD == null) {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
            return respuesta;
        }

        try {
            int filasAfectadas = conexionBD.insert("cita.crearCita", cita);
            if (filasAfectadas > 0) {
                conexionBD.commit();
                respuesta.setError(false);
                respuesta.setMensaje("Cita confirmada exitosamente.");
            } else {
                respuesta.setMensaje("No fue posible registrar la cita.");
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
}
