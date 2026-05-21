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
import pojo.Paciente;
import utilidades.Seguridad;

public class PacienteImp {

    public static Respuesta registrarPaciente(Paciente paciente) {
        Respuesta respuesta = new Respuesta();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                // Validar que el correo no esté ya registrado
                if (existeEmail(conexionBD, paciente.getEmail(), 0)) {
                    respuesta.setError(true);
                    respuesta.setMensaje("El correo electrónico ya se encuentra registrado.");
                    return respuesta;
                }

                // Hashear el código de acceso (NIP 4 dígitos → SHA-256)
                String codigoHash = Seguridad.hashear(paciente.getCodigoAcceso());
                paciente.setCodigoAcceso(codigoHash);

                // El estatus por defecto es Activo
                paciente.setEstatus(1);

                int filas = conexionBD.insert("paciente.registrar", paciente);
                conexionBD.commit();

                if (filas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje("Paciente registrado exitosamente.");
                } else {
                    respuesta.setError(true);
                    respuesta.setMensaje("No se pudo registrar al paciente.");
                }

            } catch (Exception e) {
                conexionBD.rollback();
                e.printStackTrace();
                respuesta.setError(true);
                respuesta.setMensaje(manejarErrorBD(e));
            } finally {
                conexionBD.close();
            }
        } else {
            respuesta.setError(true);
            respuesta.setMensaje("No hay conexión con la base de datos.");
        }
        return respuesta;
    }
    
    public static Respuesta actualizarPaciente(Paciente paciente) {
        Respuesta respuesta = new Respuesta();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                // Verificar que el paciente exista y esté activo
                Paciente existente = conexionBD.selectOne("paciente.obtenerPorId", paciente.getIdPaciente());
                if (existente == null) {
                    respuesta.setError(true);
                    respuesta.setMensaje("El paciente no existe.");
                    return respuesta;
                }

                // Validar que el nuevo email no esté siendo usado por OTRO paciente
                if (existeEmail(conexionBD, paciente.getEmail(), paciente.getIdPaciente())) {
                    respuesta.setError(true);
                    respuesta.setMensaje("El correo electrónico ya está registrado por otro paciente.");
                    return respuesta;
                }

                // Si viene un nuevo código de acceso, hashearlo
                // Si no viene (null o vacío), conservar el que ya está en BD
                if (paciente.getCodigoAcceso() != null && !paciente.getCodigoAcceso().trim().isEmpty()) {
                    String codigoHash = Seguridad.hashear(paciente.getCodigoAcceso());
                    paciente.setCodigoAcceso(codigoHash);
                } else {
                    // Preservar el codigo_acceso actual sin modificarlo
                    paciente.setCodigoAcceso(existente.getCodigoAcceso());
                }

                int filas = conexionBD.update("paciente.actualizar", paciente);
                conexionBD.commit();

                if (filas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje("Paciente actualizado correctamente.");
                } else {
                    respuesta.setError(true);
                    respuesta.setMensaje("No se realizaron cambios.");
                }

            } catch (Exception e) {
                conexionBD.rollback();
                e.printStackTrace();
                respuesta.setError(true);
                respuesta.setMensaje(manejarErrorBD(e));
            } finally {
                conexionBD.close();
            }
        } else {
            respuesta.setError(true);
            respuesta.setMensaje("No hay conexión con la base de datos.");
        }
        return respuesta;
    }

    // ── Helpers ─────────────────────────────────────────────────────────────

    private static boolean existeEmail(SqlSession sesion, String email, int idPaciente) {
        Map<String, Object> params = new HashMap<>();
        params.put("email", email);
        params.put("idPaciente", idPaciente);
        int count = sesion.selectOne("paciente.existeEmail", params);
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
}
