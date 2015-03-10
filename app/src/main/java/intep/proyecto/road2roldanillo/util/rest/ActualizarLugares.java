package intep.proyecto.road2roldanillo.util.rest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.internal.lu;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import intep.proyecto.road2roldanillo.ActualizarDatos;
import intep.proyecto.road2roldanillo.R;
import intep.proyecto.road2roldanillo.entidades.db.Foto;
import intep.proyecto.road2roldanillo.entidades.db.Lugar;
import intep.proyecto.road2roldanillo.persistencia.DBHelper;
import intep.proyecto.road2roldanillo.rest.ImageHelper;
import intep.proyecto.road2roldanillo.rest.RESTHelper;
import intep.proyecto.road2roldanillo.util.db.TablaHelper;

/**
 * Created by gurzaf on 1/23/15.
 */
public class ActualizarLugares extends AsyncTask<String,Void,Boolean> {

    private static final String TAG = ActualizarLugares.class.getSimpleName();
    private List<Lugar> entidades;
    private LinearLayout linearLayout;
    private ActualizarDatos context;

    public ActualizarLugares(ActualizarDatos context, LinearLayout linearLayout){
        this.context = context;
        this.linearLayout = linearLayout;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        JSONArray jsonArray = RESTHelper.getJSONLugares();

        if(jsonArray!=null && jsonArray.length()>0){

            entidades = RESTHelper.getListadoEntidades(Lugar.class,jsonArray);

            if(entidades!=null && !entidades.isEmpty()){

                for (int i = 0; i < entidades.size(); i++){
                    Lugar entidad = entidades.get(i);
                    try {
                        JSONArray fotosArray = jsonArray.getJSONObject(i).getJSONArray("fotos");
                        List<Foto> fotos = RESTHelper.getListadoEntidades(Foto.class,fotosArray);
                        Log.i(TAG,"Fotos para el lugar: ".concat(entidad.getNombre())
                                    .concat(" => "+fotos));
                        if(fotos!=null && !fotos.isEmpty()) {
                            Log.i(TAG,"Cantidad de fotos: "+fotos.size());
                            if (!ImageHelper.saveImageForLugar(entidad.getNombre(), fotos, context)) {
                                entidades = null;
                                return false;
                            }
                            entidad.setFotos(fotos);
                        }
                    }catch (Exception e){
                        Log.w(TAG,"No se encontraron fotos en el JSON para el lugar ".concat(entidad.getNombre()),e);
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

            DBHelper dbHelper = new DBHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            if(insertarRegistros(db, entidades)){
                Toast.makeText(context,"Se insertaron los lugares",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context,"Error XD",Toast.LENGTH_LONG).show();
            }

        }
    }

    private boolean insertarRegistros(SQLiteDatabase db, List<Lugar> entidades) {

        try {

            int registros = 0;

            for (Lugar lugar : entidades){
                db.beginTransaction();
                try {
                    lugar.insert(db);
                    Log.i(TAG,"Se inserta el Lugar: ".concat(lugar.getNombre()));
                    if(lugar.getFotos()!=null) {
                        Log.i(TAG,"El lugar tiene fotos: "+lugar.getFotos().size());
                        for (Foto foto : lugar.getFotos()) {
                            foto.setLugar(lugar);
                            foto.insert(db);
                            Log.i(TAG,"Se inserta la foto: ".concat(foto.getFoto()));
                        }
                    }
                    db.setTransactionSuccessful();
                    registros++;
                }catch (Exception e){
                    Log.e(TAG,"Error persistiendo el lugar: ".concat(lugar.getNombre()),e);
                }finally {
                    db.endTransaction();
                }
            }

            final int resultado = registros;

            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView etiqueta = (TextView)context.getLayoutInflater().inflate(R.layout.label_actualizado, null);
                    etiqueta.setTextAppearance(context,R.style.fuente_label_actualizado_lugares);
                    etiqueta.setText("Se insertaron " + resultado + " Lugar(es).");
                    linearLayout.addView(etiqueta);
                }
            });


            Log.i(TAG,"Se insertaron "+registros+".");
            return true;

        }catch (Exception e){

            Toast.makeText(context,"Error insertando los lugares",Toast.LENGTH_SHORT);
            Log.e(TAG,"Error insertando los lugares",e);
            return false;

        }

    }
}
