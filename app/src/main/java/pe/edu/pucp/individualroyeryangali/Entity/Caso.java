package pe.edu.pucp.individualroyeryangali.Entity;

import java.io.Serializable;
import java.util.Date;

public class Caso implements Serializable {
    private String pkCaso;
    private Date fechaRegistro;
    private Date fechaFinalizado;
    private String nombreFoto;
    private String caracteristica;
    private Usuario usuarioQueRegistra;

    public String getPkCaso() {
        return pkCaso;
    }

    public void setPkCaso(String pkCaso) {
        this.pkCaso = pkCaso;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Date getFechaFinalizado() {
        return fechaFinalizado;
    }

    public void setFechaFinalizado(Date fechaFinalizado) {
        this.fechaFinalizado = fechaFinalizado;
    }

    public String getNombreFoto() {
        return nombreFoto;
    }

    public void setNombreFoto(String nombreFoto) {
        this.nombreFoto = nombreFoto;
    }

    public String getCaracteristica() {
        return caracteristica;
    }

    public void setCaracteristica(String caracteristica) {
        this.caracteristica = caracteristica;
    }

    public Usuario getUsuarioQueRegistra() {
        return usuarioQueRegistra;
    }

    public void setUsuarioQueRegistra(Usuario usuarioQueRegistra) {
        this.usuarioQueRegistra = usuarioQueRegistra;
    }
}
