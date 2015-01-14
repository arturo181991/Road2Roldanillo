package intep.proyecto.road2roldanillo.entidades.db;

import intep.proyecto.road2roldanillo.util.db.TablaEntidadHelper;

/**
 * Created by gurzaf on 1/7/15.
 */
public class Usuario extends TablaEntidadHelper {

    private String usuario;
    private String nombres;
    private String apellidos;

    public Usuario(int id, String usuario, String nombres, String apellidos) {
        super(id);
        this.usuario = usuario;
        this.nombres = nombres;
        this.apellidos = apellidos;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
}
