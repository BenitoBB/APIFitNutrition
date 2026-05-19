package pojo;

public class Direccion {
    Integer idDireccion;
    String calle;
    String numero;
    Integer codigoPostal;
    Integer idColonia;
    String colonia;
    String ciudad;
    String municipio;
    String estado;

    public Direccion() {
    }

    public Direccion(Integer idDireccion, String calle, String numero, Integer codigoPostal, Integer idColonia, String colonia, String ciudad, String municipio, String estado) {
        this.idDireccion = idDireccion;
        this.calle = calle;
        this.numero = numero;
        this.codigoPostal = codigoPostal;
        this.idColonia = idColonia;
        this.colonia = colonia;
        this.ciudad = ciudad;
        this.municipio = municipio;
        this.estado = estado;
    }

    public Integer getIdDireccion() {
        return idDireccion;
    }

    public String getCalle() {
        return calle;
    }

    public String getNumero() {
        return numero;
    }

    public Integer getCodigoPostal() {
        return codigoPostal;
    }

    public Integer getIdColonia() {
        return idColonia;
    }

    public String getColonia() {
        return colonia;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getMunicipio() {
        return municipio;
    }

    public String getEstado() {
        return estado;
    }

    public void setIdDireccion(Integer idDireccion) {
        this.idDireccion = idDireccion;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setCodigoPostal(Integer codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public void setIdColonia(Integer idColonia) {
        this.idColonia = idColonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
}