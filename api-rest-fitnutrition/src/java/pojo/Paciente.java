/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pojo;

public class Paciente {
    private int idPaciente;
    private String nombre;
    private String primerApellido;
    private String segundoApellido;
    private String fechaNacimiento; // Mapeado como String
    private String sexo;
    private Double peso; // Double porque permite NULL
    private Double estatura;
    private Double talla;
    private String email;
    private String telefono;
    private Integer idDomicilio; // Integer porque permite NULL
    private String codigoAcceso;
    
    // Propiedades de fotografía 
    private byte[] fotografia;
    private String fotoBase64;
    
    private int idMedico;
    private String medico;
    private int estatus; // 1 = Activo, 0 = Inactivo

    public Paciente() {
    }
    
    public Paciente(int idPaciente, String nombre, String primerApellido, String segundoApellido, String fechaNacimiento, String sexo, Double peso, Double estatura, Double talla, String email, String telefono, Integer idDomicilio, String codigoAcceso, byte[] fotografia, String fotoBase64, int idMedico, String medico, int estatus) {
        this.idPaciente = idPaciente;
        this.nombre = nombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
        this.peso = peso;
        this.estatura = estatura;
        this.talla = talla;
        this.email = email;
        this.telefono = telefono;
        this.idDomicilio = idDomicilio;
        this.codigoAcceso = codigoAcceso;
        this.fotografia = fotografia;
        this.fotoBase64 = fotoBase64;
        this.idMedico = idMedico;
        this.medico = medico;
        this.estatus = estatus;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
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

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public Double getEstatura() {
        return estatura;
    }

    public void setEstatura(Double estatura) {
        this.estatura = estatura;
    }

    public Double getTalla() {
        return talla;
    }

    public void setTalla(Double talla) {
        this.talla = talla;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Integer getIdDomicilio() {
        return idDomicilio;
    }

    public void setIdDomicilio(Integer idDomicilio) {
        this.idDomicilio = idDomicilio;
    }

    public String getCodigoAcceso() {
        return codigoAcceso;
    }

    public void setCodigoAcceso(String codigoAcceso) {
        this.codigoAcceso = codigoAcceso;
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

    public int getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(int idMedico) {
        this.idMedico = idMedico;
    }

    public String getMedico() {
        return medico;
    }

    public void setMedico(String medico) {
        this.medico = medico;
    }
    
    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }
}