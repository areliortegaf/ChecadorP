package fordpasa.com.checador;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Principal on 01/02/2018.
 */

public class RegistrarUsuario {

    public static RegistroListener listener;




    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public RegistrarUsuario(final String clave, final String correo, final String cotrasena){

        Thread thread = new Thread(new Runnable() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run(){
                HashMap<String, Object> valores = new HashMap<>();
                valores.put("clave", clave);
                valores.put("correo", correo);
                valores.put("contrasena", cotrasena);

                String respuesta = post("http://sweetdev.net/agregar_usuarioPASA.php", valores);

                System.out.println(respuesta);
                int resp = leerRespuesta(respuesta);//regresa 1 o 0
                //el listener
                if (listener != null) {
                    listener.activarRegistro(resp);
                }

            }
        });

        thread.start();
    }
  /*  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String registroDeUsuario(final String clave, final String correo, final String cotrasena){
        Thread thread = new Thread(new Runnable() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run(){
                HashMap<String, Object> valores = new HashMap<>();
                valores.put("clave", clave);
                valores.put("correo", correo);
                valores.put("contrasena", cotrasena);

                String respuesta = post("http://sweetdev.net/agregar_usuarioPASA.php", valores);

                System.out.println(respuesta);
            }
        });

        thread.start();
    }
*/

    public int leerRespuesta(String resultado){
        int i = resultado.indexOf('1');
        if(i>= 0){
            return 1;
        }else{
            return 0;
        }
    }

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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String get(String pagina, Map<String, Object> valores) {
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
                System.out.println("erroooooooor");
            }
        }

        /**
         * Se crea la conexion.
         */
        try {
            url = new URL(pagina + "?" + datos);

            conexion = (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException ex) {
            System.out.println("URL MAL FORMADA");
        } catch (IOException ex) {
            System.out.println("ERROR EN LA URL");
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
            System.out.println("error en el tipo de peticion");
        }

        // Se deshabilita el cache.
        conexion.setUseCaches(false);
        // Se especifica que queremos obtener la respuesta del servidor.
        conexion.setDoOutput(true);

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
            System.out.println("otra vez error en la respuesta");
        } finally {
            conexion.disconnect();
        }

        return null;
    }



  /*  Thread thread = new Thread(new Runnable() {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void run(){
            HashMap<String, Object> valores = new HashMap<>();
            valores.put("clave", "4");
            valores.put("correo", "pruebota");
            valores.put("contrasena", "pruebotototaaaaaa");

            String respuesta = post("http://sweetdev.net/agregar_usuarioPASA.php", valores);

            System.out.println(respuesta);
        }
    });
**/
}
