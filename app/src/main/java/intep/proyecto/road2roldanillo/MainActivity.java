package intep.proyecto.road2roldanillo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.plus.PlusClient;

import intep.proyecto.road2roldanillo.entidades.db.Categoria;
import intep.proyecto.road2roldanillo.map.MapHelper;
import intep.proyecto.road2roldanillo.preferences.SettingsActivity;
import intep.proyecto.road2roldanillo.util.Constantes;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, PlusClient.OnAccessRevokedListener {

    private static final String TAG = "MainActivity";
    private static final int ACTUALIZAR_CODE = 404;

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;

    private MapHelper mapHelper;
    private GoogleMap mMap;

    private MenuItem menuItemLogin;
    private PlusClient plusClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cargarPreferencias();

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        mTitle = getTitle();

        mapHelper = new MapHelper(this);

        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout),
                mapHelper);

        setUpMapIfNeeded();
        encuentrameInicial();

        setUpGooglePlusClient();

    }

    private void setUpGooglePlusClient() {

        Log.i(TAG, "Creando nuevo cliente de Google Plus");
        plusClient = new PlusClient.Builder(getApplicationContext(), this, this)
                .build();

    }

    private void cargarPreferencias() {

        Constantes.load(this);

    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                mapHelper.setUpMap(mMap,getLayoutInflater(),this);
            }
        }
    }

    private void encuentrameInicial() {
        mapHelper.setFindMe(true);
        Log.i(TAG, "Buscando ubicacion actual....");
        mapHelper.encuentrame(this,mMap);
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
    public void onNavigationDrawerItemSelected(Categoria categoria) {

        if(mapHelper.isCategoriaSelected(categoria)) {
            mapHelper.initializeSites(mMap, categoria,getApplicationContext());
        }else{
            mapHelper.hideSites(categoria);
        }

    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);
            menuItemLogin = menu.findItem(R.id.action_login);
            restoreActionBar();
            Log.i(TAG, "Llamando el metodo verify loggin desde createoptionsmenu");
            verifyLoggin();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void verifyLoggin() {
        if(menuItemLogin!=null){
            Log.i(TAG,"MenuItem definido");
            if(plusClient!=null && plusClient.isConnected()){
                Log.i(TAG,"Cliente Google Plus Conectado");
                menuItemLogin.setTitle(getString(R.string.logout_action));
            }else{
                Log.i(TAG,"Cliente Google Plus No Conectado");
                menuItemLogin.setTitle(getString(R.string.login_action));
            }
        }else{
            Log.i(TAG, "MenuItem null");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.action_update:
                Intent i = new Intent(this,ActualizarDatos.class);
                startActivityForResult(i,ACTUALIZAR_CODE);
                return true;
            case R.id.action_login:
                if(plusClient==null || !plusClient.isConnected()) {
                    Intent intentLogin = new Intent(this, LoginGooglePlus.class);
                    startActivity(intentLogin);
                }else{
                    cerrarSesion();
                }
                return true;
            case R.id.action_settings:
                Intent intentSettings = new Intent(this, SettingsActivity.class);
                startActivity(intentSettings);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void cerrarSesion() {

        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle("Cerrar Sesión");
        dialog.setMessage("¿Confirma que desea cerrar sesión?");
        dialog.setCancelable(false);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                plusClient.revokeAccessAndDisconnect(MainActivity.this);
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.setIcon(android.R.drawable.ic_dialog_alert);
        dialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==ACTUALIZAR_CODE && resultCode == Activity.RESULT_OK){
            mNavigationDrawerFragment.cargarCategorias();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG,"Llamando el metodo verify loggin desde onconnected");
        verifyLoggin();
    }

    @Override
    public void onDisconnected() {
        Log.i(TAG,"Llamando el metodo verify loggin desde ondisconnected");
        verifyLoggin();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG,"Llamando el metodo verify loggin desde connectionfailed");
        verifyLoggin();
    }

    @Override
    public void onAccessRevoked(ConnectionResult connectionResult) {
        Log.i(TAG,"Sesión cerrada");
        Toast.makeText(this,"Cerraste Sesión...",Toast.LENGTH_LONG).show();
        Intent intentLogin = new Intent(this, LoginGooglePlus.class);
        startActivity(intentLogin);
    }
}
