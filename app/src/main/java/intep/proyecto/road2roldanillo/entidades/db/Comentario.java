package intep.proyecto.road2roldanillo.entidades.db;

import java.util.Date;

import intep.proyecto.road2roldanillo.util.db.TablaEntidadHelper;
import intep.proyecto.road2roldanillo.util.db.TablaHelper;

/**
 * Created by gurzaf on 1/7/15.
 */
public class Comentario extends TablaEntidadHelper{

    private String detalle;
    private Lugar lugar;
    private Usuario usuario;
    private Date fecha;
    private int puntaje;
    private int subido;

    public Comentario(int id,String detalle, Lugar lugar, Usuario usuario, Date fecha, int puntaje, int subido) {
        super(id);
        this.detalle = detalle;
        this.lugar = lugar;
        this.usuario = usuario;
        this.fecha = fecha;
        this.puntaje = puntaje;
        this.subido = subido;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Lugar getLugar() {
        return lugar;
    }

    public void setLugar(Lugar lugar) {
        this.lugar = lugar;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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

    public int getSubido() {
        return subido;
    }

    public void setSubido(int subido) {
        this.subido = subido;
    }
}
