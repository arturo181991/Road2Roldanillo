package intep.proyecto.road2roldanillo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;

public class LoginGooglePlus extends Activity implements GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        PlusClient.OnPeopleLoadedListener{

    private SignInButton btnSignIn;
    private Button botonOmitir;
    private PlusClient plusClient;
    private ProgressDialog connectionProgressDialog;
    private ConnectionResult connectionResult;
    private boolean mResolveOnFail;
    private static final int REQUEST_CODE_RESOLVE_ERR = 49404;
    private static final String TAG = LoginGooglePlus.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnSignIn = (SignInButton) findViewById(R.id.sign_in_button);

        botonOmitir = (Button) findViewById(R.id.boton_omitir);

        mResolveOnFail = false;

        plusClient = new PlusClient.Builder(this, this, this)
                    .build();

        connectionProgressDialog = new ProgressDialog(this);
        connectionProgressDialog.setMessage("Conectando con Google...");


        eventos();
    }

    private void eventos() {
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!plusClient.isConnected()) {
//                    connectionProgressDialog.show();
                    mResolveOnFail = true;
                    if (connectionResult != null) {
                        startResolution();
                    } else {
                        plusClient.connect();
                    }
                }

            }
        });

        botonOmitir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginGooglePlus.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        plusClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        plusClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mResolveOnFail = false;
        if(!connectionProgressDialog.isShowing()){
            connectionProgressDialog.show();
        }
        plusClient.loadPeople(this,"me");
    }

    @Override
    public void onDisconnected() {
        Toast.makeText(this,"Desconectado!",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        if (connectionResult.hasResolution()) {
            this.connectionResult = connectionResult;
            if (mResolveOnFail) {
                startResolution();
            }else{
                TextView tv = (TextView) findViewById(R.id.labelBienenido);
                LinearLayout ly = (LinearLayout) findViewById(R.id.contenedorInfoBienvenido);
                LinearLayout ly2 = (LinearLayout) findViewById(R.id.contenedorLoginBienvenido);

                tv.setVisibility(View.VISIBLE);
                ly.setVisibility(View.VISIBLE);
                ly2.setVisibility(View.VISIBLE);
            }
        }

    }


    @Override
    public void onPeopleLoaded(ConnectionResult connectionResult, PersonBuffer persons, String s) {
        if(connectionResult.getErrorCode()==ConnectionResult.SUCCESS){
            Person person = persons.get(0);
            Toast.makeText(this,"Bienvenido: ".concat(person.getDisplayName()),Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"Conexión con Google no está disponible",Toast.LENGTH_LONG).show();
        }
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        connectionProgressDialog.dismiss();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_RESOLVE_ERR && resultCode == RESULT_OK) {
            mResolveOnFail = true;
            plusClient.connect();
        } else if (requestCode == REQUEST_CODE_RESOLVE_ERR && resultCode != RESULT_OK) {
            connectionProgressDialog.dismiss();
        }
    }

    private void startResolution() {
        try {
            mResolveOnFail = false;
            connectionResult.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
        } catch (IntentSender.SendIntentException e) {
            plusClient.connect();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            final View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

}
