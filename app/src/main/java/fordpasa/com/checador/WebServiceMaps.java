package fordpasa.com.checador;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;

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
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Principal on 08/02/2018.
 */

public class WebServiceMaps {

    public void webServ(final String coordenadas){
        //final String coordenadas2 = coordenadas.replace(",", "%2c");

        Thread thread = new Thread(new Runnable() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run(){
                HashMap<String, Object> valores = new HashMap<>();
                valores.put("center", coordenadas);
                valores.put("zoom", "12");
                valores.put("size", "40x40");
                valores.put("key", "AIzaSyDnI1pToZhDoC0MWg6NB9x6PreBlAMAoIk"); //la llave

                String respuesta = post("https://maps.googleapis.com/maps/api/staticmap", valores);

                System.out.println("Respuesta del webservice: " + respuesta);


            }
        });

        thread.start();

    }

    //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
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

                /*    dataBuilder.append(URLEncoder.encode(parametro.getKey(), "UTF-8"));
                    dataBuilder.append('=');
                    dataBuilder.append(URLEncoder.encode(parametro.getValue().toString(), "UTF-8"));
*/

                    dataBuilder.append(parametro.getKey().toString());
                    dataBuilder.append('=');
                    dataBuilder.append(parametro.getValue().toString());

                }

                datos = dataBuilder.toString();

                System.out.println(" final de ws " + datos);
            } catch (Exception ex) {
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
            conexion.setRequestMethod("GET");
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
                final Object content = conexion.getContent();
                System.out.println("contentooo  " + content.toString());


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
            System.out.println("algo salio mal al obtener la respuesta");
        } finally {
            conexion.disconnect();
        }

        return null;
    }

    public static Bitmap getGoogleMapThumbnail(double lati, double longi){
        String URL = "http://maps.google.com/maps/api/staticmap?center=" +lati + "," + longi + "&zoom=15&size=200x200&sensor=false";
        Bitmap bmp = null;
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet request = new HttpGet(URL);

        InputStream in = null;
        try {
            in = httpclient.execute(request).getEntity().getContent();
            bmp = BitmapFactory.decodeStream(in);
            in.close();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bmp;
    }

}
