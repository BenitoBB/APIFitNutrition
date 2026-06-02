package dominio;

import dto.Respuesta;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Consulta;
import utilidades.Constantes;


public class ConsultaImp {

    public static Respuesta registrarConsulta(Consulta consulta) {

        Respuesta respuesta = new Respuesta();

        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {

            try {

                // CALCULO AUTOMATICO DEL IMC
                double imc = consulta.getPeso()
                        / (consulta.getTalla() * consulta.getTalla());

                consulta.setImc(imc);

                int filasAfectadas = conexionBD.insert(
                        "consulta.registrar-consulta",
                        consulta
                );

                if (filasAfectadas > 0) {
                    if (consulta.getIdCita() != null && consulta.getIdCita() > 0) {
                        conexionBD.update("consulta.marcar-cita-asistida", consulta.getIdCita());
                    }
                    conexionBD.commit();
                    respuesta.setError(false);
                    respuesta.setMensaje("Consulta registrada correctamente");
                } else {
                    conexionBD.rollback();
                    respuesta.setError(true);
                    respuesta.setMensaje("No se pudo registrar la consulta");
                }

            } catch (Exception e) {

                conexionBD.rollback();

                respuesta.setError(true);
                // respuesta.setMensaje("Error al registrar consulta");
                respuesta.setMensaje(e.getMessage());
                e.printStackTrace();

            } finally {
                conexionBD.close();
            }

        } else {

            respuesta.setError(true);
            respuesta.setMensaje("Sin conexión a la base de datos");

        }

        return respuesta;
    }
    
    public static boolean modificarConsulta(Consulta consulta) {

        boolean resultado = false;

        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {

            try {

                int filasAfectadas = conexionBD.update(
                        "consulta.modificar-consulta",
                        consulta
                );

                conexionBD.commit();

                if (filasAfectadas > 0) {
                    resultado = true;
                }

            } catch (Exception e) {

                e.printStackTrace();
                conexionBD.rollback();

            } finally {

                conexionBD.close();
            }
        }

        return resultado;
    }

        public static List<Consulta> buscarConsultasPaciente(int idPaciente) {

        List<Consulta> consultas = null;

        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {

            try {
                asegurarColumnaCancelada(conexionBD);

                consultas = conexionBD.selectList(
                        "consulta.buscar-consultas-paciente",
                        idPaciente
                );

            } catch (Exception e) {

                throw new RuntimeException(e.getMessage());

            } finally {

                conexionBD.close();

            }
        }

        return consultas;
    }

    public static List<Consulta> buscarConsultas(String criterio, Integer idMedico) {

        List<Consulta> consultas = null;
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                asegurarColumnaCancelada(conexionBD);

                Map<String, Object> parametros = new HashMap<>();
                parametros.put("criterio", criterio != null ? criterio.trim() : "");
                parametros.put("idMedico", idMedico != null && idMedico > 0 ? idMedico : null);

                consultas = conexionBD.selectList("consulta.buscar-consultas", parametros);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            } finally {
                conexionBD.close();
            }
        }

        return consultas;
    }

    public static Respuesta cancelarConsulta(int idConsulta) {

    Respuesta respuesta = new Respuesta();

    SqlSession conexionBD = MyBatisUtil.getSession();

    if (conexionBD != null) {

        try {
            asegurarColumnaCancelada(conexionBD);

            int filasAfectadas = conexionBD.update(
                    "consulta.cancelar-consulta",
                    idConsulta
            );

            conexionBD.commit();

            if (filasAfectadas > 0) {

                respuesta.setError(false);
                respuesta.setMensaje("Consulta cancelada correctamente");

            } else {

                respuesta.setError(true);
                respuesta.setMensaje(
                        "La consulta no existe o ya fue cancelada"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
            conexionBD.rollback();
            respuesta.setError(true);
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);

        } finally {

            conexionBD.close();
        }

    } else {

        respuesta.setError(true);
        respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
    }

    return respuesta;
}

    private static void asegurarColumnaCancelada(SqlSession conexionBD) {
        try {
            boolean existeColumna;
            try (ResultSet columnas = conexionBD.getConnection()
                    .getMetaData()
                    .getColumns(null, null, "consulta", "cancelada")) {
                existeColumna = columnas.next();
            }

            if (!existeColumna) {
                try (Statement sentencia = conexionBD.getConnection().createStatement()) {
                    sentencia.executeUpdate(
                            "ALTER TABLE consulta "
                            + "ADD COLUMN cancelada TINYINT(1) NOT NULL DEFAULT 0"
                    );
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("No se pudo validar el estatus de cancelacion de consultas: " + e.getMessage(), e);
        }
    }


}
