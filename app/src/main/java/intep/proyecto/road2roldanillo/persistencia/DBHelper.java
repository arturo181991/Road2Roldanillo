package intep.proyecto.road2roldanillo.persistencia;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import intep.proyecto.road2roldanillo.entidades.db.CategoriaDB;
import intep.proyecto.road2roldanillo.entidades.db.Comentario;
import intep.proyecto.road2roldanillo.entidades.db.Foto;
import intep.proyecto.road2roldanillo.entidades.db.Lugar;
import intep.proyecto.road2roldanillo.entidades.db.LugarUsuario;
import intep.proyecto.road2roldanillo.entidades.db.UltimaActualizacion;
import intep.proyecto.road2roldanillo.entidades.db.Usuario;
import intep.proyecto.road2roldanillo.util.db.TablaHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "road2roldanillo.sqlite";

    private static final int DB_SCHEME_VERSION = 9;

    private final Class[] classes;

    private static final String TAG = DBHelper.class.getName();

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_SCHEME_VERSION);
        Log.i(TAG, "Se instancia ".concat(TAG).concat(", y se carga el arreglo de clases"));
        classes = new Class[]{
                CategoriaDB.class,
                Lugar.class,
                Usuario.class,
                Comentario.class,
                Foto.class,
                LugarUsuario.class,
                UltimaActualizacion.class
        };
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        onUpgrade(sqLiteDatabase,0,0);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        Log.i(TAG,"Se eliminaran las tablas anteriores de la base de datos");

        for (Class clase : classes){
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+clase.getSimpleName());
        }

        Log.i(TAG,"Se crea la base de datos");
        List<String> consultas = getCreateSQL();
        Log.i(TAG,"Se crearan "+consultas.size()+" tablas.");

        for (String sqlTabla : consultas){
            Log.i(TAG,"CREANDO TABLA: ".concat(sqlTabla));
            sqLiteDatabase.execSQL(sqlTabla);
        }

    }

    private List<String> getCreateSQL() {



        List<String> tablas = new ArrayList<String>();

        for (Class clase : classes) {
            String nombre = clase.getSimpleName();
            List<String> campos = new ArrayList();
            List<String> tipos = new ArrayList();
            for (Field field : clase.getDeclaredFields()) {
                campos.add(field.getName());
                tipos.add(getSQLTipo(field.getType().getSimpleName()));
            }
            String sql = "CREATE TABLE ".concat(nombre).concat(" ( ");

            Class superClass = clase.getSuperclass();
            if(superClass!=null){
                if(superClass.getSimpleName().equalsIgnoreCase(TablaHelper.class.getSimpleName())){
                    sql += "id INTEGER PRIMARY KEY , ";
                }
            }

            for (int i = 0; i < tipos.size(); i++) {
                String tipo = tipos.get(i);
                String campo = campos.get(i);
                sql += campo.concat(" ").concat(tipo).concat(" ");
                if (campo.equalsIgnoreCase("id")) {
                    sql += "PRIMARY KEY ";
                }
                if (i + 1 < tipos.size()) {
                    sql += ", ";
                }
            }
            sql += ")";
            tablas.add(sql);
        }

        return tablas;
    }

    private static String getSQLTipo(String classType){
        if(classType.equalsIgnoreCase("string") || classType.equalsIgnoreCase("date")){
            return "TEXT";
        }else if(classType.equalsIgnoreCase("double") || classType.equalsIgnoreCase("float")){
            return "REAL";
        }else{
            return "INTEGER";
        }
    }

}
