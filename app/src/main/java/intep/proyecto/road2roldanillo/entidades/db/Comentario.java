package intep.proyecto.road2roldanillo.entidades.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Date;
import java.util.List;

import intep.proyecto.road2roldanillo.persistencia.DBHelper;
import intep.proyecto.road2roldanillo.util.db.TablaEntidadHelper;
import intep.proyecto.road2roldanillo.util.db.TablaHelper;

/**
 * Created by gurzaf on 1/7/15.
 */
public class Comentario extends TablaEntidadHelper{

    private String detalle;
    private Date fecha;
    private int puntaje;
    private int lugar;
    private String usuario;
    private String usuarioNombre;
    private int borrado;
    private int subido;

    public Comentario(int id){
        super(id);
    }

    public Comentario(int id, String detalle, Date fecha, int puntaje, int lugar, String usuario, String usuarioNombre, int borrado, int subido) {
        super(id);
        this.detalle = detalle;
        this.fecha = fecha;
        this.puntaje = puntaje;
        this.lugar = lugar;
        this.usuario = usuario;
        this.usuarioNombre = usuarioNombre;
        this.borrado = borrado;
        this.subido = subido;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    public int getLugar() {
        return lugar;
    }

    public void setLugar(int lugar) {
        this.lugar = lugar;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public int getBorrado() {
        return borrado;
    }

    public void setBorrado(int borrado) {
        this.borrado = borrado;
    }

    public int getSubido() {
        return subido;
    }

    public void setSubido(int subido) {
        this.subido = subido;
    }


    public long insert(SQLiteDatabase db, boolean idExterno) {
            Log.i(super.TAG, "Ejecutando método INSERT a la entidad "
                    .concat(this.getClass().getSimpleName())
                    .concat("[")
                    .concat(getId()+"")
                    .concat("]"));
            return db.insert(this.getClass().getSimpleName(), null, getContent(idExterno));
    }

    /**
     * Método para obtener los comentarios de la base de datos
     * @param db SQLiteDatabase en modo lectura
     * @param todos si es verdadero consulta todos los comentarios, sino solo los pendientes por subir
     * @return listado comentarios
     */
    public static List<Comentario> getAllValues(SQLiteDatabase db, boolean todos){

        String where = null, orderBy = null;
        String[] params = null;

        if(!todos){
            where = "subido = ?";
            orderBy = "fecha DESC";
            params = new String[]{"0"};
        }


        return (List<Comentario>) TablaEntidadHelper.getValues(Comentario.class.getSimpleName(),
                Comentario.class,db,where,params,orderBy);

    }

    public static Comentario find(SQLiteDatabase db, int id){

        Log.i("Comentario", "Obteniendo la entidad "
                .concat(Comentario.class.getSimpleName())
                .concat("[")
                .concat(id+"")
                .concat("] desde la base de datos."));
        try {
            String params[] = new String[]{id+""};
            Cursor c = db.query(Comentario.class.getSimpleName(), null, "id = ?", params, null, null, null, null);

            Comentario comentario = DBHelper.selectAll(Comentario.class,c).get(0);

            return comentario;

        }catch (Exception e){
            Log.e("Comentario","Error obteniendo la entidad",e);
            return null;
        }

    }

    public static List<Comentario> getAllFromLugar(SQLiteDatabase db, Lugar lugar) {
        String where = null, orderBy = null;
        String[] params = null;

        if(lugar!=null){
            where = "lugar = ?";
            orderBy = "fecha DESC";
            params = new String[]{""+lugar.getId()};
        }


        return (List<Comentario>) TablaEntidadHelper.getValues(Comentario.class.getSimpleName(),
                Comentario.class,db,where,params,orderBy);
    }
}
