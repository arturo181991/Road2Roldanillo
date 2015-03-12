package intep.proyecto.road2roldanillo.util;

import java.text.SimpleDateFormat;

/**
 * Created by gurzaf on 3/11/15.
 */
public class DateHelper {

    public static SimpleDateFormat getDateFormat(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public static SimpleDateFormat getDateFormatJSON(){
        return new SimpleDateFormat("MMM d, yyyy h:mm:ss a");
    }

}
