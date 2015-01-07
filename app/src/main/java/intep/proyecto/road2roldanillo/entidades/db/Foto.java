package intep.proyecto.road2roldanillo.entidades.db;

import intep.proyecto.road2roldanillo.util.db.TablaHelper;

/**
 * Created by gurzaf on 1/7/15.
 */
public class Foto extends TablaHelper {

    private String foto;
    private Lugar lugar;

    public Foto(int id, String foto, Lugar lugar) {
        super(id);
        this.foto = foto;
        this.lugar = lugar;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Lugar getLugar() {
        return lugar;
    }

    public void setLugar(Lugar lugar) {
        this.lugar = lugar;
    }

}
