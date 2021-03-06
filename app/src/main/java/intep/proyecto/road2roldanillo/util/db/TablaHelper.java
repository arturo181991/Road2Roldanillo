package intep.proyecto.road2roldanillo.util.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Date;

import intep.proyecto.road2roldanillo.util.DateHelper;

/**
 * Created by gurzaf on 1/7/15.
 */
public class TablaHelper implements Serializable {

    protected final String TAG = this.getClass().getSimpleName();

    public ContentValues getContent(){
        return getContent(true);
    }

    public ContentValues getContent(boolean todos){

        Log.i(TAG,"Se obtiene el contenido de la entidad ".concat(this.getClass().getSimpleName()));

        Field[] fields = this.getClass().getDeclaredFields();
        ContentValues values = new ContentValues();

        if(todos) {
            try {
                TablaEntidadHelper ob = (TablaEntidadHelper) this;
                values.put("id", ob.getId());
                Log.i(TAG, "El valor del id es: " + ob.getId());
            } catch (Exception e) {
                Log.i(TAG, "La clase no hereda de TablaEntidadHelper", e);
            }
        }

        for (Field field : fields) {

            if(field.getModifiers()!= Modifier.PROTECTED){
                Log.i(TAG,"Campo: ".concat(field.getName()));

                Method method = obtainGetMethod(field);

                Object value = null;
                if(method!=null){
                    value = obtainValue(method);
                }else{
                    Log.i(TAG,"No se encontró método para el campo");
                    continue;
                }

                if(value!=null){

                    Log.i(TAG, "El valor es: ".concat(value.toString()));

                    if(value instanceof Date){
                        values.put(field.getName(), DateHelper.getDateFormat().format((Date)value));
                    }else{
                        values.put(field.getName(),value.toString());
                    }

                }else{
                    Log.i(TAG,"No hay valor");
                }
            }

        }

        if(values.size()!=0){
            return values;
        }

        return null;

    }

    private String capitalize(String line){
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

    public long insert(SQLiteDatabase db){
        Log.i(TAG,"Ejecutando método INSERT a la entidad "
                .concat(this.getClass().getSimpleName())
                .concat(this.toString()));
        return db.insert(this.getClass().getSimpleName(),null,getContent());
    }

    public Method obtainGetMethod(Field field){
        Method method = null;
        try {
            method = this.getClass().getDeclaredMethod("get".concat(capitalize(field.getName())));
        }catch (NoSuchMethodException ne){

            Log.e(TAG,"No se encontró el método con GET",ne);

            try {
                method = this.getClass().getDeclaredMethod("is".concat(capitalize(field.getName())));
            }catch (NoSuchMethodException nee){

            }
        }
        return method;
    }

    public Method obtainSetMethod(Field field){
        Method method = null;
        try {
            method = this.getClass().getDeclaredMethod("set".concat(capitalize(field.getName())),field.getType());
        }catch (NoSuchMethodException ne){
            Log.e(TAG,"No se encontró el método con SET",ne);
        }
        return method;
    }

    public Object obtainValue(Method method){
        Log.i(TAG,"El método para el campo es: ".concat(method.getName()));
        try {
            Object value = method.invoke(this, null);
            return value;
        }catch (IllegalAccessException | InvocationTargetException ne){
            Log.i(TAG,"No se pudo obtener el valor para ese método");
            return null;
        }
    }

    public void putValue(Method method, Object value){
        Log.i(TAG,"El método para asignar el campo es: ".concat(method.getName()));
        try {
            method.invoke(this,value);
        }catch (IllegalAccessException | InvocationTargetException ne){
            Log.i(TAG,"No se pudo asignar el valor al campo.");
        }
    }



}
