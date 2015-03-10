package intep.proyecto.road2roldanillo.util;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import intep.proyecto.road2roldanillo.util.db.TablaHelper;

/**
 * Created by arturo on 24-01-15.
 */
public class ReflectionHelper {

    private static final String TAG = ReflectionHelper.class.getSimpleName();

    public static <T extends TablaHelper> T newInstance(Class<T> subClass, int id) throws JSONException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor constructor = getIdConstructor(subClass);
        if(constructor==null){
            return null;
        }
        T entity = (T) constructor.newInstance(id);
        Log.i(TAG, "Se instancia el objecto usando el ID: "+id);
        return entity;
    }

  /*  public static <T extends TablaHelper> T newInstance(Class<T> subClass, JSONObject dato) throws JSONException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor constructor = getIdConstructor(subClass);
        if(constructor==null){
            return null;
        }
        T entity = (T) constructor.newInstance(dato.getInt("id"));
        Log.i(TAG,"Se instancia el objecto usando ID");
        return entity;
    }*/

    private static <T extends TablaHelper> Constructor getIdConstructor(Class<T> subClass){
        for (Constructor constructor : subClass.getConstructors()) {
            if(constructor.getParameterTypes().length==1 && constructor.getParameterTypes()[0].equals(int.class)){
                return constructor;
            }
        }
        return null;
    }

}
