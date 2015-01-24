package intep.proyecto.road2roldanillo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import intep.proyecto.road2roldanillo.entidades.db.Categoria;
import intep.proyecto.road2roldanillo.map.MapHelper;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static final String TAG = "MainActivity";
    private static final int ACTUALIZAR_CODE = 404;

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;

    private MapHelper mapHelper;
    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.action_update){
            Intent i = new Intent(this,ActualizarDatos.class);
            startActivityForResult(i,ACTUALIZAR_CODE);
            return true;
        }else if(id == R.id.action_login){
            Intent intentLogin = new Intent(this, LoginGooglePlus.class);
            startActivity(intentLogin);
            return true;
        }

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==ACTUALIZAR_CODE && resultCode == Activity.RESULT_OK){
            mNavigationDrawerFragment.cargarCategorias();
        }
    }
}
