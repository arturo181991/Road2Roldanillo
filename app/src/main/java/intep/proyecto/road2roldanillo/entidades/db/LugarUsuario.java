package intep.proyecto.road2roldanillo.entidades.db;

import java.util.Date;

import intep.proyecto.road2roldanillo.util.db.TablaEntidadHelper;
import intep.proyecto.road2roldanillo.util.db.TablaHelper;

/**
 * Created by gurzaf on 1/7/15.
 */
public class LugarUsuario extends TablaEntidadHelper{

    private Lugar lugar;
    private Usuario usuario;
    private Date fecha;

    public LugarUsuario(int id, Lugar lugar, Usuario usuario, Date fecha) {
        super(id);
        this.lugar = lugar;
        this.usuario = usuario;
        this.fecha = fecha;
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
}
