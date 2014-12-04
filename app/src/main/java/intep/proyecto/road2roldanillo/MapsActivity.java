package intep.proyecto.road2roldanillo;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import intep.proyecto.road2roldanillo.entidades.Site;
import intep.proyecto.road2roldanillo.util.DataHelper;
import intep.proyecto.road2roldanillo.util.MapHelper;


public class MapsActivity extends FragmentActivity{

    private GoogleMap mMap;
    private Menu menu;

    private boolean yourHere;
    private boolean showRestaurants;
    private boolean showHotels;
    private boolean showAll;
    private boolean findMe;

    private static final String TAG = "ROAD2ROLDANILLO";
    private static final String KEY_RESTAURANT = "SHOWRESTAURANTS";
    private static final String KEY_HERE = "YOURHERE";
    private static final String KEY_HOTEL = "SHOWHOTELS";
    private static final String KEY_ALL = "SHOWALL";

    private Map<Marker,Site> sites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();


    }

//    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{
//        private View view;
//        public CustomInfoWindowAdapter(){
//            view = getLayoutInflater().inflate(R.layout.custom_info_contents,null);
//        }
//
//        @Override
//        public View getInfoContents(Marker marker) {
//            return null;
//        }
//
//        @Override
//        public View getInfoWindow(Marker marker) {
//            return view;
//        }
//    }

    //Se debe reimplementar al menos los 4 metodos onCreate(), onResume(), **onPause()** y onDestroy() para que funcione
    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        if(yourHere){
            encuentrame();
        }
        if(showRestaurants){
            showSites(Site.TYPE.RESTAURANT);
        }
        if(showHotels){
            showSites(Site.TYPE.HOTEL);
        }
        if(showAll){
            showSites(null);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_RESTAURANT,showRestaurants);
        outState.putBoolean(KEY_HERE,yourHere);
        outState.putBoolean(KEY_HOTEL,showHotels);
        outState.putBoolean(KEY_ALL,showAll);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        showRestaurants = savedInstanceState.getBoolean(KEY_RESTAURANT);
        yourHere = savedInstanceState.getBoolean(KEY_HERE);
        showHotels = savedInstanceState.getBoolean(KEY_HOTEL);
        showAll = savedInstanceState.getBoolean(KEY_ALL);
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

            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {

                    Site Site;

                    if(sites.containsKey(marker)){
                        Site = sites.get(marker);
                    }else{
                        return null;
                    }

                    View v = getLayoutInflater().inflate(R.layout.custom_info_contents, null);

                    TextView nombreSite = (TextView) v.findViewById(R.id.title);
                    nombreSite.setText(Site.getNombres());

                    TextView detalleSite = (TextView) v.findViewById(R.id.snippet);
                    detalleSite.setText(Site.getDetalle());

                    return v;

                }
            });

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    if(sites!=null && sites.containsKey(marker)){
                        marker.showInfoWindow();
                        return true;
                    }
                    return false;
                }
            });

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
        this.menu = menu;
        if(showAll){
            showRestaurants = true;
            showHotels = true;
        }
        menu.findItem(R.id.restaurant).setChecked(showRestaurants);
        menu.findItem(R.id.hotel).setChecked(showHotels);
        menu.findItem(R.id.all).setChecked(showAll);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
                        showSites(Site.TYPE.RESTAURANT);
                    }else{
                        menu.findItem(R.id.all).setChecked(false);
                        hideSites(Site.TYPE.RESTAURANT);
                    }
                    break;
                case R.id.hotel:
                    showHotels = item.isChecked();
                    if(showHotels){
                        showSites(Site.TYPE.HOTEL);
                    }else{
                        menu.findItem(R.id.all).setChecked(false);
                        hideSites(Site.TYPE.HOTEL);
                    }
                    break;
                case R.id.all:
                    showAll = item.isChecked();
                    menu.findItem(R.id.restaurant).setChecked(showAll);
                    menu.findItem(R.id.hotel).setChecked(showAll);
                    if(showAll){
                        showSites(null);
                    }else{
                        hideSites(null);
                    }
            }
        }
        return true;
    }

    private void hideSites(Site.TYPE type){
        if (sites != null){
            List<Marker> markers = new ArrayList<Marker>();
            for (Marker marker : sites.keySet()){
                if(type!=null && type!=sites.get(marker).getType()){
                    continue;
                }
                marker.remove();
                markers.add(marker);
            }
            for(Marker marker : markers){
                sites.remove(marker);
            }
        }
    }

    private void showSites(Site.TYPE type){
        if(sites==null){
            sites = new HashMap<Marker, Site>();
        }
        sites.putAll(MapHelper.initializeSites(mMap,DataHelper.getSites(),type));
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

