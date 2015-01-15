package intep.proyecto.road2roldanillo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.PlusClient;

public class LoginGooglePlus extends Activity implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

    private SignInButton btnSignIn;
    private PlusClient plusClient;
    private ProgressDialog connectionProgressDialog;
    private ConnectionResult connectionResult;
    private static final int REQUEST_CODE_RESOLVE_ERR = 49404;
    private static final String TAG = LoginGooglePlus.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnSignIn = (SignInButton) findViewById(R.id.sign_in_button);
        try {
            plusClient = new PlusClient.Builder(this, this, this)
                    .setActions("http://schemas.google.com/CommentActivity")
                    .build();
            connectionProgressDialog = new ProgressDialog(this);
            connectionProgressDialog.setMessage("Conectando con Google...");
        }catch (Exception e){
            e.printStackTrace();
        }

        eventos();
    }

    private void eventos() {
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!plusClient.isConnected()){
                    if(connectionResult == null){
                        connectionProgressDialog.show();
                    }else{
                        try {
                            connectionResult.startResolutionForResult(LoginGooglePlus.this, REQUEST_CODE_RESOLVE_ERR);
                        }catch (IntentSender.SendIntentException exc){
                            exc.printStackTrace();
                            connectionResult = null;
                            plusClient.connect();
                        }
                    }
                }
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
        connectionProgressDialog.dismiss();
        Toast.makeText(this,"Conectado!",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDisconnected() {
        Toast.makeText(this,"Desconectado!",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        if(connectionProgressDialog.isShowing()){
            if (connectionResult.hasResolution()){
                try {
                    connectionResult.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
                }catch (IntentSender.SendIntentException e){
                    plusClient.connect();
                }
            }
        }

        this.connectionResult = connectionResult;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_RESOLVE_ERR && resultCode == RESULT_OK){
            connectionResult = null;
            plusClient.connect();
        }
    }
}
