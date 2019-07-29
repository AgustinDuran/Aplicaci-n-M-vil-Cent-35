package agustin.prototipoapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import agustin.prototipoapp.R;
import agustin.prototipoapp.entidades.Carrera;

/**
 * Esta clase va a cargar la información que se necesita y devolverá los views (de 1 en 1)
 * que se van a poder cargar en cualquier ListView.
 */

public class AdapterCarrera extends BaseAdapter{

    // El activity en el que va a actuar
    private Activity activity;

    // Dentro del ArrayList vamos a insertar los registros de Carrera
    private ArrayList<Carrera> arrayListCarrera;

    // Variable contexto que vamos a utilizar para redireccionar al usuario a la URL WEB
    private Context context;

    // Constructor
    public AdapterCarrera(Activity activity, ArrayList<Carrera> arrayListCarrera, Context context) {
        this.activity = activity;
        this.arrayListCarrera = arrayListCarrera;
        this.context = context;
    }

    /**
     * Devuelve la la cantidad de registros que posee el ArrayList
     * @return cantidad de registros en el ArrayList
     */
    public int getCount(){
        return arrayListCarrera.size();
    }

    /**
     * Devuelve el registro de la posición que se manda como parámetro
     * @param posicion
     */
    public Object getItem(int posicion){
        return arrayListCarrera.get(posicion);
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

        // Cargamos el layout de la lista que hemos creado 'item_lista_carreras_layout.xml'
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.item_lista_carreras_layout, null);
        }

        // Creamos objeto de la clase Carrera y le colocamos el registro de la posición
        final Carrera CAR = arrayListCarrera.get(posicion);

        // Inicializamos view's
        TextView txtNombreCarrera = v.findViewById(R.id.txtNombreCarrera);
        TextView txtDescripcionCarrera = v.findViewById(R.id.txtDescripcionCarrera);
        LinearLayout contenedor = v.findViewById(R.id.box_container);

        /* Le aplicamos a cada view el nombre de carrera y su descripción */
        txtNombreCarrera.setText(CAR.getNombreCarrera());
        txtDescripcionCarrera.setText(CAR.getDescripcionCarrera());

        /* También a cada View individual se le aplica un listener. Cada vez que se oprima
         * se lo va a redireccionar a la dirección web dentro de la página del CENT 35
         * de dicha carrera */
        contenedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentWeb = new Intent(Intent.ACTION_VIEW, Uri.parse(CAR.getUrlCarrera()));
                context.startActivity(intentWeb);
            }
        });

        return v;
    }
}
