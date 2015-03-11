package intep.proyecto.road2roldanillo.util.rest;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.List;

import intep.proyecto.road2roldanillo.ActualizarDatos;
import intep.proyecto.road2roldanillo.entidades.db.Foto;
import intep.proyecto.road2roldanillo.entidades.db.Lugar;
import intep.proyecto.road2roldanillo.persistencia.DBHelper;
import intep.proyecto.road2roldanillo.rest.ImageHelper;
import intep.proyecto.road2roldanillo.rest.RESTHelper;
import intep.proyecto.road2roldanillo.util.UIHelper;

/**
 * Created by gurzaf on 1/23/15.
 */
public class ActualizarLugares extends AsyncTask<String,Void,Boolean> {

    private static final String TAG = ActualizarLugares.class.getSimpleName();
    private List<Lugar> entidades;
    private ActualizarDatos actualizarDatos;

    public ActualizarLugares(ActualizarDatos actualizarDatos){
        this.actualizarDatos = actualizarDatos;
    }

    @Override
    protected Boolean doInBackground(String... strings) {

        actualizarDatos.setTextProgressDialog("Actualizando Lugares ...");

        try {

            JSONArray jsonArray = RESTHelper.getJSONLugares();

            if (jsonArray != null && jsonArray.length() > 0) {

                entidades = RESTHelper.getListadoEntidades(Lugar.class, jsonArray);

                if (entidades != null && !entidades.isEmpty()) {

                    for (int i = 0; i < entidades.size(); i++) {
                        Lugar entidad = entidades.get(i);
                        try {
                            JSONArray fotosArray = jsonArray.getJSONObject(i).getJSONArray("fotos");
                            List<Foto> fotos = RESTHelper.getListadoEntidades(Foto.class, fotosArray);
                            Log.i(TAG, "Fotos para el lugar: ".concat(entidad.getNombre())
                                    .concat(" => " + fotos));
                            if (fotos != null && !fotos.isEmpty()) {
                                Log.i(TAG, "Cantidad de fotos: " + fotos.size());
                                if (!ImageHelper.saveImageForLugar(entidad.getNombre(), fotos, actualizarDatos)) {
                                    entidades = null;
                                    return false;
                                }
                                entidad.setFotos(fotos);
                            }
                        } catch (Exception e) {
                            Log.w(TAG, "No se encontraron fotos en el JSON para el lugar ".concat(entidad.getNombre()), e);
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
            Log.e(TAG,"Error actualizando los Lugares desde el servicio Web",e);
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if(aBoolean && entidades!=null) {

            DBHelper dbHelper = new DBHelper(actualizarDatos);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            if(insertarRegistros(db, entidades)){
                Toast.makeText(actualizarDatos,"Se insertaron los lugares",Toast.LENGTH_SHORT).show();
            }else{
                UIHelper.addLabelMessageToList("No actualizaron los Lugares, por favor vuelve a intentarlo.", actualizarDatos, actualizarDatos.getLinearLayout());
            }

        }else{

            UIHelper.addLabelMessageToList("No se actualizaron los Lugares, por favor vuelve a intentarlo", actualizarDatos, actualizarDatos.getLinearLayout());

        }

        actualizarDatos.hideProgressDialog();

    }

    private boolean insertarRegistros(SQLiteDatabase db, List<Lugar> entidades) {

        try {

            int registros = 0;

            for (Lugar lugar : entidades){
                db.beginTransaction();
                try {

                    DBHelper.ACTION action;

                    if(lugar.getBorrado()==1){
                        action = DBHelper.ACTION.DELETE;
                    }else if(lugar.existInDatabase(db)){
                        action = DBHelper.ACTION.UPDATE;
                    }else{
                        action = DBHelper.ACTION.INSERT;
                    }

                    switch (action){
                        case INSERT:
                            lugar.insert(db);
                            break;
                        case UPDATE:
                            lugar.update(db, lugar.getId());
                            break;
                        case DELETE:
                            lugar.remove(db);
                            break;
                    }

                    Log.i(TAG,"Se actualiza el Lugar: ".concat(lugar.getNombre()));
                    if(lugar.getFotos()!=null) {
                        Log.i(TAG,"El lugar tiene fotos: "+lugar.getFotos().size());
                        for (Foto foto : lugar.getFotos()) {
                            switch (action){
                                case INSERT:
                                    foto.setLugar(lugar);
                                    foto.insert(db);
                                    break;
                                case UPDATE:
                                    foto.setLugar(lugar);
                                    foto.update(db,foto.getId());
                                    break;
                                case DELETE:
                                    foto.remove(db);
                            }

                            Log.i(TAG,"Se actualiza la foto: ".concat(foto.getFoto()));
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

            String message = "Se actualizaron " + resultado + " Lugar(es).";
            UIHelper.addLabelMessageToList(message, actualizarDatos, actualizarDatos.getLinearLayout());
            Log.i(TAG,"Se actualizaron "+registros+".");
            return true;

        }catch (Exception e){

            Toast.makeText(actualizarDatos, "Error actualizando los lugares", Toast.LENGTH_SHORT);
            Log.e(TAG, "Error actualizando los lugares", e);
            return false;

        }

    }
}
