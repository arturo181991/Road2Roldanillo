package intep.proyecto.road2roldanillo.util.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import intep.proyecto.road2roldanillo.persistencia.DBHelper;

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

    protected static <T extends TablaHelper> List<T> getAllValues(String tableName, Class<T> subclass, SQLiteDatabase db){
        Cursor c = db.query(tableName, null, null, null, null, null, null);
        return DBHelper.selectAll(subclass, c);
    }
}
