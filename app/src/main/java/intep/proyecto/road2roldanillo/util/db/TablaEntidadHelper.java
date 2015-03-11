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
        return db.insert(this.getClass().getSimpleName(), null, getContent());
    }

    public int update(SQLiteDatabase db, Integer oldId){
        Log.i(super.TAG, "Ejecutando método UPDATE a la entidad "
                .concat(this.getClass().getSimpleName())
                .concat("[")
                .concat(id.toString())
                .concat("]"));
        String params[] = new String[]{oldId.toString()};
        return db.update(this.getClass().getSimpleName(),getContent(),"id = ?",params);
    }

    public int remove(SQLiteDatabase db){
        Log.i(super.TAG, "Ejecutando método DELETE a la entidad "
                .concat(this.getClass().getSimpleName())
                .concat("[")
                .concat(id.toString())
                .concat("]"));
        String params[] = new String[]{id.toString()};
        return db.delete(this.getClass().getSimpleName(), "id = ?", params);
    }

    public boolean existInDatabase(SQLiteDatabase db){
        Log.i(super.TAG, "Verificando si la entidad "
                .concat(this.getClass().getSimpleName())
                .concat("[")
                .concat(id.toString())
                .concat("] existe en la base de datos."));
        try {
            String params[] = new String[]{id.toString()};
            Cursor c = db.query(this.getClass().getSimpleName(), null, "id = ?", params, null, null, null, null);
            return c.getCount()==1;
        }catch (Exception e){
            Log.e(TAG,"Error verificando si existe la entidad",e);
            return false;
        }
    }

    protected static <T extends TablaHelper> List<T> getAllValues(String tableName, Class<T> subclass, SQLiteDatabase db){
        Cursor c = db.query(tableName, null, null, null, null, null, null);
        return DBHelper.selectAll(subclass, c);
    }
}
