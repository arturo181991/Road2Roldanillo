package intep.proyecto.road2roldanillo.util;

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
import java.util.TreeMap;

import intep.proyecto.road2roldanillo.R;
import intep.proyecto.road2roldanillo.entidades.Site;

/**
 * Created by gurzaf on 12/3/14.
 */
public class MapHelper {

    public static Map<Marker,Site> initializeSites(GoogleMap map, List<Site> Sites, Site.TYPE type){

        BitmapDescriptor iconHotel = BitmapDescriptorFactory.fromResource(R.drawable.hotel);
        BitmapDescriptor iconRestaurant = BitmapDescriptorFactory.fromResource(R.drawable.restaurant);

        Map<Marker,Site> mapa = new HashMap<Marker, Site>();

        for (Site site : Sites) {

            if(type!=null && type!=site.getType()){
                continue;
            }

            MarkerOptions markerOption = new MarkerOptions().title(site.getNombres())
                    .position(new LatLng(site.getLatitud(), site.getLongitud()))
                    .snippet(site.getDetalle());

            switch (site.getType()){
                case HOTEL:
                    markerOption.icon(iconHotel);
                    break;
                case RESTAURANT:
                    markerOption.icon(iconRestaurant);
                    break;
            }

            Marker marker = map.addMarker(markerOption);
            mapa.put(marker,site);

        }

        return mapa;
    }

}
