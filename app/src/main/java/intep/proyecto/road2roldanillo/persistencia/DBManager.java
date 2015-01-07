package intep.proyecto.road2roldanillo.persistencia;

/**
 * Created by arturo on 06-01-15.
 */
public class DBManager {
    public static final String TABLE_NAME = "categoria";
    public static final String CN_ID = "id";
    public static final String CN_NAME = "nombre";
    public static final String CN_ICON = "icono";
    public static final String CN_DATE = "fecha";
    public static final String CN_DELETE = "borrado";

    public static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" ("
            +CN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
            +CN_NAME +" TEXT NOT NULL,"
            +CN_ICON +" TEXT,"
            +CN_DATE +" TEXT,"
            +CN_DELETE +" TEXT);";
}
