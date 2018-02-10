package fordpasa.com.checador;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Surface;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;


public class CameraActivity extends AppCompatActivity {

    public static String posicionamiento;
    public static String LocaclizacionEnSucursal= "";
    public static String mapaDeSucursal= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);



        String s = checkInEnSitio();
        Toast.makeText(getBaseContext(), "Sus posición es: "+ s, Toast.LENGTH_LONG).show();

        if(s.equals("Sucursal Ford Pasa Matriz")){
            posicionamiento = "Sucursal Ford Pasa Matriz";
            mapaDeSucursal= "https://maps.googleapis.com/maps/api/staticmap?size=400x400&key=AIzaSyDnI1pToZhDoC0MWg6NB9x6PreBlAMAoIk&zoom=18&center="+matrizLat+","+matrizLng+"&size=600x300&maptype=roadmap&markers=color:blue%7Clabel:S%7C"+matrizLat+","+matrizLng;

        }
        else if (s.equals("Sucursal Juventud")){
            posicionamiento = "Sucursal Juventud";
            mapaDeSucursal= "https://maps.googleapis.com/maps/api/staticmap?size=400x400&key=AIzaSyDnI1pToZhDoC0MWg6NB9x6PreBlAMAoIk&zoom=18&center="+juventudLat+","+juventudLng+"&size=600x300&maptype=roadmap&markers=color:blue%7Clabel:S%7C"+juventudLat+","+juventudLng;
        }
        else if (s.equals("Sucursal Seminuevos")){
            posicionamiento = "Sucursal Seminuevos";
            mapaDeSucursal= "https://maps.googleapis.com/maps/api/staticmap?size=400x400&key=AIzaSyDnI1pToZhDoC0MWg6NB9x6PreBlAMAoIk&zoom=18&center="+seminuevosLat+","+seminuevosLng+"&size=600x300&maptype=roadmap&markers=color:blue%7Clabel:S%7C"+seminuevosLat+","+seminuevosLng;
        }
        else if (s.equals("Sucursal Casas Grandes")){
            posicionamiento = "Sucursal Casas Grandes";
            mapaDeSucursal= "https://maps.googleapis.com/maps/api/staticmap?size=400x400&key=AIzaSyDnI1pToZhDoC0MWg6NB9x6PreBlAMAoIk&zoom=18&center="+cgLat+","+cgLng+"&size=600x300&maptype=roadmap&markers=color:blue%7Clabel:S%7C"+cgLat+","+cgLng;
        }
        else if (s.equals("Sucursal Cuauhtemoc")){
            posicionamiento = "";
            mapaDeSucursal= "https://maps.googleapis.com/maps/api/staticmap?size=400x400&key=AIzaSyDnI1pToZhDoC0MWg6NB9x6PreBlAMAoIk&zoom=18&center="+cuahuLat+","+cuahuLng+"&size=600x300&maptype=roadmap&markers=color:blue%7Clabel:S%7C"+cuahuLat+","+cuahuLng;
        }else if (s.equals("Sucursal Delicias")){
            posicionamiento = "Sucursal Delicias";
            mapaDeSucursal= "https://maps.googleapis.com/maps/api/staticmap?size=400x400&key=AIzaSyDnI1pToZhDoC0MWg6NB9x6PreBlAMAoIk&zoom=18&center="+deliciasLat+","+deliciasLng+"&size=600x300&maptype=roadmap&markers=color:blue%7Clabel:S%7C"+deliciasLat+","+deliciasLng;
        }else if (s.equals("Sucursal Parral")){
            posicionamiento = "Sucursal Parral";
            mapaDeSucursal= "https://maps.googleapis.com/maps/api/staticmap?size=400x400&key=AIzaSyDnI1pToZhDoC0MWg6NB9x6PreBlAMAoIk&zoom=18&center="+parralLat+","+parralLng+"&size=600x300&maptype=roadmap&markers=color:blue%7Clabel:S%7C"+parralLat+","+parralLng;
        }else if (s.equals("Sucursal Torreon")){
            posicionamiento = "Sucursal Torreon";
            mapaDeSucursal= "https://maps.googleapis.com/maps/api/staticmap?size=400x400&key=AIzaSyDnI1pToZhDoC0MWg6NB9x6PreBlAMAoIk&zoom=18&center="+torreonLat+","+torreonLng+"&size=600x300&maptype=roadmap&markers=color:blue%7Clabel:S%7C"+torreonLat+","+torreonLng;
        }else if (s.equals("Sucursal Conauto")){
            posicionamiento = "Sucursal Conauto";
            mapaDeSucursal= "https://maps.googleapis.com/maps/api/staticmap?size=400x400&key=AIzaSyDnI1pToZhDoC0MWg6NB9x6PreBlAMAoIk&zoom=18&center="+conautoLat+","+conautoLng+"&size=600x300&maptype=roadmap&markers=color:blue%7Clabel:S%7C"+conautoLat+","+conautoLng;
        }else{
            LocaclizacionEnSucursal = "1";
            posicionamiento = s;
        }

    //aqui agarrar las coordenadas y tranformarlas con la api a nombre del lugar
    //https://maps.googleapis.com/maps/api/geocode/json?latlng=40.714224,-73.961452&key=YOUR_API_KEY

        if (null == savedInstanceState) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, Camera2BasicFragment.newInstance())
                    .commit();
        }


    }


    public Coordenadas obtenerMisCoordenadas() {
        double longitude = 0;
        double latitude = 0;
        int segundaOpcion = 0;
        //obtiene las coordenadas
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(getBaseContext(), "Primero debe activar el GPS", Toast.LENGTH_LONG).show();
            return new Coordenadas(0.0, 0.0);
        } else {
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            longitude = location.getLongitude();
            latitude = location.getLatitude();

            System.out.println("Longitud: " + longitude);
            System.out.println("Latitud: " + latitude);
        }

        return new Coordenadas(latitude, longitude);
    }


    //METODOS QUE CALCULAN LA DISTANCIA DE UN PUNTO A OTRO EN KM
    public double regresaDistaciaEnKM(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }


    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    //
    //LISTA DE LOS PUNTOS PARA PROBAR, AGENCIAS ETC
    //

    double matrizLat = 28.660871;
    double matrizLng = -106.096919;
    double juventudLat = 28.635171;
    double juventudLng = -106.122756;
    double seminuevosLat = 28.681536;
    double seminuevosLng = -106.135675;
    double cgLat = 30.427618;
    double cgLng = -107.914389;
    double cuahuLat = 28.410614;
    double cuahuLng = -106.859846;
    double deliciasLat = 28.195903;
    double deliciasLng = -105.462758;
    double parralLat = 26.928512;
    double parralLng = -105.651745;
    double torreonLat = 25.545834;
    double torreonLng = -103.451503;
    double conautoLat = 28.661205;
    double conautoLng = -106.097000;

    public String checkInEnSitio() {
        Coordenadas coor = obtenerMisCoordenadas();
        double lat = coor.obtenerLatitud();
        double lng = coor.obtenerLongitud();

        //VALIDACIONES SI EL EMPLEADO SE ENCUANTRA EN CUALQUIER AGENCIA

        double distanciaMatriz = regresaDistaciaEnKM(matrizLat, matrizLng, lat, lng);
        double distanciaJuventud = regresaDistaciaEnKM(juventudLat, juventudLng, lat, lng);
        double distanciaSemi = regresaDistaciaEnKM(seminuevosLat, seminuevosLng, lat, lng);
        double distanciaCasasG = regresaDistaciaEnKM(cgLat, cgLng, lat, lng);
        double distanciaCuahu = regresaDistaciaEnKM(cuahuLat, cuahuLng, lat, lng);
        double distanciaCDeli = regresaDistaciaEnKM(deliciasLat, deliciasLng, lat, lng);
        double distanciaParral = regresaDistaciaEnKM(parralLat, parralLng, lat, lng);
        double distanciaTorreon = regresaDistaciaEnKM(torreonLat, torreonLng, lat, lng);
        double distanciaConauto = regresaDistaciaEnKM(conautoLat, conautoLng, lat, lng);


        //   System.out.println("La distancia entre las coordenadas de matriz y mi ubicacion es: "+ distancia);

        if (distanciaMatriz < 1.0) {
            return "Sucursal Ford Pasa Matriz";
        } else if (distanciaJuventud < 1.0) {
            return "Sucursal Juventud";
        } else if (distanciaSemi < 1.0) {
            return "Sucursal Seminuevos";
        } else if (distanciaCasasG < 1.0) {
            return "Sucursal Casas Grandes";
        } else if (distanciaCuahu < 1.0) {
            return "Sucursal Cuauhtemoc";
        } else if (distanciaCDeli < 1.0) {
            return "Sucursal Delicias";
        } else if (distanciaParral < 1.0) {
            return "Sucursal Parral";
        } else if (distanciaTorreon < 1.0) {
            return "Sucursal Torreon";
        } else if (distanciaConauto < 1.0) {
            return "Sucursal Conauto";
        } else {
            String sLat = String.valueOf(lat);
            String sLng = String.valueOf(lng);

            return sLat + "," + sLng;
        }


    }


         ///////////POST/////////////////

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String post(String pagina, Map<String, Object> valores) {
        URL url = null;
        HttpURLConnection conexion = null;

        // Se almacenan los datos a enviar en una cadena de texto.
        String datos = "";

        /**
         * Si se especifican valores se almacenan en la variable datos.
         */
        if (null != valores) {
            try {
                StringBuilder dataBuilder = new StringBuilder();
                for (Map.Entry<String, Object> parametro : valores.entrySet()) {
                    if (dataBuilder.length() != 0) {
                        dataBuilder.append('&');
                    }

                    dataBuilder.append(URLEncoder.encode(parametro.getKey(), "UTF-8"));
                    dataBuilder.append('=');
                    dataBuilder.append(URLEncoder.encode(parametro.getValue().toString(), "UTF-8"));
                }

                datos = dataBuilder.toString();
            } catch (UnsupportedEncodingException ex) {
                System.out.println("NO SE ALMACENARON LOS VALORES CORRECTAMENTE");
            }
        }

        /**
         * Se crea la conexion.
         */
        try {
            url = new URL(pagina);

            conexion = (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException ex) {
            // TODO: manejar error.
        } catch (IOException ex) {
            // TODO: manejar error.
        }

        if (null == url || null == conexion) {
            return null;
        }

        /**
         * Se establece el tipo de peticion.
         */
        try {
            conexion.setRequestMethod("POST");
        } catch (ProtocolException ex) {
            System.out.println("Algo salio mal con la peticion");
        }

        // Se deshabilita el cache.
        conexion.setUseCaches(false);
        // Se especifica que queremos obtener la respuesta del servidor.
        conexion.setDoOutput(true);

        /**
         * Si se especificaron valores e enviar, se agrega la informacion necesaria para que el servidor lo entienda.
         */
        if (null != valores && !valores.isEmpty()) {
            try {
                byte[] dataBytes = datos.getBytes("UTF-8");

                conexion.setRequestProperty(
                        "Content-Type", "application/x-www-form-urlencoded");
                conexion.setRequestProperty(
                        "Content-Length", String.valueOf(dataBytes.length));

                conexion.getOutputStream().write(dataBytes);
            } catch (UnsupportedEncodingException ex) {
                System.out.println("salio mal en utf8");
            } catch (IOException ex) {
                System.out.println("salio mal en utf8");
            }
        }

        /**
         * Se obtiene la respuesta.
         */
        try {
            // Se obtienen los errores.
            InputStream errores = conexion.getErrorStream();

            // Si no hay errores entonces se obtiene la respuesta.
            if (null == errores) {
                errores = conexion.getInputStream();
            }

            // Se construye la respuesta.
            StringBuilder constructorRespuesta = new StringBuilder();
            try (BufferedReader reader
                         = new BufferedReader(new InputStreamReader(errores))) {
                String linea;
                while ((linea = reader.readLine()) != null) {
                    constructorRespuesta.append(linea);
                    constructorRespuesta.append('\r');
                }
            }

            // Se obtiene el codigo http, por ejemplo 200, 404, 500
            int codigoHttp = conexion.getResponseCode();
            // Se obtiene la respuesta.
            String respuestaHttp = constructorRespuesta.toString();

            return respuestaHttp;
        } catch (IOException ex) {
            System.out.println("salio mal en obtener la respuesta");
        } finally {
            conexion.disconnect();
        }

        return null;
    }



}
