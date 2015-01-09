package intep.proyecto.road2roldanillo.rest;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import intep.proyecto.road2roldanillo.util.Constantes;
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

    public static JSONArray getJSONCategorias(){
        String url = Constantes.concatPath(
                Constantes.BASE_PATH,
                Constantes.CATEGORIAS_PATH,
                Constantes.getTimeStampAsString());
        Log.i(TAG,"Se generó la URL para obtener los datos JSON de las categorias");
        return getObjects(url);
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

                T entity=null;

                for (Constructor constructor : subClass.getConstructors()) {
                    if(constructor.getParameterTypes().length==1 && constructor.getParameterTypes()[0].equals(int.class)){
                        int id = dato.getInt("id");
                        entity = (T) constructor.newInstance(id);
                        Log.i(TAG,"Se instancia el objecto usando ID");
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
                    if(typeName.equals("String")){
                        value = dato.getString(field.getName());
                    }else if(typeName.equalsIgnoreCase("double")){
                        value = dato.getDouble(field.getName());
                    }else if(typeName.equalsIgnoreCase("float")){
                        value = new Double(dato.getDouble(field.getName())).floatValue();
                    }else if(typeName.equalsIgnoreCase("int") || typeName.equalsIgnoreCase("integer")){
                        value = dato.getInt(field.getName());
                    }

                    Method method = entity.obtainSetMethod(field);
                    if(method!=null && value!=null){
                        entity.putValue(method,value);
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
