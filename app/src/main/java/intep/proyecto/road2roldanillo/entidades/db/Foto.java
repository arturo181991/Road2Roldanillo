package intep.proyecto.road2roldanillo.entidades.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.List;

import intep.proyecto.road2roldanillo.persistencia.DBHelper;
import intep.proyecto.road2roldanillo.rest.ImageHelper;
import intep.proyecto.road2roldanillo.util.db.TablaEntidadHelper;
import intep.proyecto.road2roldanillo.util.db.TablaHelper;

/**
 * Created by gurzaf on 1/7/15.
 */
public class Foto extends TablaEntidadHelper {

    private String foto;
    private Lugar lugar;

    public Foto(int id){
        super(id);
    }

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

    public Bitmap getImage(Context context){
        return ImageHelper.getImageForFoto(this, context);
    }

    public static List<Foto> getAllValuesByLugar(SQLiteDatabase db, Lugar lugar){
        String[] args = new String[]{lugar.getId()+""};
        String sql = "SELECT * FROM ".concat(Foto.class.getSimpleName()).concat(" WHERE lugar = "+lugar.getId());
        Log.i(Foto.class.getSimpleName(), sql);
        Cursor c = db.rawQuery(sql,null);
        List<Foto> fotos = DBHelper.selectAll(Foto.class, c);
        for (int i = 0 ; i < fotos.size() ; i++){
            fotos.get(i).setLugar(lugar);
        }
        return fotos;
    }

}
