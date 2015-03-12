package intep.proyecto.road2roldanillo.rest;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import intep.proyecto.road2roldanillo.entidades.db.Comentario;
import intep.proyecto.road2roldanillo.persistencia.DBHelper;

/**
 * Created by gurzaf on 3/11/15.
 */
public class PushComentarioHelper {

    private static final String TAG = PushComentarioHelper.class.getSimpleName();

    private static IdsPares[] Post(String url, String content){
        InputStream inputStream = null;
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);


            StringEntity se = new StringEntity(content);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            if(httpResponse.getStatusLine().getStatusCode()== HttpStatus.SC_OK){

                BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

                String line;
                String result = "";
                while((line = reader.readLine()) != null)
                    result += line;
                
                IdsPares[] ids = new Gson().fromJson(result,IdsPares[].class);

                return ids;

            }

        } catch (Exception e) {
            Log.d(TAG, "Error con petición POST", e);
        }

        return null;
    }

     public void enviarComentarios(Activity activity){
         new HttpAsyncTask(activity).execute();
     }

    private class HttpAsyncTask extends AsyncTask<String, Void, IdsPares[]> {

        private List<Comentario> comentarioList;
        private Activity context;

        public HttpAsyncTask(Activity activity){
            this.context = activity;
        }

        @Override
        protected IdsPares[] doInBackground(String... objects) {

            SQLiteDatabase db = new DBHelper(context).getReadableDatabase();
            comentarioList = Comentario.getAllValues(db,false);

            Log.i(TAG,"Se obtuvieron "+comentarioList.size()+" comentarios para enviar.");

            final String json = new Gson().toJson(comentarioList);

            Log.i(TAG,"Se codificaron los comentarios en JSON");
            Log.i(TAG,json);

            String url = RESTHelper.getURLPutComments();

            return PushComentarioHelper.Post(url, json);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(IdsPares[] result) {
            String mensaje = "Data not sent!";
            if(result!=null){

                Log.i(TAG,"Comentarios enviados");

                SQLiteDatabase db = new DBHelper(context).getWritableDatabase();

                for (IdsPares idPares : result){

                    Comentario comentario = Comentario.find(db, idPares.getIdDispositivo());
                    if(comentario!=null){
                        comentario.setId(idPares.getIdServidor());
                        comentario.setSubido(1);
                        comentario.update(db, idPares.getIdDispositivo());
                        Log.i(TAG, "Se subió el comentario: " + comentario.getId());
                    }

                }

            }else{
                Log.w(TAG, "Los datos no fueron enviados");
            }
        }
    }

    private class IdsPares{
        private Integer idServidor, idDispositivo;

        public IdsPares(Integer idServidor, Integer idDispositivo) {
            this.idServidor = idServidor;
            this.idDispositivo = idDispositivo;
        }

        public Integer getIdServidor() {
            return idServidor;
        }

        public void setIdServidor(Integer idServidor) {
            this.idServidor = idServidor;
        }

        public Integer getIdDispositivo() {
            return idDispositivo;
        }

        public void setIdDispositivo(Integer idDispositivo) {
            this.idDispositivo = idDispositivo;
        }
    }

}
