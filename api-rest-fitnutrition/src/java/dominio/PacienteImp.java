/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;

import dto.PacienteUpdateRequest;
import dto.Respuesta;
import java.util.HashMap;
import java.util.Map;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Paciente;
import utilidades.Seguridad;
import utilidades.Validaciones;

public class PacienteImp {

    public static Respuesta registrarPaciente(Paciente paciente) {
        Respuesta respuesta = new Respuesta();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                // Validar formato de teléfono (obligatorio en registro)
                String errorTelefono = Validaciones.validarTelefono(paciente.getTelefono(), true);
                if (errorTelefono != null) {
                    respuesta.setError(true);
                    respuesta.setMensaje(errorTelefono);
                    return respuesta;
                }

                // Validar formato de correo (obligatorio en registro)
                String errorCorreo = Validaciones.validarCorreo(paciente.getEmail(), true);
                if (errorCorreo != null) {
                    respuesta.setError(true);
                    respuesta.setMensaje(errorCorreo);
                    return respuesta;
                }
                
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
    
    public static Respuesta actualizarPaciente(PacienteUpdateRequest req) {
        Respuesta respuesta = new Respuesta();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {

                // 1. Verificar que el paciente exista
                Paciente existente = conexionBD.selectOne(
                        "paciente.obtenerPorId", req.getIdPaciente());
                if (existente == null) {
                    respuesta.setError(true);
                    respuesta.setMensaje("El paciente no existe.");
                    return respuesta;
                }

                // 2. Validar teléfono si viene en la petición
                String errorTelefono = Validaciones.validarTelefono(req.getTelefono(), false);
                if (errorTelefono != null) {
                    respuesta.setError(true);
                    respuesta.setMensaje(errorTelefono);
                    return respuesta;
                }

                // 3. Validar formato de correo
                String errorCorreo = Validaciones.validarCorreo(req.getEmail(), true);
                if (errorCorreo != null) {
                    respuesta.setError(true);
                    respuesta.setMensaje(errorCorreo);
                    return respuesta;
                }
                
                // 3. Validar unicidad de email (excluir al propio paciente)
                if (existeEmail(conexionBD, req.getEmail(), req.getIdPaciente())) {
                    respuesta.setError(true);
                    respuesta.setMensaje("El correo electrónico ya está registrado por otro paciente.");
                    return respuesta;
                }

                // 4. Gestionar domicilio solo si vienen los campos completos
                Integer idDomicilioFinal = existente.getIdDomicilio(); // conservar por defecto

                if (req.tieneDomicilio()) {
                    Map<String, Object> paramsDom = new HashMap<>();
                    paramsDom.put("calle",     req.getCalle());
                    paramsDom.put("numero",    req.getNumero());
                    paramsDom.put("idColonia", req.getIdColonia());

                    if (existente.getIdDomicilio() != null) {
                        // El paciente ya tiene domicilio → solo actualizar
                        paramsDom.put("idDomicilio", existente.getIdDomicilio());
                        conexionBD.update("domicilio.actualizar", paramsDom);
                        idDomicilioFinal = existente.getIdDomicilio();
                    } else {
                        // El paciente no tenía domicilio → insertar y capturar la llave generada
                        conexionBD.insert("domicilio.insertar", paramsDom);
                        idDomicilioFinal = (Integer) paramsDom.get("idDomicilio");
                    }
                }
                // Si !req.tieneDomicilio() → idDomicilioFinal ya apunta al valor actual; no se toca la tabla.

                // 5. Hashear código de acceso si viene nuevo; si no, preservar el existente
                String codigoFinal;
                if (req.getCodigoAcceso() != null && !req.getCodigoAcceso().trim().isEmpty()) {
                    codigoFinal = Seguridad.hashear(req.getCodigoAcceso());
                } else {
                    codigoFinal = existente.getCodigoAcceso();
                }

                // 6. Construir el Paciente para el UPDATE
                Paciente paciente = new Paciente(
                    req.getIdPaciente(),
                    req.getNombre(),
                    req.getPrimerApellido(),
                    req.getSegundoApellido(),
                    req.getFechaNacimiento(),
                    req.getSexo(),
                    null, null, null,          // peso / estatura / talla — nunca se tocan aquí
                    req.getEmail(),
                    req.getTelefono(),
                    idDomicilioFinal,
                    codigoFinal,
                    null, null,                // fotografia / fotoBase64
                    req.getIdMedico(),
                    req.getEstatus()
                );

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
    
    public static Respuesta darDeBajaPaciente(int idPaciente) {
        Respuesta respuesta = new Respuesta();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                Paciente existente = conexionBD.selectOne("paciente.obtenerPorId", idPaciente);

                if (existente == null) {
                    respuesta.setError(true);
                    respuesta.setMensaje("El paciente no existe.");
                    return respuesta;
                }

                if (existente.getEstatus() == 0) {
                    respuesta.setError(true);
                    respuesta.setMensaje("El paciente ya se encuentra dado de baja.");
                    return respuesta;
                }

                int filas = conexionBD.update("paciente.darDeBaja", idPaciente);
                conexionBD.commit();

                if (filas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje("Paciente dado de baja correctamente.");
                } else {
                    respuesta.setError(true);
                    respuesta.setMensaje("No se pudo dar de baja al paciente.");
                }

            } catch (Exception e) {
                conexionBD.rollback();
                e.printStackTrace();
                respuesta.setError(true);
                respuesta.setMensaje("Error inesperado en la base de datos.");
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
