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
import intep.proyecto.road2roldanillo.entidades.Sitio;

/**
 * Created by gurzaf on 12/3/14.
 */
public class MapHelper {

    public static Map<Marker,Sitio> inicializarSitios(GoogleMap map, List<Sitio> sitios, int resource){
        BitmapDescriptor bt = BitmapDescriptorFactory.fromResource(resource);


        Map<Marker,Sitio> mapa = new HashMap<Marker, Sitio>();

        for (Sitio restaurante : sitios) {
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
