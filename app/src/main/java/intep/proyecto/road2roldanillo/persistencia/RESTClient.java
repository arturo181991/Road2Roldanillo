package intep.proyecto.road2roldanillo.persistencia;

import android.util.Log;
import android.widget.ArrayAdapter;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import intep.proyecto.road2roldanillo.Categorias;
import intep.proyecto.road2roldanillo.R;

public class RESTClient{


    public void getCategorias(){
        HttpClient httpClient = new DefaultHttpClient();
        /*HttpPost httpPost = new HttpPost("192.168.1.10:8080/Road2RoldanilloWS/generic");
        httpPost.setHeader("content-type","application/json");
        HttpClient httpClient = new DefaultHttpClient();*/

        HttpGet del = new HttpGet("http://192.168.1.10:8080/Road2RoldanilloWS/datos/get/categoria");
        del.setHeader("content-type", "application/json");

        try {
            HttpResponse httpResponse = httpClient.execute(del);
            String respStr = EntityUtils.toString(httpResponse.getEntity());
            JSONArray jsonArray = new JSONArray(respStr);
            String [] categorias = new String[jsonArray.length()];
            for (int i = 0; i<jsonArray.length(); i++){
                JSONObject dato = jsonArray.getJSONObject(i);
                int idCategoria = dato.getInt("id");
                String nombreCategoria = dato.getString("nombre");
                String iconoCategoria = dato.getString("icono");
                categorias[i] = "" + idCategoria + "-" + nombreCategoria + "-" + iconoCategoria;
            }
           //ArrayAdapter<String> adaptador = new ArrayAdapter<String>(RESTClient.this, android.R.layout.simple_list_item_1, categorias);

        }catch (Exception exc){
            Log.e("Servicio REST", "Error!", exc);
        }

        //JSONObject dato = new JSONObject();

    }





}
