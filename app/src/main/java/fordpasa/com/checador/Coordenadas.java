package fordpasa.com.checador;

/**
 * Created by Principal on 07/02/2018.
 */

public class Coordenadas {

    public double latitud;
    public double longitud;

    public Coordenadas(double latitud, double longitud){
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public void cambiarLatitud(double latitud){
        this.latitud = latitud;
    }

    public void cambiarLongitud(double longitud){
        this.longitud = longitud;
    }

    public double obtenerLatitud(){
        return this.latitud;
    }

    public double obtenerLongitud(){
        return this.longitud;
    }
}
