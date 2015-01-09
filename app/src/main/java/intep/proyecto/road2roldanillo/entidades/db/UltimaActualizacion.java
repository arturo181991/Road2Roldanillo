package intep.proyecto.road2roldanillo.entidades.db;

import java.util.Date;

import intep.proyecto.road2roldanillo.util.db.TablaHelper;

/**
 * Created by gurzaf on 1/7/15.
 */
public class UltimaActualizacion extends TablaHelper{

    private Date fecha;

    public UltimaActualizacion(){
    }

    public UltimaActualizacion(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFecha() {

        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
