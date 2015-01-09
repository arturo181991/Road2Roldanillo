package intep.proyecto.road2roldanillo.util.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by gurzaf on 1/9/15.
 */
public class TablaEntidadHelper extends TablaHelper{

    private Integer id;

    public TablaEntidadHelper(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id.toString();
    }

    public long insert(SQLiteDatabase db){
        Log.i(super.TAG, "Ejecutando método INSERT a la entidad "
                .concat(this.getClass().getSimpleName())
                .concat("[")
                .concat(id.toString())
                .concat("]"));
        return db.insert(this.getClass().getSimpleName(),null,getContent());
    }
}
