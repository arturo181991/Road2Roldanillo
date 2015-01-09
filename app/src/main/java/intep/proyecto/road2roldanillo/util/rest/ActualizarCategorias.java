package intep.proyecto.road2roldanillo.util.rest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.List;

import intep.proyecto.road2roldanillo.R;
import intep.proyecto.road2roldanillo.entidades.db.Categoria;
import intep.proyecto.road2roldanillo.persistencia.DBHelper;
import intep.proyecto.road2roldanillo.rest.ImageHelper;
import intep.proyecto.road2roldanillo.rest.RESTHelper;
import intep.proyecto.road2roldanillo.util.CategoriaDrawerListAdapter;
import intep.proyecto.road2roldanillo.util.NavigationDrawerListAdapter;
import intep.proyecto.road2roldanillo.util.db.TablaHelper;

/**
 * Created by gurzaf on 1/7/15.
 */
public class ActualizarCategorias extends AsyncTask<String,Void,Boolean> {

    private static final String TAG = ActualizarCategorias.class.getSimpleName();

    private List entidades;

    private Context context;
    private ListView listView;
    private Class subClass;

    public<T extends TablaHelper> ActualizarCategorias(Context context, ListView listView, Class<T> subClass){
        this.context = context;
        this.listView = listView;
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
                CategoriaDrawerListAdapter adaptador = new CategoriaDrawerListAdapter(context, R.layout.menu_item_row,entidades);
                listView.setAdapter(adaptador);
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
            return true;

        }catch (Exception e){

            Log.e(TAG,"Error insertando las categorias",e);
            return false;

        }

    }

}
