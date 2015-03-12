package intep.proyecto.road2roldanillo.util;

import android.content.Context;
import android.preference.PreferenceManager;

import java.util.Calendar;

import intep.proyecto.road2roldanillo.R;

/**
 * Created by gurzaf on 1/9/15.
 */
public class Constantes {

    private static String BASE_PATH;

    public static final String CATEGORIAS_PATH = "datos/categoria/get";
    public static final String CATEGORIAS_IMAGES_PATH = "ImageServlet/#/categoria/#";

    public static final String LUGARES_PATH = "datos/lugar/get";
    public static final String LUGARES_IMAGES_PATH = "ImageServlet/lugar";

    public static final String COMENTARIOS_PATH = "datos/comentario/get";
    public static final String PUT_COMENTARIOS_PATH = "ws/comentario";

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

    public static void load(Context context){
        BASE_PATH = PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.key_base_path), context.getString(R.string.pref_default_basepath_name));
    }

    public static String getBASE_PATH(){
        return Constantes.BASE_PATH;
    }

}
