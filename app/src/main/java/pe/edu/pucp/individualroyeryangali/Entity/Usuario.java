package pe.edu.pucp.individualroyeryangali.Entity;

import java.io.Serializable;
import java.util.ArrayList;

public class Usuario implements Serializable {


    private String rol;
    private String primaryKey;
    private String codigoUsuario;
    private String nombreUsuario;

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }


    public String getCodigoUsuario() {
        return codigoUsuario;
    }


    public void setCodigoUsuario(String codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }
}
