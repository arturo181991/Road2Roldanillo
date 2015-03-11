package intep.proyecto.road2roldanillo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import intep.proyecto.road2roldanillo.util.rest.ActualizarCategorias;


public class ActualizarDatos extends ActionBarActivity {

    private Button botonListar;
    private LinearLayout linearLayout;
    private ProgressDialog progressDialog;

    private static final String TAG = "Categorias Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_datos);
        linearLayout = (LinearLayout) findViewById(R.id.listado_actualizados);
        botonListar = (Button) findViewById(R.id.botonListar);
        eventos();
    }

    public void getCategorias(){

        linearLayout.removeAllViews();
        progressDialog = ProgressDialog.show(this,
                 "Sonríe mientras esperas ...", "Descargando Vitaminas ...",true,true);

        ActualizarCategorias actualizarCategorias = new ActualizarCategorias(this);
        actualizarCategorias.execute();

    }

    public void setTextProgressDialog(final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(progressDialog!=null && progressDialog.isShowing()){
                    progressDialog.setMessage(message);
                }
            }
        });

    }

    public void hideProgressDialog(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(progressDialog!=null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        });
    }

    public LinearLayout getLinearLayout(){
        return linearLayout;
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
