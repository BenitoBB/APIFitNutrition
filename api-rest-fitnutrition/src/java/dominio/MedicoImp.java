package dominio;

import dto.Respuesta;
import java.util.HashMap;
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