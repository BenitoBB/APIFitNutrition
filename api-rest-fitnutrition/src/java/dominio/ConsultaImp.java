package dominio;

import dto.Respuesta;
import java.util.List;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Consulta;

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

                conexionBD.commit();

                if (filasAfectadas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje("Consulta registrada correctamente");
                } else {
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

    public static boolean cancelarConsulta(int idConsulta) {

        boolean resultado = false;

        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {

            try {

                int filasAfectadas = conexionBD.update(
                        "consulta.cancelar-consulta",
                        idConsulta
                );

                conexionBD.commit();

                resultado = filasAfectadas > 0;

            } catch (Exception e) {

                e.printStackTrace();
                conexionBD.rollback();

            } finally {

                conexionBD.close();
            }
        }

        return resultado;
    }

}
