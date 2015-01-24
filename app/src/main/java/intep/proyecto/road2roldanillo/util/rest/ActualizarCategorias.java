package intep.proyecto.road2roldanillo.util.rest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.List;

import intep.proyecto.road2roldanillo.ActualizarDatos;
import intep.proyecto.road2roldanillo.R;
import intep.proyecto.road2roldanillo.entidades.db.Categoria;
import intep.proyecto.road2roldanillo.persistencia.DBHelper;
import intep.proyecto.road2roldanillo.rest.ImageHelper;
import intep.proyecto.road2roldanillo.rest.RESTHelper;
import intep.proyecto.road2roldanillo.util.db.TablaHelper;

/**
 * Created by gurzaf on 1/7/15.
 */
public class ActualizarCategorias extends AsyncTask<String,Void,Boolean> {

    private static final String TAG = ActualizarCategorias.class.getSimpleName();

    private List entidades;

    private ActualizarDatos context;
    private LinearLayout linearLayout;
    private Class subClass;

    public<T extends TablaHelper> ActualizarCategorias(ActualizarDatos context, LinearLayout linearLayout, Class<T> subClass){
        this.context = context;
        this.linearLayout = linearLayout;
        this.subClass = subClass;
    }

    @Override
    protected Boolean doInBackground(String... strings) {

        JSONArray jsonArray = RESTHelper.getJSONCategorias();

        if(jsonArray!=null && jsonArray.length()>0){

            entidades = RESTHelper.getListadoEntidades(subClass,jsonArray);

            if(entidades!=null && !entidades.isEmpty()){

                for (Object entidad : entidades){
                    if(entidad instanceof Categoria){
                        if(!ImageHelper.saveImageForCategoria((Categoria) entidad, context)){
                            entidades = null;
                            return false;
                        }
                    }
                }

            }else{
                entidades = null;
                return false;
            }

            return true;
        }else{
            entidades = null;
            return false;
        }

    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {

        if(aBoolean && entidades!=null) {

            Toast.makeText(context, "Arreglo de categorias-> " + entidades.size(), Toast.LENGTH_SHORT).show();

            DBHelper dbHelper = new DBHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            if(insertarRegistros(db, entidades)){

                Toast.makeText(context,"Se insertaron las categorias",Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(context,"Error XD",Toast.LENGTH_LONG).show();
            }


        }

    }

    private boolean insertarRegistros(SQLiteDatabase db, List entidades) {

        try {

            int registros = 0;

            for (Object entidad: entidades){
                if(entidad instanceof TablaHelper){
                    if(((TablaHelper)entidad).insert(db)!=-1){
                        registros++;
                    }
                }
            }

            Log.i(TAG,"Se insertaron "+registros+".");

            final int resultado = registros;

            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    TextView etiqueta = (TextView) context.getLayoutInflater().inflate(R.layout.label_actualizado, null);
                    etiqueta.setTextAppearance(context,R.style.fuente_label_actualizado_categorias);
                    etiqueta.setText("Se insertaron "+resultado+" Categoria(s).");
                    linearLayout.addView(etiqueta);

                }
            });



            return true;

        }catch (Exception e){

            Log.e(TAG,"Error insertando las categorias",e);
            return false;

        }

    }

}
