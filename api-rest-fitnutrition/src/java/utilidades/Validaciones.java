package utilidades;

public class Validaciones {
    
    public static boolean esVacio(String campo){
        return campo == null || campo.trim().isEmpty();
    }
    
    public static boolean esNumerico(String campo) {
        return campo.matches("\\d+");
    }
    
    public static boolean esNumericoConLongitud(String campo, int longitud) {
        return campo.matches("\\d{" + longitud + "}");
    }
    
}
