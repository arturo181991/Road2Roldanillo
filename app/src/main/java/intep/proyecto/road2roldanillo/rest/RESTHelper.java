package intep.proyecto.road2roldanillo.rest;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import intep.proyecto.road2roldanillo.entidades.db.Lugar;
import intep.proyecto.road2roldanillo.util.Constantes;
import intep.proyecto.road2roldanillo.util.ReflectionHelper;
import intep.proyecto.road2roldanillo.util.db.TablaEntidadHelper;
import intep.proyecto.road2roldanillo.util.db.TablaHelper;

/**
 * Created by gurzaf on 1/9/15.
 */
public class RESTHelper {

    private static final String TAG = RESTHelper.class.getSimpleName();

    private static JSONArray getObjects(String url){
        HttpClient httpClient = new DefaultHttpClient();

        HttpGet method = new HttpGet(url);
        method.setHeader("content-type", "application/json");

        Log.i(TAG,"Se obtendran datos JSON de la URL: ".concat(url));

        try {
            HttpResponse httpResponse = httpClient.execute(method);
            String respStr = EntityUtils.toString(httpResponse.getEntity());
            JSONArray jsonArray = new JSONArray(respStr);
            Log.i(TAG,"Se obtuvo el array con "+jsonArray.length()+" registro(s)");
            return jsonArray;
        }catch (Exception exc){
            Log.e(TAG, "Ocurrió un error obteniendo los datos JSON desde la URL", exc);
        }
        return null;
    }

    public static JSONArray getJSONComentarios(Lugar lugar){
        String url = Constantes.concatPath(
                Constantes.getBASE_PATH(),
                Constantes.COMENTARIOS_PATH,
                lugar.getId()+"",
                Constantes.getTimeStampAsString());
        Log.i(TAG,"Se generó la URL para obtener los datos JSON de los comentarios");
        return getObjects(url);
    }

    public static JSONArray getJSONCategorias(){
        String url = Constantes.concatPath(
                Constantes.getBASE_PATH(),
                Constantes.CATEGORIAS_PATH,
                Constantes.getTimeStampAsString());
        Log.i(TAG,"Se generó la URL para obtener los datos JSON de las categorias");
        return getObjects(url);
    }

    public static JSONArray getJSONLugares(){
        String url = Constantes.concatPath(
                Constantes.getBASE_PATH(),
                Constantes.LUGARES_PATH,
                Constantes.getTimeStampAsString());
        Log.i(TAG,"Se generó la URL para obtener los datos JSON de los lugares");
        JSONArray array = getObjects(url);
        Log.i(TAG,array.toString());
        return array;
    }


    public static <T extends TablaHelper> List<T> getListadoEntidades(Class<T> subClass, JSONArray jsonArray){
        try {

            List<T> entities = new ArrayList<>();

            Log.i(TAG,"Tamaño del ArrayJSON: "+jsonArray.length());

            for (int i = 0; i<jsonArray.length(); i++){
                JSONObject dato = jsonArray.getJSONObject(i);

                Log.i(TAG,"Se obtiene el JSONObject");

                Field[] campos = subClass.getDeclaredFields();

                Log.i(TAG,"Se obtienen los campos de la clase: ".concat(subClass.getSimpleName()));

                int id = dato.getInt("id");
                T entity= ReflectionHelper.newInstance(subClass, id);

                if(entity==null){
                    entity = subClass.newInstance();
                    Log.i(TAG,"Se instancia el objecto sin usar parametros en el constructor");
                }

                for (Field field : campos){

                    if(field.getModifiers() != Modifier.PROTECTED) {
                        Object value = null;
                        String typeName = field.getType().getSimpleName();
                        if (typeName.equals("String")) {
                            value = dato.getString(field.getName());
                        } else if (typeName.equalsIgnoreCase("double")) {
                            value = dato.getDouble(field.getName());
                        } else if (typeName.equalsIgnoreCase("float")) {
                            value = new Double(dato.getDouble(field.getName())).floatValue();
                        } else if (typeName.equalsIgnoreCase("int") || typeName.equalsIgnoreCase("integer")) {
                            value = dato.getInt(field.getName());
                        } else if (field.getType().getSuperclass().equals(TablaEntidadHelper.class)) {
                            try {
                                value = dato.getInt(field.getName());
                                Class<T> c = (Class<T>) Class.forName(field.getType().getName());
                                value = ReflectionHelper.newInstance(c, (Integer) value);
                            }catch (Exception e){

                            }

                        }

                        Method method = entity.obtainSetMethod(field);
                        if (method != null && value != null) {
                            entity.putValue(method, value);
                        }
                    }

                }

                Log.i(TAG,"Se agrega una instancia de la clase a la lista de entidades");

                entities.add(entity);
            }

            Log.i(TAG,"Se obtuvieron "+entities.size()+" entidades desde el JSONArray");

            return entities;

        }catch (Exception e){
            Log.i(TAG,"Error obteniendo la lista de entidades",e);
            return null;
        }
    }




}
