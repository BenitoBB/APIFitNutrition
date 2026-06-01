package dominio;

import dto.RSAutenticacionMedico;
import java.util.HashMap;
import java.util.Map;
import java.util.Base64;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Medico;
import utilidades.Constantes;

public class AutenticacionImp {

    public static RSAutenticacionMedico loginMedico(String noPersonal, String contrasena) {
        RSAutenticacionMedico respuesta = new RSAutenticacionMedico();
        respuesta.setError(true);
        
        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD == null) {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
            return respuesta;
        }

        try {
            Map<String, String> parametros = new HashMap<>();
            parametros.put("noPersonal", noPersonal);
            parametros.put("contrasena", contrasena);

            Medico medico = conexionBD.selectOne("autenticacion.loginMedico", parametros);

            if (medico == null) {
                respuesta.setMensaje("Credenciales inválidas");
            } else {
                if (medico.getEstatus() == 0) {
                    respuesta.setMensaje("Usuario inactivo");
                } else {
                    respuesta.setError(false);
                    respuesta.setMensaje("Autenticación exitosa");
                    
                    // Convertir fotografía a Base64 si existe
                    if (medico.getFotografia() != null) {
                        try {
                            String fotoBase64 = Base64.getEncoder().encodeToString(medico.getFotografia());
                            medico.setFotoBase64(fotoBase64);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    
                    // Limpiar contraseña por seguridad
                    medico.setContrasena(null);
                    
                    respuesta.setMedico(medico);
                    respuesta.setEsAdministrador(medico.getEsAdministrador() == 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        } finally {
            conexionBD.close();
        }

        return respuesta;
    }
}
