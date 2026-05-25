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
    
    // Utilizado para Cita
    public static String formatearFechaISO(String fechaCitaStr) {
        if (esVacio(fechaCitaStr)) {
            return null;
        }
        try {
            java.time.LocalDate fecha;
            if (fechaCitaStr.contains("-")) {
                fecha = java.time.LocalDate.parse(fechaCitaStr.trim(), java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } else if (fechaCitaStr.contains("/")) {
                fecha = java.time.LocalDate.parse(fechaCitaStr.trim(), java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } else {
                return null;
            }
            return fecha.toString();
        } catch (Exception e) {
            return null;
        }
    }
    
    // Utilizado para Cita
    public static boolean esFechaCitaValida(String fechaCitaStr) {
        String iso = formatearFechaISO(fechaCitaStr);
        if (iso == null) {
            return false;
        }
        java.time.LocalDate fecha = java.time.LocalDate.parse(iso);
        java.time.LocalDate hoyMasUno = java.time.LocalDate.now().plusDays(1);
        return !fecha.isBefore(hoyMasUno);
    }
    
    // Utilizado para Cita
    public static boolean esHoraValida(String horaCitaStr) {
        if (esVacio(horaCitaStr)) {
            return false;
        }
        try {
            java.time.LocalTime hora;
            String trimmed = horaCitaStr.trim();
            if (trimmed.length() == 5) {
                hora = java.time.LocalTime.parse(trimmed, java.time.format.DateTimeFormatter.ofPattern("HH:mm"));
            } else if (trimmed.length() == 8) {
                hora = java.time.LocalTime.parse(trimmed, java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
            } else {
                return false;
            }
            
            // RN-08: Hora entre 07:00:00 y 20:30:00
            return !hora.isBefore(java.time.LocalTime.of(7, 0)) && !hora.isAfter(java.time.LocalTime.of(20, 30));
        } catch (Exception e) {
            return false;
        }
    }
    
    // Utilizado para Cita
    public static boolean esBloque30Minutos(String horaCitaStr) {
        if (esVacio(horaCitaStr)) {
            return false;
        }
        try {
            java.time.LocalTime hora;
            String trimmed = horaCitaStr.trim();
            if (trimmed.length() == 5) {
                hora = java.time.LocalTime.parse(trimmed, java.time.format.DateTimeFormatter.ofPattern("HH:mm"));
            } else if (trimmed.length() == 8) {
                hora = java.time.LocalTime.parse(trimmed, java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
            } else {
                return false;
            }
            
            // RN-08: Minutos 00 o 30
            return hora.getMinute() == 0 || hora.getMinute() == 30;
        } catch (Exception e) {
            return false;
        }
    }
}