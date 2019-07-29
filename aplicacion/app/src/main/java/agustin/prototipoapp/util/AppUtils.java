package agustin.prototipoapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.util.Calendar;

import agustin.prototipoapp.R;

public class AppUtils {
    public static int rotacion = 0;

    public static String getDiaSemana(String fecha) {

        if (fecha.replace(" ", "").equalsIgnoreCase(""))
            return "";


        int anio = Integer.parseInt(fecha.substring(0, 4));
        int mes = Integer.parseInt(fecha.substring(4, 6));
        int dia = Integer.parseInt(fecha.substring(6, 8));

        Calendar c = Calendar.getInstance();
        c.set(anio, mes, dia);
        int diaSemana = c.get(Calendar.DAY_OF_WEEK);


        switch (diaSemana) {
            case 1:
                return "DOMINGO";
            case 2:
                return "LUNES";
            case 3:
                return "MARTES";
            case 4:
                return "MIÉRCOLES";
            case 5:
                return "JUEVES";
            case 6:
                return "VIERNES";
            default:
                return "SÁBADO";

        }

    }

    public static String formatFecha(String fecha) {
        if (fecha == null || fecha.equals("") || fecha.replace(" ", "").equalsIgnoreCase(""))
            return "SIN FECHA PROGRAMADA";
        else
            return  fecha.substring(6, 8) +"/" +fecha.substring(4, 6) +"/" +fecha.substring(0, 4);
    }

    /**
     * Valida la conexión a internet del usuario
     */
    public static boolean validarConexion(Context c) {
        ConnectivityManager connectivityManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info_wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo info_datos = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (String.valueOf(info_wifi.getState()).equals("CONNECTED")) {
            Toast.makeText(c, R.string.toast_no_conexion_server, Toast.LENGTH_LONG).show();
            //conectado por wifi
            return true;
        } else {
            if (String.valueOf(info_datos.getState()).equals("CONNECTED")) {
                Toast.makeText(c, R.string.toast_no_conexion_server, Toast.LENGTH_LONG).show();
                // conectado por DATOS MOVILES
                return true;
            } else {
                // Sin ningún tipo de conexión
                Toast.makeText(c, R.string.toast_no_internet, Toast.LENGTH_LONG).show();
                return false;
            }
        }
    }

}
