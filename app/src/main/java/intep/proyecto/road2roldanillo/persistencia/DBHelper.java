package intep.proyecto.road2roldanillo.persistencia;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import intep.proyecto.road2roldanillo.entidades.db.Categoria;
import intep.proyecto.road2roldanillo.entidades.db.Comentario;
import intep.proyecto.road2roldanillo.entidades.db.Foto;
import intep.proyecto.road2roldanillo.entidades.db.Lugar;
import intep.proyecto.road2roldanillo.entidades.db.LugarUsuario;
import intep.proyecto.road2roldanillo.entidades.db.UltimaActualizacion;
import intep.proyecto.road2roldanillo.entidades.db.Usuario;
import intep.proyecto.road2roldanillo.util.ReflectionHelper;
import intep.proyecto.road2roldanillo.util.db.TablaEntidadHelper;
import intep.proyecto.road2roldanillo.util.db.TablaHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "road2roldanillo.sqlite";

    private static final int DB_SCHEME_VERSION = 42;

    private final Class[] classes;

    private static final String TAG = DBHelper.class.getName();

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_SCHEME_VERSION);
        Log.i(TAG, "Se instancia ".concat(TAG).concat(", y se carga el arreglo de clases"));
        classes = new Class[]{
                Categoria.class,
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
//        createTables(sqLiteDatabase);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        Log.i(TAG,"Se eliminaran las tablas anteriores de la base de datos");

        for (Class clase : classes){
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+clase.getSimpleName());
        }

        createTables(sqLiteDatabase);

    }

    private void createTables(SQLiteDatabase sqLiteDatabase) {
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
                if(superClass.getSimpleName().equalsIgnoreCase(TablaEntidadHelper.class.getSimpleName())){
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

    public static <T extends TablaHelper> List<T> selectAll(Class<T> subClass, Cursor cursor){
        try {

            List<T> entities = new ArrayList<>();

            Log.i(TAG,"Tamaño del Cursor: "+cursor.getCount());

            if (cursor.moveToFirst()) {
                do {
                    Field[] campos = subClass.getDeclaredFields();
                    Log.i(TAG,"Se obtienen los campos de la clase: ".concat(subClass.getSimpleName()));

                    T entity=null;

                    String[] columnNames = cursor.getColumnNames();
                    for (String columnName : columnNames){
                        Log.i(TAG,"Columna: ".concat(columnName));
                    }

                    for (Constructor constructor : subClass.getConstructors()) {
                        if(constructor.getParameterTypes().length==1 && constructor.getParameterTypes()[0].equals(int.class)){
                            int id = cursor.getInt(cursor.getColumnIndex("id"));
                            entity = (T) constructor.newInstance(id);
                            Log.i(TAG,"Se instancia el objecto usando ID: "+id);
                            break;
                        }
                    }

                    if(entity==null){
                        entity = subClass.newInstance();
                        Log.i(TAG,"Se instancia el objecto sin usar parametros en el constructor");
                    }

                    for (Field field : campos){

                        Object value = null;
                        String typeName = field.getType().getSimpleName();
                        int columnIndex = cursor.getColumnIndex(field.getName());
                        if(typeName.equals("String")){
                            value = cursor.getString(columnIndex);
                        }else if(typeName.equalsIgnoreCase("double")){
                            value = cursor.getDouble(columnIndex);
                        }else if(typeName.equalsIgnoreCase("float")){
                            value = new Double(cursor.getDouble(columnIndex)).floatValue();
                        }else if(typeName.equalsIgnoreCase("int") || typeName.equalsIgnoreCase("integer")){
                            value = cursor.getInt(columnIndex);
                        } else {
                            try {
                                value = cursor.getInt(columnIndex);
                                Class<T> c = (Class<T>) Class.forName(field.getType().getName());
                                value = ReflectionHelper.newInstance(c, (Integer) value);
                            } catch (Exception e){

                            }

                        }

                        Method method = entity.obtainSetMethod(field);
                        if(method!=null && value!=null){
                            entity.putValue(method,value);
                        }

                    }

                    Log.i(TAG,"Se agrega una instancia de la clase a la lista de entidades");

                    entities.add(entity);




                } while(cursor.moveToNext());

                Log.i(TAG, "Se obtuvieron " + entities.size() + " entidades desde el JSONArray");

                return entities;

            }

            return entities;

        }catch (Exception e){
            Log.i(TAG,"Error obteniendo la lista de entidades",e);
            return null;
        }
    }

}
