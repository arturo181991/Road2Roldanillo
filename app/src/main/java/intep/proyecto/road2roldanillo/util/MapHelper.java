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

    public static Map<Marker,Site> inicializarSites(GoogleMap map, List<Site> Sites, int resource){
        BitmapDescriptor bt = BitmapDescriptorFactory.fromResource(resource);


        Map<Marker,Site> mapa = new HashMap<Marker, Site>();

        for (Site restaurante : Sites) {
            MarkerOptions markerOption = new MarkerOptions().title(restaurante.getNombres()).
                    icon(bt)
                    .position(new LatLng(restaurante.getLatitud(), restaurante.getLongitud()))
                    .snippet(restaurante.getDetalle());
            Marker marker = map.addMarker(markerOption);
            mapa.put(marker,restaurante);
        }

        return mapa;
    }

}
