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
import pojo.Cita;
import pojo.CitaDetalle;
import pojo.CitaMobil;
import utilidades.Constantes;
import utilidades.Validaciones;

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
    
    public static Cita buscarCitaPorId(int idCita) {
        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD != null) {
            try {
                return conexionBD.selectOne("cita.buscarCitaPorId", idCita);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        return null;
    }
    
    public static Respuesta modificarCita(Cita cita) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD == null) {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
            return respuesta;
        }
        try {
            int filasAfectadas = conexionBD.update("cita.modificarCita", cita);
            if (filasAfectadas > 0) {
                conexionBD.commit();
                respuesta.setError(false);
                respuesta.setMensaje("Cita modificada exitosamente.");
            } else {
                respuesta.setMensaje("No fue posible modificar la cita.");
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
    
    public static Respuesta reagendarCita(Cita cita) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD == null) {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
            return respuesta;
        }
        try {
            int filasAfectadas = conexionBD.update("cita.reagendarCita", cita);
            if (filasAfectadas > 0) {
                conexionBD.commit();
                respuesta.setError(false);
                respuesta.setMensaje("Cita reagendada exitosamente.");
            } else {
                respuesta.setMensaje("No fue posible reagendar la cita.");
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
    
    
    // ── CARD 29: Móvil ────────────────────────────────────────────────────────

    public static List<CitaMobil> obtenerCitasPaciente(int idPaciente) {
        List<CitaMobil> citas = new ArrayList<>();
        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD != null) {
            try {
                citas = conexionBD.selectList("cita.obtenerCitasPaciente", idPaciente);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        return citas;
    }

    public static Respuesta cancelarCitaPaciente(int idCita, int idPaciente, String motivoCancelacion) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD == null) {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
            return respuesta;
        }

        try {
            // 1. Obtener la cita para verificar propiedad y fecha
            Cita cita = conexionBD.selectOne("cita.buscarCitaPorId", idCita);

            if (cita == null) {
                respuesta.setMensaje("La cita no existe.");
                return respuesta;
            }

            // Verificar que la cita pertenece al paciente
            if (cita.getIdPaciente() != idPaciente) {
                respuesta.setMensaje("No tienes permiso para cancelar esta cita.");
                return respuesta;
            }

            // Verificar que la cita no esté ya cancelada
            if ("Cancelada".equals(cita.getEstatus())) {
                respuesta.setMensaje("Esta cita ya fue cancelada.");
                return respuesta;
            }

            // RN-09: fecha_cita >= hoy+1 para poder cancelar desde móvil
            if (!Validaciones.esFechaCitaValida(cita.getFechaCita())) {
                respuesta.setMensaje("Solo puedes cancelar citas con al menos 1 día de antelación.");
                return respuesta;
            }

            // 2. Ejecutar la cancelación
            Cita citaActualizar = new Cita();
            citaActualizar.setIdCita(idCita);
            citaActualizar.setMotivoCancelacion(motivoCancelacion);

            int filasAfectadas = conexionBD.update("cita.cancelarCita", citaActualizar);
            if (filasAfectadas > 0) {
                conexionBD.commit();
                respuesta.setError(false);
                respuesta.setMensaje("Cita cancelada exitosamente.");
            } else {
                respuesta.setMensaje("No fue posible cancelar la cita.");
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
