package dominio;

import org.apache.ibatis.session.SqlSession;
import pojo.*;
import java.util.List;
import modelo.mybatis.MyBatisUtil;

public class InicioImp {

    public static ResumenPaciente obtenerResumen(int idPaciente) {
        ResumenPaciente resumen = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD != null) {
            try {
                resumen = conexionBD.selectOne("inicio.resumen-paciente", idPaciente);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        return resumen;
    }

    public static ProximaCita obtenerProximaCita(int idPaciente) {
        ProximaCita cita = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD != null) {
            try {
                cita = conexionBD.selectOne("inicio.proxima-cita", idPaciente);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        return cita;
    }

    public static List<PuntoPeso> obtenerProgresoPeso(int idPaciente) {
        List<PuntoPeso> progreso = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD != null) {
            try {
                progreso = conexionBD.selectList("inicio.progreso-peso", idPaciente);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        return progreso;
    }

    public static ResumenDietaInicio obtenerResumenDieta(int idPaciente) {
        ResumenDietaInicio resumen = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD != null) {
            try {
                resumen = conexionBD.selectOne("inicio.dieta-resumen", idPaciente);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        return resumen;
    }

    public static UltimaConsulta obtenerUltimaConsulta(int idPaciente) {
        UltimaConsulta consulta = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD != null) {
            try {
                consulta = conexionBD.selectOne("inicio.ultima-consulta", idPaciente);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        return consulta;
    }
}