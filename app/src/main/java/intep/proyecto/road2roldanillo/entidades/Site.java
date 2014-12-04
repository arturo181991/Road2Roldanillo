package intep.proyecto.road2roldanillo.entidades;

/**
 * Created by gurzaf on 12/3/14.
 */
public class Site {
    private String nombres;
    private String detalle;
    private Double latitud;
    private Double longitud;

    public Site(String nombres, String detalle, Double latitud, Double longitud) {
        this.nombres = nombres;
        this.detalle = detalle;
        this.latitud = latitud;
        this.longitud = longitud;
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



}
