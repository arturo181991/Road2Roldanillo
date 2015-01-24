package intep.proyecto.road2roldanillo.util;

import java.util.Calendar;

/**
 * Created by gurzaf on 1/9/15.
 */
public class Constantes {

    public static final String BASE_PATH = "http://192.168.1.7:8080/Road2RoldanilloWS";

    public static final String CATEGORIAS_PATH = "datos/categoria/get";
    public static final String CATEGORIAS_IMAGES_PATH = "ImageServlet/#/categoria/#";

    public static final String LUGARES_PATH = "datos/lugar/get";
    public static final String LUGARES_IMAGES_PATH = "ImageServlet/lugar";

    private static final String URL_SEPARATOR = "/";

    /**
     * Método para concatenar varias cadenas y adicionar / entre ellas para contruir una URL
     * @param paths cada una de las cadenas a unir
     * @return cadena de la URL
     */
    public static String concatPath(String... paths){
        if(paths!=null && paths.length>0){
            String result = paths[0];
            for (int i = 1; i < paths.length; i++) {
                result += URL_SEPARATOR + paths[i];
            }
            return result;
        }
        return "";
    }

    public static String getTimeStampAsString(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-100);
        return new Long(calendar.getTimeInMillis()).toString();
    }

}
