package agustin.prototipoapp.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import agustin.prototipoapp.R;
import agustin.prototipoapp.entidades.Anuncio;

/**
 * Esta clase va a cargar la información que se necesita y devolverá los views (de 1 en 1)
 * que se van a poder cargar en cualquier ListView.
 *
 * Created by agustin on 30/12/17.
 *
 * Animación del dialog agregada:
 * http://www.devexchanges.info/2015/10/showing-dialog-with-animation-in-android.html
 * se utilizan los archivos
 */

public class AdapterAnuncio extends BaseAdapter {

    // El activity en el que va a actuar
    private Activity activity;

    // Dentro del ArrayList vamos a insertar los registros de Carrera
    private ArrayList<Anuncio> arrayListAnuncio;

    // Variable contexto que vamos a utilizar para redireccionar al usuario a la URL WEB
    private Context context;

    // Constructor
    public AdapterAnuncio(Activity activity, ArrayList<Anuncio> arrayListAnuncio, Context context) {
        this.activity = activity;
        this.arrayListAnuncio = arrayListAnuncio;
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
        return arrayListAnuncio.size();
    }

    /**
     * Devuelve el registro de la posición que se manda como parámetro
     * @param posicion
     */
    public Object getItem(int posicion){
        return arrayListAnuncio.get(posicion);
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

        // Cargamos el layout de la lista que hemos creado 'item_lista_anuncios_layout.xml'
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.item_lista_anuncios_layout, null);
        }

        // Creamos objeto de la clase Anuncio y le colocamos el registro de la posición
        /* En este caso queremos que el último anuncio sea el primero en el listView, es decir,
         * el más reciente. Por eso invertimos el sentido, pidiendo la cantidad de item's
         * restandole la posición que se busca y un -1 */
        final Anuncio ANUNCIO = arrayListAnuncio.get(getCount() - posicion - 1);

        // Inicializamos view's
        final TextView txtAsunto = v.findViewById(R.id.txtAsunto);
        final TextView txtDescripcion = v.findViewById(R.id.txtDescripcionAnuncio);
        final TextView txtFecha = v.findViewById(R.id.txtFecha);
        final LinearLayout contenedor = v.findViewById(R.id.box_container);

        /* Le aplicamos a cada view el asunto */
        txtAsunto.setText(ANUNCIO.getAsunto());

        // Creamos una variable que nos va a servir más adelante
        String subtexto;

        // Si la descripción grande, le agregamos unos puntos suspensivos
        if (ANUNCIO.getDescripcion().length() > 45) {
            txtDescripcion.setText(ANUNCIO.getDescripcion().substring(0,40) +" ...");
            subtexto = ANUNCIO.getDescripcion();
        } else {
            txtDescripcion.setText(ANUNCIO.getDescripcion());
            subtexto = ANUNCIO.getDescripcion();
        }

        // Aplicamos la fecha
        txtFecha.setText(ANUNCIO.getFecha());

        // Creamos el objeto AlertDialog, (La ventana emergente)
        final AlertDialog DIALOG = crearAlertDialog(ANUNCIO, subtexto, ANUNCIO.getFecha());

        /*  se le aplica un listener. Cada vez que se oprima,
         *  se va a mostrar la ventana emergente con la información */
        contenedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // muestra el mensaje emergente
                DIALOG.show();
            }
        });

        return v;
    }

    /**
     * Crea el objeto AlertDialog
     * @param anuncio
     * @return
     */
    public AlertDialog crearAlertDialog(Anuncio anuncio, String subtexto, String fecha){

        final TextView txtVwSubTexto = new TextView(context);

        // Creamos el objeto de tipo builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Le agregamos el título y mensaje
        builder.setTitle(anuncio.getAsunto());

        txtVwSubTexto.setPadding(50,20,50,0);
        txtVwSubTexto.setText(subtexto +"\n\n" +fecha);
        //txtVwSubTexto.setMovementMethod(LinkMovementMethod.getInstance());
        Linkify.addLinks( txtVwSubTexto , Linkify.WEB_URLS);
        builder.setView(txtVwSubTexto);

        builder.setCancelable(true);
        // El botón que cierra el mensaje
        builder.setNegativeButton("CERRAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Creamos y aplicamos la animación
        final AlertDialog DIALOG = builder.create();
        DIALOG.getWindow().getAttributes().windowAnimations = R.style.AnimacionDialog;

        return DIALOG;
    }
}