package pojo;

public class Medico {
    
    private int idMedico;
    private String nombre;
    private String primerApellido;
    private String segundoApellido;
    private String fechaNacimiento; // Mapeado como String
    private String sexo;
    private Integer idDomicilio; // Integer porque permite NULL
    private String noPersonal;
    private String cedulaProfesional;
    private String contrasena;
    
    // Propiedades de fotografía 
    private byte[] fotografia;
    private String fotoBase64;
    
    // Campos de visualización de dirección
    private String calle;
    private String numero;
    private Integer idColonia;
    private String nombreColonia;
    private String codigoPostal;
    private String ciudad;
    private String estado;
    private String direccionCompleta;
    
    private int esAdministrador; // 0 = Médico, 1 = Administrador
    private int estatus; // 1 = Activo, 0 = Inactivo

    public Medico() {
    }

    public Medico(int idMedico, String nombre, String primerApellido, String segundoApellido, String fechaNacimiento, String sexo, Integer idDomicilio, String noPersonal, String cedulaProfesional, String contrasena, byte[] fotografia, String fotoBase64, int esAdministrador, int estatus) {
        this.idMedico = idMedico;
        this.nombre = nombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
        this.idDomicilio = idDomicilio;
        this.noPersonal = noPersonal;
        this.cedulaProfesional = cedulaProfesional;
        this.contrasena = contrasena;
        this.fotografia = fotografia;
        this.fotoBase64 = fotoBase64;
        this.esAdministrador = esAdministrador;
        this.estatus = estatus;
    }

    public int getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(int idMedico) {
        this.idMedico = idMedico;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Integer getIdDomicilio() {
        return idDomicilio;
    }

    public void setIdDomicilio(Integer idDomicilio) {
        this.idDomicilio = idDomicilio;
    }

    public String getNoPersonal() {
        return noPersonal;
    }

    public void setNoPersonal(String noPersonal) {
        this.noPersonal = noPersonal;
    }

    public String getCedulaProfesional() {
        return cedulaProfesional;
    }

    public void setCedulaProfesional(String cedulaProfesional) {
        this.cedulaProfesional = cedulaProfesional;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public byte[] getFotografia() {
        return fotografia;
    }

    public void setFotografia(byte[] fotografia) {
        this.fotografia = fotografia;
    }

    public String getFotoBase64() {
        return fotoBase64;
    }

    public void setFotoBase64(String fotoBase64) {
        this.fotoBase64 = fotoBase64;
    }

    public int getEsAdministrador() {
        return esAdministrador;
    }

    public void setEsAdministrador(int esAdministrador) {
        this.esAdministrador = esAdministrador;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Integer getIdColonia() {
        return idColonia;
    }

    public void setIdColonia(Integer idColonia) {
        this.idColonia = idColonia;
    }

    public String getNombreColonia() {
        return nombreColonia;
    }

    public void setNombreColonia(String nombreColonia) {
        this.nombreColonia = nombreColonia;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDireccionCompleta() {
        return direccionCompleta;
    }

    public void setDireccionCompleta(String direccionCompleta) {
        this.direccionCompleta = direccionCompleta;
    }
}