package intep.proyecto.road2roldanillo.entidades.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import intep.proyecto.road2roldanillo.persistencia.DBHelper;
import intep.proyecto.road2roldanillo.util.db.TablaEntidadHelper;
import intep.proyecto.road2roldanillo.util.db.TablaHelper;

/**
 * Created by gurzaf on 1/7/15.
 */
public class Lugar extends TablaEntidadHelper{

    private String nombre;
    private Double latitud;
    private Double longitud;
    private String descripcion;
    private Float puntaje;
    private Categoria categoria;
    private int borrado;
    private String direccion;
    private String telefono;
    private String sitio;

    protected List<Foto> fotos;

    public Lugar(int id){
        super(id);
    }

    public Lugar(int id, String nombre, Double latitud, Double longitud, String descripcion, Float puntaje, Categoria categoria, int borrado, String direccion, String telefono, String sitioWeb) {
        super(id);
        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
        this.descripcion = descripcion;
        this.puntaje = puntaje;
        this.categoria = categoria;
        this.borrado = borrado;
        this.direccion = direccion;
        this.telefono = telefono;
        this.sitio = sitioWeb;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Float getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(Float puntaje) {
        this.puntaje = puntaje;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public int getBorrado() {
        return borrado;
    }

    public void setBorrado(int borrado) {
        this.borrado = borrado;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getSitio() {
        return sitio;
    }

    public void setSitio(String sitioWeb) {
        this.sitio= sitioWeb;
    }

    public List<Foto> getFotos() {
        return fotos;
    }

    public void setFotos(List<Foto> fotos) {
        this.fotos = fotos;
    }

    public static List<Lugar> getAllValuesByCategoria(SQLiteDatabase db, Categoria categoria){
//        String[] args = new String[]{categoria.getId()+""};
//        Log.i(Lugar.class.getSimpleName(),"");
//        Cursor c = db.query(Lugar.class.getSimpleName(),null,"categoria=?",args,null,null,null);
        Cursor c = db.query(Lugar.class.getSimpleName(),null,null,null,null,null,null);
        return DBHelper.selectAll(Lugar.class, c);
    }

}
