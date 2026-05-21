package utilidades;

import java.util.regex.Pattern;

public class Validaciones {
    
    // ── Métodos existentes (sin cambios) ────────────────────────────────────
    
    public static boolean esVacio(String campo) {
        return campo == null || campo.trim().isEmpty();
    }
    
    public static boolean esNumerico(String campo) {
        return campo.matches("\\d+");
    }
    
    public static boolean esNumericoConLongitud(String campo, int longitud) {
        return campo.matches("\\d{" + longitud + "}");
    }
    
    // ── Métodos nuevos ──────────────────────────────────────────────────────
    
    private static final Pattern PATRON_CORREO =
            Pattern.compile("^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$");

    /**
     * Retorna null si el teléfono es válido, o el mensaje de error si no lo es.
     * Internamente usa esVacio() y esNumericoConLongitud() ya existentes.
     *
     * @param requerido true = obligatorio, false = opcional (se permite vacío)
     */
    public static String validarTelefono(String telefono, boolean requerido) {
        if (esVacio(telefono)) {
            return requerido ? "El teléfono es obligatorio." : null;
        }
        if (!esNumericoConLongitud(telefono.trim(), 10)) {
            return "El teléfono debe contener exactamente 10 dígitos numéricos.";
        }
        return null;
    }

    /**
     * Retorna null si el correo es válido, o el mensaje de error si no lo es.
     *
     * @param requerido true = obligatorio, false = opcional (se permite vacío)
     */
    public static String validarCorreo(String correo, boolean requerido) {
        if (esVacio(correo)) {
            return requerido ? "El correo electrónico es obligatorio." : null;
        }
        if (!PATRON_CORREO.matcher(correo.trim()).matches()) {
            return "El correo electrónico no tiene un formato válido.";
        }
        return null;
    }
}