package intep.proyecto.road2roldanillo;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;


public class MapsActivity extends FragmentActivity{
    private GoogleMap mMap;

    private boolean yourHere;
    private boolean showRestaurants;
    private boolean showHotels;
    private boolean findMe;

    private static final String TAG = "ROAD2ROLDANILLO";
    private static final String KEY_RESTAURANT = "SHOWRESTAURANTS";
    private static final String KEY_HERE = "YOURHERE";
    private static final String KEY_HOTEL = "SHOWHOTELS";

    private List<Marker> restaurantes;
    private List<Marker> hoteles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        if (mMap != null) {

            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            LayoutInflater inflater = null;
            private TextView textViewTitle;

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker marker) {
                View v = getLayoutInflater().inflate(R.layout.custom_info_contents, null);

                LatLng latLng = marker.getPosition();// Getting the position from the marker
                setContentView(R.layout.custom_info_contents);
                Button botonIrAlSitio = (Button) findViewById(R.id.button_go_ubication);
                Button botonResenias = (Button) findViewById(R.id.button_review);
                botonIrAlSitio.setText("Como llegar aqui");
                botonResenias.setText("Reseñas del sitio");
                return v;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker marker) {

                /*View v = getLayoutInflater().inflate(R.layout.custom_info_contents, null);

                LatLng latLng = marker.getPosition();// Getting the position from the marker
                setContentView(R.layout.custom_info_contents);
                Button botonIrAlSitio = (Button) findViewById(R.id.button_go_ubication);
                Button botonResenias = (Button) findViewById(R.id.button_review);
                botonIrAlSitio.setText("Como llegar aqui");
                botonResenias.setText("Reseñas del sitio");
                return v;*/
                return null;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);

            }
        });
    }
    }

    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{
        private View view;
        public CustomInfoWindowAdapter(){
            view = getLayoutInflater().inflate(R.layout.custom_info_contents,null);
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }
    }

    //Se debe reimplementar al menos los 4 metodos onCreate(), onResume(), **onPause()** y onDestroy() para que funcione
    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        if(yourHere){
            encuentrame();
        }
        if(showRestaurants){
            mostrarRestaurantes();
        }
        if(showHotels){
            mostrarHoteles();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_RESTAURANT,showRestaurants);
        outState.putBoolean(KEY_HERE,yourHere);
        outState.putBoolean(KEY_HOTEL,showHotels);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        showRestaurants = savedInstanceState.getBoolean(KEY_RESTAURANT);
        yourHere = savedInstanceState.getBoolean(KEY_HERE);
        showHotels = savedInstanceState.getBoolean(KEY_HOTEL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.restaurant).setChecked(showRestaurants);
        menu.findItem(R.id.hotel).setChecked(showHotels);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if(item.getItemId() == R.id.findme){
            findMe = true;
            Toast.makeText(this,"Obteniendo su ubicacion", Toast.LENGTH_SHORT).show();
            encuentrame();
        }else{
            item.setChecked(!item.isChecked());
            switch (item.getItemId()){
                case R.id.restaurant:
                    showRestaurants = item.isChecked();
                    if(item.isChecked()){
                        mostrarRestaurantes();
                    }else{
                        ocultarMarcadores(restaurantes);
                        restaurantes = null;
                    }
                    break;
                case R.id.hotel:
                    showHotels = item.isChecked();
                    if(showHotels){
                        mostrarHoteles();
                    }else{
                        ocultarMarcadores(hoteles);
                        hoteles = null;
                    }
                    break;
            }
        }
        return true;
    }

    private void ocultarMarcadores(List<Marker> marcadores){
        if (marcadores != null){
            for (Marker marker : marcadores){
                marker.remove();
            }
        }
    }

    private void mostrarRestaurantes(){
        Log.i(TAG,"Mostrando restaurantes");
        if (restaurantes == null){
            List<MarkerOptions> marcadores = inicializarRestaurantes();
            restaurantes = new ArrayList<Marker>();
            for (MarkerOptions markerOptions : marcadores){
                restaurantes.add(mMap.addMarker(markerOptions));
            }
        }
    }

    private void mostrarHoteles(){
        Log.i(TAG,"Mostrando hoteles");
        if(hoteles == null){
            List<MarkerOptions> marcadores = inicializarHoteles();
            hoteles = new ArrayList<Marker>();
            for (MarkerOptions markerOptions : marcadores){
                hoteles.add(mMap.addMarker(markerOptions));
            }
        }
    }

    private List<MarkerOptions> inicializarRestaurantes(){
        BitmapDescriptor bt = BitmapDescriptorFactory.fromResource(R.drawable.restaurant);
        String[] nombres = new String[]{"Flakos","Bambinos","Richard"};
        String[] descripcionesRestaurantes = new String[]{"Este es el restaurante Flakos","Este es el restaurante Bambinos","Este es el restaurante Richard"};
        Double[] latitudes = new Double[]{4.410819,4.408990, 4.413429 };
        Double[] longitudes = new Double[]{-76.153504,-76.153848, -76.152968};

        List<MarkerOptions> marcadores = new ArrayList<MarkerOptions>();
        for (int i = 0; i < nombres.length; i++){
            marcadores.add(new MarkerOptions().title(nombres[i]).icon(bt).position(new LatLng(latitudes[i], longitudes[i])).snippet(descripcionesRestaurantes[i]));
        }
        return marcadores;
    }

    private List<MarkerOptions> inicializarHoteles() {
        BitmapDescriptor bt = BitmapDescriptorFactory.fromResource(R.drawable.hotel);
        String[] nombres = new String[]{"Balcones del Parque", "La Posada", "Iyoma"};
        String[] descripcionesHoteles = new String[]{"Este es el hotel BP","Este es el hotel La Posada","Este es el hotel Iyoma"};
        Double[] latitudes = new Double[]{4.410937, 4.409439, 4.407506};
        Double[] longitudes = new Double[]{-76.153762, -76.153998, -76.154024};

        List<MarkerOptions> marcadores = new ArrayList<MarkerOptions>();

        for (int i = 0; i < nombres.length; i++) {
            marcadores.add(new MarkerOptions().title(nombres[i]).icon(bt).position(new LatLng(latitudes[i], longitudes[i])).snippet(descripcionesHoteles[i]));
        }

        return marcadores;
    }

    private void encuentrame() {
        Log.i(TAG,"Encuentrame!");
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {

            public void onLocationChanged(Location location) {
                teEncontre(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setAltitudeRequired(false);
        locationManager.requestSingleUpdate(criteria,locationListener,getMainLooper());
    }

    private void teEncontre(Location location) {
        yourHere = true;
        Toast.makeText(this,getString(R.string.you_r_here),Toast.LENGTH_SHORT).show();
        if(findMe){
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), (float) 17.0));
            //mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
            mMap.moveCamera(center);
            mMap.animateCamera(zoom,2000,null);
        }
        BitmapDescriptor bt = BitmapDescriptorFactory.fromResource(R.drawable.me);
        mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title(getString(R.string.my_location)).icon(bt));
    }

}

