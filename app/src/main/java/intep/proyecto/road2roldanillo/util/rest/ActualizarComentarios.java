package intep.proyecto.road2roldanillo.util.rest;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.List;

import intep.proyecto.road2roldanillo.CommentsFragment;
import intep.proyecto.road2roldanillo.entidades.db.Categoria;
import intep.proyecto.road2roldanillo.entidades.db.Comentario;
import intep.proyecto.road2roldanillo.entidades.db.Lugar;
import intep.proyecto.road2roldanillo.persistencia.DBHelper;
import intep.proyecto.road2roldanillo.rest.ImageHelper;
import intep.proyecto.road2roldanillo.rest.RESTHelper;
import intep.proyecto.road2roldanillo.util.UIHelper;
import intep.proyecto.road2roldanillo.util.db.TablaHelper;

/**
 * Created by gurzaf on 3/11/15.
 */
public class ActualizarComentarios extends AsyncTask<String,Void,Boolean> {

    private Lugar lugar;
    private CommentsFragment commentsFragment;
    private Activity activity;

    private List<Comentario> entidades;

    private static final String TAG = ActualizarComentarios.class.getSimpleName();

    public ActualizarComentarios(Lugar lugar, CommentsFragment commentsFragment){
        this.lugar = lugar;
        this.commentsFragment = commentsFragment;
        this.activity = commentsFragment.getActivity();
    }

    public ActualizarComentarios(Lugar lugar, Activity activity){
        this.lugar = lugar;
        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(String... strings) {

        try {


            JSONArray jsonArray = RESTHelper.getJSONComentarios(lugar);

            if (jsonArray != null && jsonArray.length() > 0) {

                entidades = RESTHelper.getListadoEntidades(Comentario.class, jsonArray);

                if (entidades != null && !entidades.isEmpty()) {

                    return true;

                } else {
                    entidades = null;
                    return false;
                }

            } else {
                entidades = null;
                return false;
            }
        }catch (Exception e){

            Log.w(TAG, "Error obteniendo los comentarios desde el servicio Web", e);

            return false;

        }

    }


    @Override
    protected void onPostExecute(Boolean aBoolean) {

        if(aBoolean && entidades!=null) {

            DBHelper dbHelper = new DBHelper(activity);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            if(insertarRegistros(db, entidades)){

                Toast.makeText(activity,"Se actualizaron los comentarios",Toast.LENGTH_SHORT).show();

            }else{

                Toast.makeText(activity,"No se actualizaron las Categorias, por favor vuelve a intentarlo",Toast.LENGTH_SHORT).show();

            }

        }else{

            String message = "No está disponible el servicio Web";

            Toast.makeText(activity,message,Toast.LENGTH_SHORT).show();

        }

        if(commentsFragment!=null){
            commentsFragment.updateCommentList();
            commentsFragment.hideRefreshing();
        }
    }

    private boolean insertarRegistros(SQLiteDatabase db, List<Comentario> comentarios) {

        try {

            int registros = 0;

            for (Comentario comentario: comentarios){


                    if(comentario.getBorrado()==1) {
                        if (comentario.remove(db) == 1) {
                            registros++;
                        }
                    }else if(comentario.existInDatabase(db)){
                        if(comentario.update(db,comentario.getId())!=-1){
                            registros++;
                        }
                    }else if(comentario.insert(db)!=-1){
                        registros++;
                    }
                }

            Log.i(TAG,"Se actualizaron "+registros+" registros.");

            final int resultado = registros;

            final String message = "Se actualizaron "+resultado+" Categoria(s).";
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity,message,Toast.LENGTH_SHORT).show();
                }
            });

            return true;

        }catch (Exception e){

            Log.e(TAG,"Error insertando las categorias",e);
            return false;

        }

    }

}
