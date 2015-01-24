package intep.proyecto.road2roldanillo;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import intep.proyecto.road2roldanillo.entidades.db.Categoria;
import intep.proyecto.road2roldanillo.util.rest.ActualizarCategorias;
import intep.proyecto.road2roldanillo.util.rest.ActualizarLugares;


public class ActualizarDatos extends ActionBarActivity {

    private Button botonListar;
    private LinearLayout linearLayout;

    private static final String TAG = "Categorias Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_datos);
        linearLayout = (LinearLayout) findViewById(R.id.listado_actualizados);
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

        ActualizarCategorias actualizarCategorias = new ActualizarCategorias(this,linearLayout, Categoria.class);
        actualizarCategorias.execute();

        ActualizarLugares actualizarLugares = new ActualizarLugares(this, linearLayout);
        actualizarLugares.execute();

    }

    private void eventos() {
        botonListar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCategorias();
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK);
        super.onBackPressed();
    }
}
