package intep.proyecto.road2roldanillo.util.rest;

import android.app.ProgressDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.List;

import intep.proyecto.road2roldanillo.ActualizarDatos;
import intep.proyecto.road2roldanillo.entidades.db.Categoria;
import intep.proyecto.road2roldanillo.persistencia.DBHelper;
import intep.proyecto.road2roldanillo.rest.ImageHelper;
import intep.proyecto.road2roldanillo.rest.RESTHelper;
import intep.proyecto.road2roldanillo.util.UIHelper;
import intep.proyecto.road2roldanillo.util.db.TablaHelper;

/**
 * Created by gurzaf on 1/7/15.
 */
public class ActualizarCategorias extends AsyncTask<String,Void,Boolean> {

    private static final String TAG = ActualizarCategorias.class.getSimpleName();

    private List entidades;

    private ActualizarDatos actualizarDatos;

    public<T extends TablaHelper> ActualizarCategorias(ActualizarDatos actualizarDatos){
        this.actualizarDatos = actualizarDatos;
    }

    @Override
    protected Boolean doInBackground(String... strings) {

        actualizarDatos.setTextProgressDialog("Actualizando Categorias ...");

        try {


            JSONArray jsonArray = RESTHelper.getJSONCategorias();

            if (jsonArray != null && jsonArray.length() > 0) {

                entidades = RESTHelper.getListadoEntidades(Categoria.class, jsonArray);

                if (entidades != null && !entidades.isEmpty()) {

                    for (Object entidad : entidades) {
                        if (entidad instanceof Categoria) {
                            if (!ImageHelper.saveImageForCategoria((Categoria) entidad, actualizarDatos)) {
                                entidades = null;
                                return false;
                            }
                        }
                    }

                } else {
                    entidades = null;
                    return false;
                }

                return true;
            } else {
                entidades = null;
                return false;
            }
        }catch (Exception e){

            Log.w(TAG,"Error obteniendo las categorias desde el servicio Web",e);

            return false;

        }

    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {

        if(aBoolean && entidades!=null) {

            DBHelper dbHelper = new DBHelper(actualizarDatos);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            if(insertarRegistros(db, entidades)){

                Toast.makeText(actualizarDatos,"Se actualizaron las categorias, por favor vuelve a intentarlo",Toast.LENGTH_SHORT).show();

            }else{

                UIHelper.addLabelMessageToList("No se actualizaron las Categorias, por favor vuelve a intentarlo", actualizarDatos, actualizarDatos.getLinearLayout());

            }

            ActualizarLugares actualizarLugares = new ActualizarLugares(actualizarDatos);
            actualizarLugares.execute();

        }else{

            String message = "No está disponible el servicio Web";
            UIHelper.addLabelMessageToList(message, actualizarDatos,actualizarDatos.getLinearLayout());

            actualizarDatos.hideProgressDialog();

        }




    }

    private boolean insertarRegistros(SQLiteDatabase db, List<Categoria> categorias) {

        try {

            int registros = 0;

            for (Categoria categoria: categorias){
                if(categoria instanceof TablaHelper){

                    if(categoria.getBorrado()==1) {
                        if (categoria.remove(db) == 1) {
                            registros++;
                        }
                    }else if(categoria.existInDatabase(db)){
                        if(categoria.update(db,categoria.getId())!=-1){
                            registros++;
                        }
                    }else if(categoria.insert(db)!=-1){
                        registros++;
                    }
                }
            }

            Log.i(TAG,"Se actualizaron "+registros+" registros.");

            final int resultado = registros;

            String message = "Se actualizaron "+resultado+" Categoria(s).";
            UIHelper.addLabelMessageToList(message, actualizarDatos, actualizarDatos.getLinearLayout());

            return true;

        }catch (Exception e){

            Log.e(TAG,"Error insertando las categorias",e);
            return false;

        }

    }

}
