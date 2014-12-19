package intep.proyecto.road2roldanillo.entidades;

import java.io.Serializable;

/**
 * Created by gurzaf on 12/3/14.
 */
public class Site implements Serializable{

    public enum TYPE{
        HOTEL,
        RESTAURANT
    }

    private String nombres;
    private String detalle;
    private String direccion;
    private Float rating;
    private Double latitud;
    private Double longitud;
    private TYPE type;

    public Site(String nombres, String detalle, String direccion, Float rating, Double latitud, Double longitud, TYPE type) {
        this.nombres = nombres;
        this.detalle = detalle;
        this.direccion = direccion;
        this.rating = rating;
        this.latitud = latitud;
        this.longitud = longitud;
        this.type = type;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
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

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }
}
