package intep.proyecto.road2roldanillo.persistencia;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "road2roldanillo.sqlite";

    private static final int DB_SCHEME_VERSION = 1;

    private static final String TABLA_CATEGORIA = "CREATE TABLE categoria" +
            "(id INTEGER PRIMARY KEY, nombre TEXT, subido INT, icono TEXT)";

    private static final String TABLA_USUARIO = "CREATE TABLE usuario" +
            "(id INTEGER PRIMARY KEY, usuario TEXT, nombres TEXT, apellidos TEXT)";

    private static final String TABLA_LUGAR = "CREATE TABLE lugar" +
            "(id INTEGER PRIMARY KEY, nombre TEXT, latitud REAL, longitud REAL, descripcion TEXT, puntaje REAL," +
            "categoria INT, subido INT, direccion TEXT, telefono TEXT, sitio_web TEXT)";

    private static final String TABLA_FOTO = "CREATE TABLE foto" +
            "(id INTEGER PRIMARY KEY, foto TEXT, lugar INT)";

    private static final String TABLA_COMENTARIO = "CREATE TABLE comentario" +
            "(id INTEGER PRIMARY KEY, detalle TEXT, lugar INT, usuario INT, fecha TEXT, puntaje INT, subido INT)";

    private static final String TABLA_LUGAR_USUARIO = "CREATE TABLE lugar_usuario" +
            "(id_lugar_usuario INTEGER PRIMARY KEY, id_lugar INT, id_usuario INT, fecha TEXT)";

    private static final String TABLA_ULTIMA_ACTUALIZACION = "CREATE TABLE ultima_actualizacion" +
            "(fecha TEXT)";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_SCHEME_VERSION);
    }

    /*public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }*/

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLA_CATEGORIA);
        sqLiteDatabase.execSQL(TABLA_USUARIO);
        sqLiteDatabase.execSQL(TABLA_LUGAR);
        sqLiteDatabase.execSQL(TABLA_FOTO);
        sqLiteDatabase.execSQL(TABLA_COMENTARIO);
        sqLiteDatabase.execSQL(TABLA_LUGAR_USUARIO);
        sqLiteDatabase.execSQL(TABLA_ULTIMA_ACTUALIZACION);
        //sqLiteDatabase.execSQL(DBManager.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLA_CATEGORIA);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLA_USUARIO);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLA_LUGAR);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLA_FOTO);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLA_COMENTARIO);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLA_LUGAR_USUARIO);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLA_ULTIMA_ACTUALIZACION);
        onCreate(sqLiteDatabase);
    }
}
