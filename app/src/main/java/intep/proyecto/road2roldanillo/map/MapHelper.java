package intep.proyecto.road2roldanillo.map;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import intep.proyecto.road2roldanillo.MainActivity;
import intep.proyecto.road2roldanillo.R;
import intep.proyecto.road2roldanillo.TabbedActivity;
import intep.proyecto.road2roldanillo.entidades.Categoria;
import intep.proyecto.road2roldanillo.entidades.Site;
import intep.proyecto.road2roldanillo.util.DataHelper;

/**
 * Created by gurzaf on 12/23/14.
 */
public class MapHelper {



    private static final String KEY_RESTAURANT = "SHOWRESTAURANTS";
    private static final String KEY_HERE = "YOURHERE";
    private static final String KEY_HOTEL = "SHOWHOTELS";
    private static final String KEY_ALL = "SHOWALL";

    public static final String KEY_SITE = "SITE";
    public static final String KEY_MY_LOCATION = "MY_LOCATION";

    private boolean yourHere;
    private boolean showAll;
    private boolean findMe;
    private Map<Categoria,Boolean> estadosCategorias;

    private Marker miUbicacion;
    private Map<Marker,Site> sites;


    public MapHelper(){

    }

    public void setUpMap(final GoogleMap mMap,
                          final LayoutInflater layoutInflater, final MainActivity mainActivity) {

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker marker) {

                return null;

            }

            @Override
            public View getInfoContents(Marker marker) {

                Site site;

                if(sites!=null && sites.containsKey(marker)){
                    site = sites.get(marker);
                }else{
                    return null;
                }

                View v = layoutInflater.inflate(R.layout.custom_info_contents, null);
                TextView nombreSite = (TextView) v.findViewById(R.id.title);
                nombreSite.setText(site.getNombres());

                TextView address = (TextView) v.findViewById(R.id.address);
                address.setText(site.getDireccion());


                RatingBar ratingBar = (RatingBar) v.findViewById(R.id.starRating);
                ratingBar.setRating(site.getRating());
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

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                if(sites!=null && sites.containsKey(marker)){
                    Site site = sites.get(marker);
                    Log.i("Click", "Dio click");
                    Intent intent = new Intent(mainActivity,TabbedActivity.class);
                    intent.putExtra(KEY_SITE,site);
                    if(miUbicacion!=null){
                        intent.putExtra(KEY_MY_LOCATION,miUbicacion.getPosition());
                    }

                    Bundle bndlanimation =
                            ActivityOptions.makeCustomAnimation(mainActivity.getApplicationContext(), R.anim.slide_in_right, R.anim.slide_out_left).toBundle();

                    mainActivity.startActivity(intent, bndlanimation);
                }

            }
        });





    }

    public void initializeSites(GoogleMap map, Categoria categoria){

        if(sites==null){
            sites = new HashMap<Marker, Site>();
        }


        for (Site site : DataHelper.getSites()) {

            if(categoria!=null && categoria!=site.getCategoria()){
                continue;
            }

            MarkerOptions markerOption = new MarkerOptions().title(site.getNombres())
                    .position(new LatLng(site.getLatitud(), site.getLongitud()))
                    .snippet(site.getDetalle());

            markerOption.icon(site.getCategoria().getBitMapDescriptor());

            Marker marker = map.addMarker(markerOption);
            sites.put(marker,site);

        }

    }

    public void hideSites(Categoria categoria){
        if (sites != null){
            List<Marker> markers = new ArrayList<Marker>();
            for (Marker marker : sites.keySet()){
                if(categoria!=null && categoria!=sites.get(marker).getCategoria()){
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

    public void encuentrame(final MainActivity mainActivity, final GoogleMap mMap) {
        LocationManager locationManager = (LocationManager) mainActivity.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {

            public void onLocationChanged(Location location) {
                teEncontre(location,mainActivity,mMap);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setAltitudeRequired(false);
        locationManager.requestSingleUpdate(criteria,locationListener,mainActivity.getMainLooper());
    }

    private void teEncontre(final Location location, final MainActivity mainActivity, final GoogleMap mMap) {
        if(miUbicacion!=null){
            miUbicacion.remove();
        }
        yourHere = true;
        Toast.makeText(mainActivity, mainActivity.getString(R.string.you_r_here), Toast.LENGTH_SHORT).show();
        if(findMe){
            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
            mMap.moveCamera(center);
            mMap.animateCamera(zoom,2000,null);
        }
        BitmapDescriptor bt = BitmapDescriptorFactory.fromResource(R.drawable.me);
        miUbicacion = mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title(mainActivity.getString(R.string.my_location)).icon(bt));
    }

    public void doSelectCategoria(Categoria categoria) {
        if(estadosCategorias==null){
            estadosCategorias = new HashMap<Categoria, Boolean>();
        }
        estadosCategorias.put(categoria,
                !estadosCategorias.get(categoria));
    }

    public boolean isCategoriaSelected(Categoria categoria) {
        if(estadosCategorias==null){
            estadosCategorias = new HashMap<Categoria, Boolean>();
        }

        if(estadosCategorias.containsKey(categoria)){

            return estadosCategorias.get(categoria);

        }else{
            estadosCategorias.put(categoria,false);
            return isCategoriaSelected(categoria);
        }
    }

    public String getCategoriaKey(Categoria categoria){
        return "KEY_CATEGORIA_".concat(categoria.toString());
    }

    public boolean isYourHere() {
        return yourHere;
    }

    public void setYourHere(boolean yourHere) {
        this.yourHere = yourHere;
    }

    public boolean isShowAll() {
        return showAll;
    }

    public void setShowAll(boolean showAll) {
        this.showAll = showAll;
    }

    public boolean isFindMe() {
        return findMe;
    }

    public void setFindMe(boolean findMe) {
        this.findMe = findMe;
    }

    public Marker getMiUbicacion() {
        return miUbicacion;
    }

    public void setMiUbicacion(Marker miUbicacion) {
        this.miUbicacion = miUbicacion;
    }

    public Map<Marker, Site> getSites() {
        return sites;
    }

    public void setSites(Map<Marker, Site> sites) {
        this.sites = sites;
    }
}
