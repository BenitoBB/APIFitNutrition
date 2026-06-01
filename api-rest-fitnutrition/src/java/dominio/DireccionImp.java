package dominio;

import java.util.List;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Direccion;

public class DireccionImp {
     public static List<Direccion> obtenerDireccionCodigoPostal(int codigoPostal){
        List<Direccion> colonias = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        
        if (conexionBD != null) {
            try {
                colonias = conexionBD.selectList("direccion.obtener-colonias-codigo_postal", codigoPostal);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        
        return colonias;
    }

    public static dto.Respuesta crearDireccion(Direccion direccion) {
        dto.Respuesta respuesta = new dto.Respuesta();
        respuesta.setError(true);
        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD != null) {
            try {
                int filasAfectadas = conexionBD.insert("direccion.crear-direccion", direccion);
                conexionBD.commit();
                if (filasAfectadas == 1) {
                    respuesta.setError(false);
                    respuesta.setMensaje("Dirección guardada");
                    respuesta.setValor(String.valueOf(direccion.getIdDireccion()));
                }
            } catch (Exception e) {
                respuesta.setMensaje(e.getMessage());
            } finally {
                conexionBD.close();
            }
        }
        return respuesta;
    }

    public static dto.Respuesta editar(Direccion direccion) {
        dto.Respuesta respuesta = new dto.Respuesta();
        respuesta.setError(true);
        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD != null) {
            try {
                int filasAfectadas = conexionBD.update("direccion.editar", direccion);
                conexionBD.commit();
                if (filasAfectadas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje("Información de la dirección actualizada.");
                } else {
                    respuesta.setMensaje("No se pudo actualizar la dirección.");
                }
            } catch (Exception e) {
                respuesta.setMensaje(e.getMessage());
            } finally {
                conexionBD.close();
            }
        }
        return respuesta;
    }

    public static boolean eliminar(Integer idDireccion) {
        if (idDireccion == null || idDireccion <= 0) return false;
        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD != null) {
            try {
                int filas = conexionBD.delete("direccion.eliminar", idDireccion);
                conexionBD.commit();
                return filas > 0;
            } catch (Exception e) {
                conexionBD.rollback();
                return false;
            } finally {
                conexionBD.close();
            }
        }
        return false;
    }

    public static Direccion obtenerDireccionPorId(Integer idDireccion) {
        Direccion direccion = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD != null) {
            try {
                direccion = conexionBD.selectOne("direccion.obtener-direccion-id", idDireccion);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        return direccion;
    }
}
