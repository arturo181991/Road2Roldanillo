package intep.proyecto.road2roldanillo.util.rest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import intep.proyecto.road2roldanillo.entidades.db.CategoriaDB;
import intep.proyecto.road2roldanillo.persistencia.DBHelper;

/**
 * Created by gurzaf on 1/7/15.
 */
public class ActualizarCategorias extends AsyncTask<String,Void,Boolean> {

    private static final String TAG = ActualizarCategorias.class.getSimpleName();

    private List<CategoriaDB> categorias;

    private Context context;
    private ListView listView;

    public ActualizarCategorias(Context context, ListView listView){
        this.context = context;
        this.listView = listView;
        categorias = new ArrayList<CategoriaDB>();
    }

    @Override
    protected Boolean doInBackground(String... strings) {

        HttpClient httpClient = new DefaultHttpClient();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-10);

        Long horaTimeStamp = calendar.getTimeInMillis();

        HttpGet httpGet = new HttpGet("http://192.168.1.2:8080/Road2RoldanilloWS/datos/categoria/get/"+ horaTimeStamp);
        httpGet.setHeader("content-type", "application/json");

        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            String respStr = EntityUtils.toString(httpResponse.getEntity());
            JSONArray jsonArray = new JSONArray(respStr);
            for (int i = 0; i<jsonArray.length(); i++){
                JSONObject dato = jsonArray.getJSONObject(i);

                Field[] campos = CategoriaDB.class.getDeclaredFields();

                int idCategoria = dato.getInt("id");

                CategoriaDB categoriaDB = new CategoriaDB(idCategoria);

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


                    Method method = categoriaDB.obtainSetMethod(field);
                    if(method!=null && value!=null){
                        categoriaDB.putValue(method,value);
                    }

                }

                categorias.add(categoriaDB);
            }

            return true;

        }catch (Exception exc){
            exc.printStackTrace();
            return false;
        }

    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {

        if(aBoolean && !categorias.isEmpty()) {

            Toast.makeText(context, "Arreglo de categorias-> " + categorias, Toast.LENGTH_SHORT).show();

            DBHelper dbHelper = new DBHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            if(insertarCategorias(db, categorias)){
                ArrayAdapter<Object> adaptador = new ArrayAdapter<Object>(context, android.R.layout.simple_list_item_1, categorias.toArray());
                listView.setAdapter(adaptador);
            }else{
                Toast.makeText(context,"Error XD",Toast.LENGTH_LONG).show();
            }


        }

    }

    private boolean insertarCategorias(SQLiteDatabase db, List<CategoriaDB> categorias) {

        try {

            int registros = 0;

            for (CategoriaDB categoriaDB : categorias){
                if(categoriaDB.insert(db)!=-1){
                    registros++;
                }
            }

            Log.i(TAG,"Se insertaron "+registros+" categorias.");
            return true;
        }catch (Exception e){
            Log.e(TAG,"Error insertando las categorias",e);
            return false;
        }

    }

}
