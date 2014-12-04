package intep.proyecto.road2roldanillo.util;

import java.util.ArrayList;
import java.util.List;

import intep.proyecto.road2roldanillo.entidades.Sitio;

/**
 * Created by gurzaf on 12/3/14.
 */
public class DataHelper {

    public static List<Sitio> getRestaurantes(){

        List<Sitio> restaurantes = new ArrayList<Sitio>();

        restaurantes.add(new Sitio("Flakos","Este es el restaurante Flakos",4.410819,-76.153504));
        restaurantes.add(new Sitio("Bambinos","Este es el restaurante Bambinos",4.408990,-76.153848));
        restaurantes.add(new Sitio("Richard","Este es el restaurante Richard",4.413429,-76.152968));

        return restaurantes;

    }


    public static List<Sitio> getHoteles() {

        List<Sitio> hoteles = new ArrayList<Sitio>();

        hoteles.add(new Sitio("Balcones del Parque","Este es el hotel BP",4.410937,-76.153762));
        hoteles.add(new Sitio("La Posada","Este es el hotel La Posada",4.409439,-76.153998));
        hoteles.add(new Sitio("Iyoma","Este es el hotel Iyomá",4.407506,-76.154024));

        return hoteles;

    }

}
