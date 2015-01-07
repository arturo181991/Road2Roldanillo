package intep.proyecto.road2roldanillo.entidades.db;

/**
 * Created by gurzaf on 1/7/15.
 */
public class Usuario {

    private int id;
    private String usuario;
    private String nombres;
    private String apellidos;

    public Usuario(int id, String usuario, String nombres, String apellidos) {
        this.id = id;
        this.usuario = usuario;
        this.nombres = nombres;
        this.apellidos = apellidos;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
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
