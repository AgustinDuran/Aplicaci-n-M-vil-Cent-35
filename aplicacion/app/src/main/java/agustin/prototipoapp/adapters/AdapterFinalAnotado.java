package agustin.prototipoapp.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import agustin.prototipoapp.R;
import agustin.prototipoapp.entidades.FinalAnotado;
import agustin.prototipoapp.util.AppUtils;

/**
 * Esta clase va a cargar la información que se necesita y devolverá los views (de 1 en 1)
 * que se van a poder cargar en cualquier ListView.
 *
 * Created by agustin on 30/12/17.
 *
 */

public class AdapterFinalAnotado extends BaseAdapter {

    // El activity en el que va a actuar
    private Activity activity;

    private ArrayList<FinalAnotado> arrayListFinalAnotado;

    // Variable contexto que vamos a utilizar para redireccionar al usuario a la URL WEB
    private Context context;

    // Constructor
    public AdapterFinalAnotado(Activity activity, ArrayList<FinalAnotado> arrayListFinalAnotado, Context context) {
        this.activity = activity;
        this.arrayListFinalAnotado = arrayListFinalAnotado;
        this.context = context;
    }

    /**
     * Devuelve la la cantidad de registros que posee el ArrayList
     * NOTA: tira errores de vez en cuando, te suma uno a la longitud
     * es mejor utilizar el length de la clase 'ConversorJsonEnObjeto.java'
     * @return cantidad de registros en el ArrayList
     * @deprecated
     */
    public int getCount(){
        return arrayListFinalAnotado.size();
    }

    /**
     * Devuelve el registro de la posición que se manda como parámetro
     * @param posicion
     */
    public Object getItem(int posicion){
        return arrayListFinalAnotado.get(posicion);
    }

    // No se va a utilizar. Aunque es necesario tener el método implementado
    public long getItemId(int posicion){
        return 0;
    }

    /**
     * Va a devolver un objeto del tipo View que se va a poder agregar al ListView, con la información
     * ya cargada.
     */
    @SuppressLint("ResourceAsColor")
    public View getView(int posicion, View convertView, ViewGroup parent) {
        // Generamos un View por motivos de eficiencia
        View v = convertView;

        // Cargamos el layout de la lista que hemos creado 'item_lista_AUSENCIAs_layout.xml'
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.item_lista_final_anotado_layout, null);
        }

        // Creamos objeto de la clase FINAL y le colocamos el registro de la posición
        /* En este caso queremos que el último FINAL sea el primero en el listView, es decir,
         * el más reciente. Por eso invertimos el sentido, pidiendo la cantidad de item's
         * restandole la posición que se busca y un -1 */
        final FinalAnotado FINAL = arrayListFinalAnotado.get(posicion);

        // Inicializamos view's
        final TextView txtMateria = v.findViewById(R.id.txtFinalAnotadoMateria);
        final TextView txtFecha = v.findViewById(R.id.txtFinalAnotadoFecha);

        /* Le aplicamos a cada view el asunto */
        txtMateria.setText(FINAL.getMATERIA() +" " +FINAL.getNOMBREMATERIA());
        txtFecha.setText(AppUtils.getDiaSemana(FINAL.getFECHA()) +" " +AppUtils.formatFecha(FINAL.getFECHA()));

        return v;
    }

}
