package intep.proyecto.road2roldanillo.util;

import java.util.ArrayList;
import java.util.List;

import intep.proyecto.road2roldanillo.entidades.Site;

/**
 * Created by gurzaf on 12/3/14.
 */
public class DataHelper {

    public static List<Site> getSites(){

        List<Site> sites = new ArrayList<Site>();

        sites.add(new Site("Flakos", "Este es el restaurante Flakos", 4.410819, -76.153504, Site.TYPE.RESTAURANT));
        sites.add(new Site("Bambinos", "Este es el restaurante Bambinos", 4.408990, -76.153848, Site.TYPE.RESTAURANT));
        sites.add(new Site("Richard", "Este es el restaurante Richard", 4.413429, -76.152968, Site.TYPE.RESTAURANT));
        sites.add(new Site("Balcones del Parque", "Este es el hotel BP", 4.410937, -76.153762, Site.TYPE.HOTEL));
        sites.add(new Site("La Posada", "Este es el hotel La Posada", 4.409439, -76.153998,Site.TYPE.HOTEL));
        sites.add(new Site("Iyoma", "Este es el hotel Iyomá", 4.407506, -76.154024,Site.TYPE.HOTEL));

        return sites;

    }

}
