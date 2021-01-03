package pe.edu.pucp.individualroyeryangali.Entity;

import java.util.Date;

public class CitaUsuario {
    private Usuario usuario;
    private int dia;
    private int mes;
    private int año;
    private int hora;
    private Usuario doctor;
    private String pkCita;
    private String direccionGPS;
    private String direccionUsuario;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getAño() {
        return año;
    }

    public void setAño(int año) {
        this.año = año;
    }

    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

    public Usuario getDoctor() {
        return doctor;
    }

    public void setDoctor(Usuario doctor) {
        this.doctor = doctor;
    }

    public String getPkCita() {
        return pkCita;
    }

    public void setPkCita(String pkCita) {
        this.pkCita = pkCita;
    }

    public String getDireccionGPS() {
        return direccionGPS;
    }

    public void setDireccionGPS(String direccionGPS) {
        this.direccionGPS = direccionGPS;
    }

    public String getDireccionUsuario() {
        return direccionUsuario;
    }

    public void setDireccionUsuario(String direccionUsuario) {
        this.direccionUsuario = direccionUsuario;
    }
}
