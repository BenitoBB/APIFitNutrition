package dominio;

import dto.Respuesta;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Medico;
import utilidades.Constantes;
import utilidades.Seguridad;
import utilidades.Validaciones;

public class MedicoImp {

    public static Respuesta registrarMedico(Medico medico) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);
        SqlSession conexion = MyBatisUtil.getSession();

        if (conexion != null) {
            try {
                // No personal único (idMedico = 0 porque aún no existe)
                if (existeNoPersonal(conexion, medico.getNoPersonal(), 0)) {
                    respuesta.setMensaje("El número de personal ya se encuentra registrado.");
                    return respuesta;
                }

                if (existeCedula(conexion, medico.getCedulaProfesional(), 0)) {
                    respuesta.setMensaje("La cédula profesional ya se encuentra registrada.");
                    return respuesta;
                }

                medico.setContrasena(Seguridad.hashear(medico.getContrasena()));
                medico.setEstatus(1);

                int filas = conexion.insert("medico.registrar", medico);

                if (filas > 0) {
                    conexion.commit();
                    respuesta.setError(false);
                    respuesta.setMensaje("Médico registrado correctamente.");
                } else {
                    respuesta.setMensaje("No se pudo registrar al médico.");
                }
            } catch (Exception e) {
                conexion.rollback();
                e.printStackTrace();
                respuesta.setMensaje(manejarErrorBD(e));
            } finally {
                conexion.close();
            }
        } else {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }
        return respuesta;
    }
    
    public static Respuesta editarMedico(Medico medico) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);
        SqlSession conexion = MyBatisUtil.getSession();

        if (conexion != null) {
            try {
                if (!existeMedico(conexion, medico.getIdMedico())) {
                    respuesta.setMensaje("El médico que intenta editar no existe.");
                    return respuesta;
                }

                // Validar unicidad excluyendo al propio médico
                if (existeNoPersonal(conexion, medico.getNoPersonal(), medico.getIdMedico())) {
                    respuesta.setMensaje("El número de personal ya está registrado por otro médico.");
                    return respuesta;
                }

                if (existeCedula(conexion, medico.getCedulaProfesional(), medico.getIdMedico())) {
                    respuesta.setMensaje("La cédula profesional ya está registrada por otro médico.");
                    return respuesta;
                }

                int filas = conexion.update("medico.editar", medico);

                if (filas > 0) {
                    conexion.commit();
                    respuesta.setError(false);
                    respuesta.setMensaje("Médico actualizado correctamente.");
                } else {
                    respuesta.setMensaje("No se pudo actualizar la información del médico.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                respuesta.setMensaje(manejarErrorBD(e));
            } finally {
                conexion.close();
            }
        } else {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }
        return respuesta;
    }
    
    public static Respuesta cambiarContrasena(int idMedico, String contrasenaActual, String contrasenaNueva) {
        Respuesta respuesta = new Respuesta();
        SqlSession conexion = MyBatisUtil.getSession();

        if (conexion != null) {
            try {
                Map<String, Object> params = new HashMap<>();
                params.put("idMedico",         idMedico);
                params.put("contrasenaActual",  Seguridad.hashear(contrasenaActual));
                params.put("contrasenaNueva",   Seguridad.hashear(contrasenaNueva));

                int filas = conexion.update("medico.cambiarContrasena", params);
                conexion.commit();

                if (filas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje("Contraseña actualizada correctamente.");
                } else {
                    respuesta.setError(true);
                    respuesta.setMensaje("La contraseña actual es incorrecta.");
                }
            } catch (Exception e) {
                conexion.rollback();
                e.printStackTrace();
                respuesta.setError(true);
                respuesta.setMensaje("Error al cambiar la contraseña.");
            } finally {
                conexion.close();
            }
        } else {
            respuesta.setError(true);
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }
        return respuesta;
    }
    
    public static List<Medico> buscarMedicos(String criterio) {
        List<Medico> medicos = new ArrayList<>();
        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD != null) {
            try {
                Map<String, Object> parametros = new HashMap<>();
                parametros.put("criterio",
                        (criterio != null && !criterio.trim().isEmpty())
                        ? criterio.trim()
                        : null
                );
                medicos = conexionBD.selectList("medico.buscarMedicos", parametros);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        return medicos;
    }
    
    public static Respuesta bajaMedicoReasignar(int idMedicoBaja, int idMedicoNuevo) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        if (idMedicoBaja == idMedicoNuevo) {
            respuesta.setMensaje("El medico destino debe ser distinto al medico a dar de baja.");
            return respuesta;
        }

        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD == null) {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
            return respuesta;
        }

        try {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("idMedicoBaja", idMedicoBaja);
            parametros.put("idMedicoNuevo", idMedicoNuevo);

            Integer medicoBajaValido = conexionBD.selectOne("medico.validarMedicoBaja", idMedicoBaja);
            if (medicoBajaValido == null || medicoBajaValido == 0) {
                respuesta.setMensaje("El medico a dar de baja no existe, esta inactivo o es administrador.");
                return respuesta;
            }

            Integer medicoDestinoValido = conexionBD.selectOne("medico.validarMedicoDestino", idMedicoNuevo);
            if (medicoDestinoValido == null || medicoDestinoValido == 0) {
                respuesta.setMensaje("El medico destino no existe, esta inactivo o es administrador.");
                return respuesta;
            }

            conexionBD.insert("medico.registrarHistorialReasignacionBaja", parametros);
            conexionBD.update("medico.reasignarPacientesActivosMedico", parametros);

            int filasAfectadas = conexionBD.update("medico.bajaLogicaMedico", idMedicoBaja);
            if (filasAfectadas > 0) {
                conexionBD.commit();
                respuesta.setError(false);
                respuesta.setMensaje("Medico dado de baja y pacientes reasignados exitosamente.");
            } else {
                conexionBD.rollback();
                respuesta.setMensaje("No fue posible dar de baja al medico.");
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
    
    public static Respuesta guardarFotografia(int idMedico, byte[] fotografia) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);
 
        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD != null) {
            try {
                Medico medico = new Medico();
                medico.setIdMedico(idMedico);
                medico.setFotografia(fotografia);
 
                int filasAfectadas = conexionBD.update("medico.guardar-fotografia", medico);
                conexionBD.commit();
 
                if (filasAfectadas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje("La fotografía del médico ha sido guardada correctamente");
                } else {
                    respuesta.setMensaje("La fotografía del médico no ha sido guardada, inténtelo más tarde");
                }
            } catch (Exception e) {
                respuesta.setMensaje(e.getMessage());
            } finally {
                conexionBD.close();
            }
        } else {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }
 
        return respuesta;
    }
 
    // ----------------------------------------------------------------
    // Obtener fotografía del médico (devuelve fotoBase64)
    // ----------------------------------------------------------------
    public static Medico obtenerFotografia(int idMedico) {
        Medico medico = new Medico();
 
        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD != null) {
            try {
                medico = conexionBD.selectOne("medico.obtener-fotografia", idMedico);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
 
        return medico;
    }
    
    // ── Helpers ──────────────────────────────────────────────────────────────

    private static boolean existeMedico(SqlSession conexion, int idMedico) {
        return conexion.selectOne("medico.obtenerPorId", idMedico) != null;
    }

    private static boolean existeNoPersonal(SqlSession conexion, String noPersonal, int idMedico) {
        Map<String, Object> params = new HashMap<>();
        params.put("noPersonal", noPersonal);
        params.put("idMedico",   idMedico);
        int count = conexion.selectOne("medico.existeNoPersonal", params);
        return count > 0;
    }

    private static boolean existeCedula(SqlSession conexion, String cedula, int idMedico) {
        Map<String, Object> params = new HashMap<>();
        params.put("cedulaProfesional", cedula);
        params.put("idMedico",          idMedico);
        int count = conexion.selectOne("medico.existeCedula", params);
        return count > 0;
    }

    private static String manejarErrorBD(Exception e) {
        String msg = e.getMessage();
        if (msg != null) {
            if (msg.contains("uq_medico_no_personal")) return "El número de personal ya está registrado.";
        }
        return "Error inesperado en la base de datos.";
    }
}