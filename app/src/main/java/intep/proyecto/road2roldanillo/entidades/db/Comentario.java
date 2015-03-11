package intep.proyecto.road2roldanillo.entidades.db;

import java.util.Date;

import intep.proyecto.road2roldanillo.util.db.TablaEntidadHelper;
import intep.proyecto.road2roldanillo.util.db.TablaHelper;

/**
 * Created by gurzaf on 1/7/15.
 */
public class Comentario extends TablaEntidadHelper{

    private String detalle;
    private Date fecha;
    private int puntaje;
    private int lugar;
    private String usuario;
    private String usuarioNombre;
    private int borrado;

    public Comentario(int id){
        super(id);
    }

    public Comentario(int id, String detalle, Date fecha, int puntaje, int lugar, String usuario, String usuarioNombre, int borrado) {
        super(id);
        this.detalle = detalle;
        this.fecha = fecha;
        this.puntaje = puntaje;
        this.lugar = lugar;
        this.usuario = usuario;
        this.usuarioNombre = usuarioNombre;
        this.borrado = borrado;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    public int getLugar() {
        return lugar;
    }

    public void setLugar(int lugar) {
        this.lugar = lugar;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public int getBorrado() {
        return borrado;
    }

    public void setBorrado(int borrado) {
        this.borrado = borrado;
    }
}
