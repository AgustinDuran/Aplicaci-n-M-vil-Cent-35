package agustin.prototipoapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import agustin.prototipoapp.R;
import agustin.prototipoapp.entidades.FechaEvento;

/**
 * Esta clase va a cargar la información que se necesita y devolverá los views (de 1 en 1)
 * que se van a poder cargar en cualquier ListView.
 */

public class AdapterFechaEvento extends BaseAdapter {

    // El activity en el que va a actuar
    private Activity activity;

    // Dentro del ArrayList vamos a insertar los registros de FechaEvento
    private ArrayList<FechaEvento> arrayListFechaEvento;

    // Constructor
    public AdapterFechaEvento(Activity activity, ArrayList<FechaEvento> arrayListFechaEvento) {
        this.activity = activity;
        this.arrayListFechaEvento = arrayListFechaEvento;
    }

    /**
     * Devuelve la la cantidad de registros que posee el ArrayList
     * @return cantidad de registros en el ArrayList
     */
    public int getCount(){
        return arrayListFechaEvento.size();
    }

    /**
     * Devuelve el registro de la posición que se manda como parámetro
     * @param posicion
     */
    public Object getItem(int posicion){
        return arrayListFechaEvento.get(posicion);
    }

    // No se va a utilizar. Aunque es necesario tener el método implementado
    public long getItemId(int posicion){
        return 0;
    }

    /**
     * Va a devolver un objeto del tipo View que se va a poder agregar al ListView, con la información
     * ya cargada.
     */
    public View getView(int posicion, View convertView, ViewGroup parent) {
        // Generamos un View por motivos de eficiencia
        View v = convertView;

        // Cargamos el layout de la lista que hemos creado 'item_lista_fechas_layout_layout.xml'
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.item_lista_fechas_layout, null);
        }

        // Creamos objeto de la clase FechaEvento y le colocamos el registro de la posición
        FechaEvento fe = arrayListFechaEvento.get(posicion);

        // Inicializamos view's
        TextView txtFechaEvento = v.findViewById(R.id.txtFechaEvento);
        TextView txtDescripcion = v.findViewById(R.id.txtDescripcionEvento);

        /*
         * si el registro tiene un diaFin (por defecto, cuando no lo tiene, es 0)
         * entonces va a imprimir en el TextView una cadena de caracteres del tipo
         * DD/MM/YYYY HASTA DD/MM/YYYY
         * Siendo el primero el día dónde comienza el evento y el segundo la fecha de fin
         * Si no tiene un diaFin entonces se deja el formato de una sola fecha DD / MM / YYYY en el
         * TextView
         */
        if (fe.getDiaFin() != 0)
            txtFechaEvento.setText(fe.getDia() +"/" +fe.getMes() +"/" +fe.getAño() +" HASTA "
                    +fe.getDiaFin() +"/" +fe.getMesFin() +"/" +fe.getAñoFin());
        else
            txtFechaEvento.setText(fe.getDia() +" / " +fe.getMes() +" / " +fe.getAño());

        // Se agrega la descripción
        txtDescripcion.setText(fe.getDescripcion());

        return v;
    }

}
