package intep.proyecto.road2roldanillo.entidades.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import java.util.List;

import intep.proyecto.road2roldanillo.persistencia.DBHelper;
import intep.proyecto.road2roldanillo.rest.ImageHelper;
import intep.proyecto.road2roldanillo.util.db.TablaEntidadHelper;
import intep.proyecto.road2roldanillo.util.db.TablaHelper;

/**
 * Created by gurzaf on 1/7/15.
 */
public class Categoria extends TablaEntidadHelper {

    private String nombre;
    private int borrado;
    private String icono;

    public Categoria(int id) {
        super(id);
    }

    public Categoria(int id, String nombre, int borrado, String icono) {
        super(id);
        this.nombre = nombre;
        this.borrado = borrado;
        this.icono = icono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getBorrado() {
        return borrado;
    }

    public void setBorrado(int borrado) {
        this.borrado = borrado;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public Bitmap getIconoBitMap(Context context){
        return ImageHelper.getImageForCategoria(this,context);
    }

    @Override
    public String toString() {
        return nombre;
    }

    public static List<Categoria> getAllValues(SQLiteDatabase db){
        return (List<Categoria>) TablaEntidadHelper.getAllValues(Categoria.class.getSimpleName(),Categoria.class,db);
    }

}
