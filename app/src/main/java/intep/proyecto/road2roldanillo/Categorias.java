package intep.proyecto.road2roldanillo;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import intep.proyecto.road2roldanillo.util.rest.ActualizarCategorias;


public class Categorias extends ActionBarActivity {
    private ListView lstCategorias;
    private Button botonListar;

    private static final String TAG = "Categorias Activity";

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

        ActualizarCategorias actualizarCategorias = new ActualizarCategorias(this,lstCategorias);
        actualizarCategorias.execute();

    }

    private void eventos() {
        botonListar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCategorias();
            }
        });
    }

}
