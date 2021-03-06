package pe.edu.pucp.individualroyeryangali.Entity;

import java.io.Serializable;
import java.util.Date;

public class CasoCovid implements Serializable {
    private String pkCaso;
    private String fechaRegistro;
    private String fechaFinalizado;
    private String nombreFoto;
    private String sintomas;
    private Double latitud;
    private Double longitud;
    private String direccionGPS;
    private String zonaLimena;
    private Usuario usuarioQueRegistra;
    private String estado;
    private String nombrePaciente;
    private int dniPaciente;

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public int getDniPaciente() {
        return dniPaciente;
    }

    public void setDniPaciente(int dniPaciente) {
        this.dniPaciente = dniPaciente;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getZonaLimena() {
        return zonaLimena;
    }

    public void setZonaLimena(String zonaLimena) {
        this.zonaLimena = zonaLimena;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getDireccionGPS() {
        return direccionGPS;
    }

    public void setDireccionGPS(String direccionGPS) {
        this.direccionGPS = direccionGPS;
    }


    public String getPkCaso() {
        return pkCaso;
    }

    public void setPkCaso(String pkCaso) {
        this.pkCaso = pkCaso;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getFechaFinalizado() {
        return fechaFinalizado;
    }

    public void setFechaFinalizado(String fechaFinalizado) {
        this.fechaFinalizado = fechaFinalizado;
    }

    public String getNombreFoto() {
        return nombreFoto;
    }

    public void setNombreFoto(String nombreFoto) {
        this.nombreFoto = nombreFoto;
    }

    public String getSintomas() {
        return sintomas;
    }

    public void setSintomas(String sintomas) {
        this.sintomas = sintomas;
    }

    public Usuario getUsuarioQueRegistra() {
        return usuarioQueRegistra;
    }

    public void setUsuarioQueRegistra(Usuario usuarioQueRegistra) {
        this.usuarioQueRegistra = usuarioQueRegistra;
    }
}
