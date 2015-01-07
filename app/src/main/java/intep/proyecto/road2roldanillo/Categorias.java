package intep.proyecto.road2roldanillo;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import intep.proyecto.road2roldanillo.persistencia.DBHelper;


public class Categorias extends ActionBarActivity {
    private ListView lstCategorias;
    private Button botonListar;
    private String [] categorias;
    Long horaTimeStamp = 0l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);


        //dbHelper.onUpgrade(db,1,1);

        lstCategorias = (ListView)findViewById(R.id.lstCategorias);
        botonListar = (Button) findViewById(R.id.botonListar);
        eventos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_categorias, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getCategorias(){

        ActualizarCategorias actualizarCategorias = new ActualizarCategorias();
        actualizarCategorias.execute();

    }

    private long insertarCategoria(SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id",categorias[0]);
        contentValues.put("nombre",categorias[1]);
        contentValues.put("subido",categorias[2]);
        contentValues.put("icono",categorias[3]);

        return db.insert("CATEGORIA",null,contentValues);
    }

    private void eventos() {
        botonListar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCategorias();
                Context context = getApplicationContext();
                CharSequence text = ""+horaTimeStamp;
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
    }

    class ActualizarCategorias extends AsyncTask<String,Void,Boolean>{

        @Override
        protected Boolean doInBackground(String... strings) {

            HttpClient httpClient = new DefaultHttpClient();

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE,-2);

            horaTimeStamp = calendar.getTimeInMillis();

            HttpGet httpGet = new HttpGet("http://192.168.1.10:8080/Road2RoldanilloWS/datos/categoria/get/"+ horaTimeStamp);
            httpGet.setHeader("content-type", "application/json");

            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                String respStr = EntityUtils.toString(httpResponse.getEntity());
                JSONArray jsonArray = new JSONArray(respStr);
                categorias = new String[jsonArray.length()];
                for (int i = 0; i<jsonArray.length(); i++){
                    JSONObject dato = jsonArray.getJSONObject(i);
                    int idCategoria = dato.getInt("id");
                    String nombreCategoria = dato.getString("nombre");
                    String icono = dato.getString("icono");
                    String fecha = dato.getString("fecha");
                    String borrado = dato.getString("borrado");
                    categorias[i] = "" + idCategoria + "-" + nombreCategoria +icono +fecha+ "" +borrado;
                }

                DBHelper dbHelper = new DBHelper(Categorias.this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                insertarCategoria(dbHelper.getWritableDatabase());
                Log.w("Mensaje: ","Linea 135");
                return true;

            }catch (Exception exc){
                exc.printStackTrace();
                return false;
            }

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {

            if(aBoolean) {

                Toast.makeText(getApplicationContext(), "Arreglo de categorias-> " + categorias, Toast.LENGTH_SHORT).show();
                ArrayAdapter<String> adaptador = new ArrayAdapter<String>(Categorias.this, android.R.layout.simple_list_item_1, categorias);
                lstCategorias.setAdapter(adaptador);

            }

        }
    }

}
