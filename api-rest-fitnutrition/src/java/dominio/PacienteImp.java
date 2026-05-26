package dominio;

import dto.RSAutenticacionPaciente;
import dto.Respuesta;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;

import pojo.Dieta;
import pojo.Medico;
import pojo.Paciente;
import pojo.ProgresoPaciente;
import utilidades.Constantes;
import utilidades.Seguridad;
import utilidades.Validaciones;

public class PacienteImp {

    public static Respuesta registrarPaciente(Paciente paciente) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);
        SqlSession conexion = MyBatisUtil.getSession();

        if (conexion != null) {
            try {
                String errorTelefono = Validaciones.validarTelefono(paciente.getTelefono(), true);
                if (errorTelefono != null) {
                    respuesta.setMensaje(errorTelefono);
                    return respuesta;
                }

                String errorCorreo = Validaciones.validarCorreo(paciente.getEmail(), true);
                if (errorCorreo != null) {
                    respuesta.setMensaje(errorCorreo);
                    return respuesta;
                }

                // CORRECCIÓN: armar el map igual que en actualizarEmail
                if (existeEmail(conexion, paciente.getEmail(), 0)) {
                    respuesta.setMensaje("El correo electrónico ya se encuentra registrado.");
                    return respuesta;
                }

                paciente.setCodigoAcceso(Seguridad.hashear(paciente.getCodigoAcceso()));
                paciente.setEstatus(1);

                int filas = conexion.insert("paciente.registrar", paciente);

                if (filas > 0) {
                    conexion.commit();
                    respuesta.setError(false);
                    respuesta.setMensaje("Paciente registrado exitosamente.");
                } else {
                    respuesta.setMensaje("No se pudo registrar al paciente.");
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

    // ── Admin ────────────────────────────────────────────────────────────────
    public static Respuesta actualizarPaciente(Paciente paciente) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);
        SqlSession conexion = MyBatisUtil.getSession();

        if (conexion != null) {
            try {
                if (!existePaciente(conexion, paciente.getIdPaciente())) {
                    respuesta.setMensaje("El paciente que intenta editar no existe.");
                    return respuesta;
                }

                String errorTel = Validaciones.validarTelefono(paciente.getTelefono(), false);
                if (errorTel != null) {
                    respuesta.setMensaje(errorTel);
                    return respuesta;
                }

                int filasAfectadas = conexion.update("paciente.actualizar", paciente);

                if (filasAfectadas > 0) {
                    conexion.commit();
                    respuesta.setError(false);
                    respuesta.setMensaje("Paciente actualizado correctamente.");
                } else {
                    respuesta.setMensaje("No se pudo actualizar la información del paciente.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                respuesta.setMensaje("Error interno al editar el paciente.");
            } finally {
                conexion.close();
            }
        } else {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }
        return respuesta;
    }

    public static Respuesta darDeBajaPaciente(int idPaciente) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);
        SqlSession conexion = MyBatisUtil.getSession();

        if (conexion != null) {
            try {
                Paciente existente = conexion.selectOne("paciente.obtenerPorId", idPaciente);

                if (existente == null) {
                    respuesta.setMensaje("El paciente no existe.");
                    return respuesta;
                }
                if (existente.getEstatus() == 0) {
                    respuesta.setMensaje("El paciente ya se encuentra dado de baja.");
                    return respuesta;
                }

                int filas = conexion.update("paciente.darDeBaja", idPaciente);

                if (filas > 0) {
                    conexion.commit();
                    respuesta.setError(false);
                    respuesta.setMensaje("Paciente dado de baja correctamente.");
                } else {
                    respuesta.setMensaje("No se pudo dar de baja al paciente.");
                }
            } catch (Exception e) {
                conexion.rollback();
                e.printStackTrace();
                respuesta.setMensaje("Error inesperado en la base de datos.");
            } finally {
                conexion.close();
            }
        } else {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }
        return respuesta;
    }

    public static List<Paciente> buscarPacientes(String criterio, Integer idMedico) {
        List<Paciente> pacientes = null;
        SqlSession conexion = MyBatisUtil.getSession();

        if (conexion != null) {
            try {
                Map<String, Object> params = new HashMap<>();
                params.put("criterio", criterio);
                params.put("idMedico", idMedico); // null o 0 → admin, sin filtro
                pacientes = conexion.selectList("paciente.buscar", params);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexion.close();
            }
        }
        return pacientes;
    }

    // ── Móvil ────────────────────────────────────────────────────────────────
    public static RSAutenticacionPaciente loginPaciente(String email, String nip) {
        RSAutenticacionPaciente respuesta = new RSAutenticacionPaciente();
        respuesta.setError(true);

        SqlSession conexion = MyBatisUtil.getSession();

        if (conexion != null) {
            try {

                Map<String, Object> params = new HashMap<>();
                params.put("email", email);
                params.put("codigoAcceso", Seguridad.hashear(nip));

                Paciente paciente = conexion.selectOne("paciente.login", params);

                if (paciente == null) {
                    respuesta.setMensaje("Correo o NIP incorrectos.");
                    return respuesta;
                }

                if (paciente.getEstatus() == 0) {
                    respuesta.setMensaje("La cuenta está inactiva. Contacte a su médico.");
                    return respuesta;
                }

                respuesta.setError(false);
                respuesta.setMensaje("Autenticación exitosa.");
                respuesta.setPaciente(paciente);

            } catch (Exception e) {
                e.printStackTrace();
                respuesta.setError(true);
                respuesta.setMensaje("Error interno al autenticar.");
            } finally {
                conexion.close();
            }
        } else {
            respuesta.setError(true);
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }

    public static Paciente obtenerPerfilPaciente(int idPaciente) {
        Paciente perfil = null;
        SqlSession conexion = MyBatisUtil.getSession();

        if (conexion != null) {
            try {
                perfil = conexion.selectOne("paciente.obtenerPerfil", idPaciente);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexion.close();
            }
        }
        return perfil;
    }

    public static Respuesta editarPerfilPaciente(Paciente paciente) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);
        SqlSession conexion = MyBatisUtil.getSession();

        if (conexion != null) {
            try {
                if (!existePaciente(conexion, paciente.getIdPaciente())) {
                    respuesta.setMensaje("El paciente no existe.");
                    return respuesta;
                }

                String errorTel = Validaciones.validarTelefono(paciente.getTelefono(), false);
                if (errorTel != null) {
                    respuesta.setMensaje(errorTel);
                    return respuesta;
                }

                int filasAfectadas = conexion.update("paciente.editarPerfil", paciente);

                if (filasAfectadas > 0) {
                    conexion.commit();
                    respuesta.setError(false);
                    respuesta.setMensaje("Perfil actualizado correctamente.");
                } else {
                    // filasAfectadas == 0 puede significar que el estatus es 0
                    respuesta.setMensaje("No se pudo actualizar el perfil. Verifique que la cuenta esté activa.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                respuesta.setMensaje("Error interno al actualizar el perfil.");
            } finally {
                conexion.close();
            }
        } else {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }
        return respuesta;
    }

    public static Respuesta actualizarEmail(int idPaciente, String emailActual, String nuevoEmail) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);
        SqlSession conexion = MyBatisUtil.getSession();

        if (conexion != null) {
            try {
                Paciente existente = conexion.selectOne("paciente.obtenerPorId", idPaciente);
                if (existente == null) {
                    respuesta.setMensaje("El paciente no existe.");
                    return respuesta;
                }

                if (!existente.getEmail().equalsIgnoreCase(emailActual)) {
                    respuesta.setMensaje("El correo actual ingresado no es correcto.");
                    return respuesta;
                }

                if (existente.getEmail().equalsIgnoreCase(nuevoEmail)) {
                    respuesta.setMensaje("El nuevo correo electrónico es igual al actual.");
                    return respuesta;
                }

                String errorFormato = Validaciones.validarCorreo(nuevoEmail, true);
                if (errorFormato != null) {
                    respuesta.setMensaje(errorFormato);
                    return respuesta;
                }

                if (existeEmail(conexion, nuevoEmail, idPaciente)) {
                    respuesta.setMensaje("El correo electrónico ya está registrado por otro paciente.");
                    return respuesta;
                }

                Map<String, Object> params = new HashMap<>();
                params.put("idPaciente", idPaciente);
                params.put("nuevoEmail", nuevoEmail);

                int filasAfectadas = conexion.update("paciente.actualizarEmail", params);

                if (filasAfectadas > 0) {
                    conexion.commit();
                    respuesta.setError(false);
                    respuesta.setMensaje("Correo electrónico actualizado correctamente.");
                } else {
                    respuesta.setMensaje("No se pudo actualizar el correo electrónico.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                respuesta.setMensaje("Error interno al actualizar el correo.");
            } finally {
                conexion.close();
            }
        } else {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }
        return respuesta;
    }

    public static Respuesta actualizarNip(int idPaciente, String nipActual, String nuevoNip) {
        Respuesta respuesta = new Respuesta();
        SqlSession conexion = MyBatisUtil.getSession();

        if (conexion != null) {
            try {

                if (!Validaciones.esNumericoConLongitud(nuevoNip, 4)) {
                    respuesta.setError(true);
                    respuesta.setMensaje("El nuevo NIP debe ser de exactamente 4 dígitos numéricos.");
                    return respuesta;
                }

                Paciente paciente = conexion.selectOne("paciente.obtenerPorId", idPaciente);

                if (paciente == null) {
                    respuesta.setError(true);
                    respuesta.setMensaje("El paciente no existe.");
                    return respuesta;
                }

                String hashActual = Seguridad.hashear(nipActual);
                String hashNuevo = Seguridad.hashear(nuevoNip);

                if (hashActual.equals(hashNuevo)) {
                    respuesta.setError(true);
                    respuesta.setMensaje("El nuevo NIP no puede ser igual al actual.");
                    return respuesta;
                }

                Map<String, Object> params = new HashMap<>();
                params.put("idPaciente", idPaciente);
                params.put("nipActual", hashActual);
                params.put("nuevoNip", hashNuevo);

                int filas = conexion.update("paciente.actualizarNip", params);
                conexion.commit();

                if (filas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje("NIP actualizado correctamente.");
                } else {
                    respuesta.setError(true);
                    respuesta.setMensaje("El NIP actual es incorrecto.");
                }

            } catch (Exception e) {
                conexion.rollback();
                e.printStackTrace();

                respuesta.setError(true);
                respuesta.setMensaje("Error al cambiar el NIP.");

            } finally {
                conexion.close();
            }
        } else {
            respuesta.setError(true);
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }

    // ── Helpers ──────────────────────────────────────────────────────────────
    private static boolean existePaciente(SqlSession conexion, int idPaciente) {
        return conexion.selectOne("paciente.obtenerPorId", idPaciente) != null;
    }

    private static boolean existeEmail(SqlSession conexion, String email, int idPaciente) {
        Map<String, Object> params = new HashMap<>();
        params.put("email", email);
        params.put("idPaciente", idPaciente);
        int count = conexion.selectOne("paciente.existeEmail", params);
        return count > 0;
    }

    private static String manejarErrorBD(Exception e) {
        String msg = e.getMessage();
        if (msg != null) {
            if (msg.contains("uq_paciente_email")) {
                return "El correo electrónico ya está registrado.";
            }
            if (msg.contains("chk_paciente_telefono")) {
                return "El teléfono debe contener exactamente 10 dígitos.";
            }
            if (msg.contains("fk_paciente_medico")) {
                return "El médico indicado no existe.";
            }
        }
        return "Error inesperado en la base de datos.";
    }

    //T330 - Consultar dietas y progreso (app móvil)
    public static List<ProgresoPaciente> obtenerProgresoPaciente(
            int idPaciente) {

        List<ProgresoPaciente> progreso = null;

        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {

            try {

                progreso = conexionBD.selectList(
                        "paciente.obtener-progreso-paciente",
                        idPaciente
                );

            } catch (Exception e) {

                e.printStackTrace();

            } finally {

                conexionBD.close();
            }
        }

        return progreso;
    }

    public static List<Dieta> obtenerDietasPaciente(
            int idPaciente) {

        List<Dieta> dietas = null;

        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {

            try {

                dietas = conexionBD.selectList(
                        "paciente.obtener-dietas-paciente",
                        idPaciente
                );

            } catch (Exception e) {

                e.printStackTrace();

            } finally {

                conexionBD.close();
            }
        }

        return dietas;
    }
    
    
    // ----------------------------------------------------------------
    // Guardar (subir) fotografía del paciente
    // ----------------------------------------------------------------
    public static Respuesta guardarFotografia(int idPaciente, byte[] fotografia) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);
 
        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD != null) {
            try {
                Paciente paciente = new Paciente();
                paciente.setIdPaciente(idPaciente);
                paciente.setFotografia(fotografia);
 
                int filasAfectadas = conexionBD.update("paciente.guardar-fotografia", paciente);
                conexionBD.commit();
 
                if (filasAfectadas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje("La fotografía del paciente ha sido guardada correctamente");
                } else {
                    respuesta.setMensaje("La fotografía del paciente no ha sido guardada, inténtelo más tarde");
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
    // Obtener fotografía del paciente (devuelve fotoBase64)
    // ----------------------------------------------------------------
    public static Paciente obtenerFotografia(int idPaciente) {
        Paciente paciente = new Paciente();
 
        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD != null) {
            try {
                paciente = conexionBD.selectOne("paciente.obtener-fotografia", idPaciente);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
 
        return paciente;
    }
}
