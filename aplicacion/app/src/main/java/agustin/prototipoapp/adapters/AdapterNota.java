package agustin.prototipoapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import agustin.prototipoapp.R;
import agustin.prototipoapp.entidades.Anuncio;
import agustin.prototipoapp.entidades.Nota;
import agustin.prototipoapp.entidades.RespuestaNota;
import agustin.prototipoapp.util.AppUtils;

/**
 * Esta clase va a cargar la información que se necesita y devolverá los views (de 1 en 1)
 * que se van a poder cargar en cualquier ListView.
 */

public class AdapterNota extends BaseAdapter {

    private String
            colorAprobado = "#088A08",
            colorRegular = "#D7DF01",
            colorLibre = "#FF0000",
            colorFaltaLibreta = "#FF0000",
            colorNoRegularizada = "#FF868686";

    // Se van a utilizar para el dialog
    private String
            mesasDisponibles = "",
            estado = "",
            anioMateria = "";

    // El activity en el que va a actuar
    private Activity activity;

    // Dentro del ArrayList vamos a insertar los registros de Carrera
    private ArrayList<Nota> arrayListNotas;

    // Variable contexto que vamos a utilizar para redireccionar al usuario a la URL WEB
    private Context context;

    // Constructor
    public AdapterNota(Activity activity, ArrayList<Nota> arrayListNotas, Context context) {
        this.activity = activity;
        this.arrayListNotas = arrayListNotas;
        this.context = context;
    }

    /**
     * Devuelve la la cantidad de registros que posee el ArrayList
     * @return cantidad de registros en el ArrayList
     */
    public int getCount(){
        return arrayListNotas.size();
    }

    /**
     * Devuelve el registro de la posición que se manda como parámetro
     * @param posicion
     */
    public Object getItem(int posicion){
        return arrayListNotas.get(posicion);
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

        // Cargamos el layout de la lista que hemos creado 'item_lista_Notas_layout.xml'
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.item_lista_notas_layout, null);
        }

        // Creamos objeto de la clase Nota y le colocamos el registro de la posición
        final Nota NOTA = arrayListNotas.get(posicion);

        // Inicializamos view's
        TextView txtNombreMateria = v.findViewById(R.id.txtNombreMateria);
        TextView txtNota = v.findViewById(R.id.txtNota);
        TextView txtEstado = v.findViewById(R.id.txtEstado);
        final LinearLayout contenedor = v.findViewById(R.id.box_container);

        // Lo hago invisible para las materias que no terminan en 1 (Ejemplo: SO12)
        TextView txtAnioNota = v.findViewById(R.id.txtAnioNota);
        txtAnioNota.setHeight(0);

        // Evalúa cual es la primer materia del ciclo
        if ( posicion == 0 ) {
            txtAnioNota.setHeight(75);
            txtAnioNota.setText( "PRIMER AÑO" );
        } else if (NOTA.getCICLO().equals("2") && arrayListNotas.get(posicion-1).getCICLO().equals("1") ) {
            txtAnioNota.setHeight(75);
            txtAnioNota.setText( "SEGUNDO AÑO" );
        } else if (NOTA.getCICLO().equals("3") && arrayListNotas.get(posicion-1).getCICLO().equals("2") ) {
            txtAnioNota.setHeight(75);
            txtAnioNota.setText( "TERCER AÑO" );
        }

        // Evalua de que ciclo es la materia, tener en cuenta que todas las carreras tienen 3 años maximo
        anioMateria = NOTA.getCICLO().equals("1") ? "primer año" : (NOTA.getCICLO().equals("2") ? "segundo año" : "tercer año");

        /* Le aplicamos a cada view el nombre de carrera y su descripción */
        txtNombreMateria.setText( NOTA.getMATERIA() +" " +NOTA.getNOMBREMATERIA() );

        RespuestaNota rn = getEstadoFormateado(NOTA);
        estado = rn.getInfor();

        // Le aplica color
        SpannableString sColored = new SpannableString( "ESTADO: " +rn.getInfor() );
        sColored.setSpan(new ForegroundColorSpan( rn.getColorTexto() ), 7, rn.getInfor().length()+8, 0);

        txtEstado.setText( sColored );

        if (! (NOTA.getAPROBADA().startsWith(" ")) ) {
            txtNota.setText("CALIFICACIÓN: " + NOTA.getNOTA());
            txtNota.setHeight(60);
        } else if (rn.getInfor().equalsIgnoreCase("REGULAR")) {

            if ( ! NOTA.getACTA3().replace(" ","").isEmpty() ) {
                txtNota.setText("MESAS DISPONIBLES: 0");
                mesasDisponibles = "0";
            } else if ( ! NOTA.getACTA2().replace(" ","").isEmpty() ) {
                txtNota.setText("MESAS DISPONIBLES: 1");
                mesasDisponibles = "1";
            } else if ( ! NOTA.getACTA1().replace(" ","").isEmpty() ) {
                txtNota.setText("MESAS DISPONIBLES: 2");
                mesasDisponibles = "2";
            } else {
                txtNota.setText("MESAS DISPONIBLES: 3");
                mesasDisponibles = "3";
            }

            txtNota.setHeight(60);
        } else
            txtNota.setHeight(0);

        // Creamos el objeto AlertDialog, (La ventana emergente)
        final AlertDialog DIALOG = crearAlertDialog(NOTA);

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

    private RespuestaNota getEstadoFormateado(Nota nota) {

        int colorTexto = Color.parseColor("#FF868686");
        String texto = "";

        if ( nota.getFOLIO().startsWith("EQUIV") || nota.getFOLIO().startsWith("equiv") ) {
            texto = "APROBADA COMO EQUIVALENCIA";
            colorTexto = Color.parseColor(colorAprobado);
            return new RespuestaNota(texto, colorTexto);
        }

        switch ( nota.getESTADOCUR().replace(" ","") ){

            case "Regul":
                if (nota.getAPROBADA().replace(" ","").isEmpty()) {

                    if (nota.getPUEDEFIN().startsWith("FALTA LIBRETA")) {
                        texto = "FALTA LIBRETA";
                        colorTexto = Color.parseColor(colorFaltaLibreta);
                    } else if (nota.getPUEDEFIN().startsWith("VENCIDA")) {
                        texto = "VENCIDA";
                        colorTexto = Color.parseColor(colorLibre);
                    }else{
                        texto = "REGULAR";
                        colorTexto = Color.parseColor(colorRegular);
                    }
                } else {
                    texto = "APROBADA";
                    colorTexto = Color.parseColor(colorAprobado);
                }
                break;

            case "Promo":
                texto = "PROMOCIONADA";
                colorTexto = Color.parseColor(colorAprobado);
                break;

            case "Libre":
                texto = "LIBRE";
                colorTexto = Color.parseColor(colorLibre);
                if (nota.getPUEDEFIN().startsWith("FALTA LIBRETA"))
                    texto = "FALTA LIBRETA";
                else if (nota.getPUEDEFIN().startsWith("YA FUE APROB")) {
                    texto = "APROBADA COMO LIBRE";
                    colorTexto = Color.parseColor(colorAprobado);
                }
                break;

            case "":
                if ( nota.getPUEDEFIN().startsWith("VENCIDA") ) {
                    texto = "VENCIDA";
                    colorTexto = Color.parseColor(colorLibre);
                } else if (nota.getPUEDEFIN().startsWith("FALTA LIBRETA")) {
                    texto = "FALTA LIBRETA";
                    colorTexto = Color.parseColor(colorFaltaLibreta);
                } else {
                    texto = "NO REGULARIZADA";
                    colorTexto = Color.parseColor(colorNoRegularizada);
                }
                break;
        }

        return new RespuestaNota(texto, colorTexto);
    }

    /**
     * Crea el objeto AlertDialog
     * @param nota
     * @return
     */
    public AlertDialog crearAlertDialog(Nota nota){

        // Creamos el objeto de tipo builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inf.inflate(R.layout.dialog_nota, null);

        builder.setView(v);

        // Datos que siempre van a estar
        ((TextView)v.findViewById(R.id.txtCodMat)).setText(nota.getMATERIA());
        ((TextView)v.findViewById(R.id.txtNomMat)).setText(nota.getNOMBREMATERIA());
        ((TextView)v.findViewById(R.id.txtTipoMat)).setText(anioMateria +" " +nota.getTIPOMAT());
        ((TextView)v.findViewById(R.id.txtEstadoMateria)).setText("Estado: " +estado);

        if (estado.equalsIgnoreCase("REGULAR")) {
            ((TextView)v.findViewById(R.id.txtFinalVence)).setText("Vence en: " +nota.getVENCEEN());
            hacerVisible(R.id.txtFinalVence, v);
        } else {
            hacerInvisible(R.id.txtFinalVence, v);
        }

        // Agrega los textView de notas
        hacerInvisible(R.id.txtNota1, v);
        hacerInvisible(R.id.txtNota2, v);
        hacerInvisible(R.id.txtNota3, v);
        if (!(nota.getACTA1().replace(" ","").isEmpty())) {
            if(!(nota.getACTA2().replace(" ","").isEmpty())) {
                if (!(nota.getACTA3().replace(" ","").isEmpty())) {
                    ((TextView)v.findViewById(R.id.txtNota3)).setText("Rendida el " +AppUtils.formatFecha(nota.getFECHA3()) +" nota "+ nota.getNOTA3());
                    hacerVisible(R.id.txtNota3, v);
                }
                ((TextView)v.findViewById(R.id.txtNota2)).setText("Rendida el " +AppUtils.formatFecha(nota.getFECHA2()) +" nota " +nota.getNOTA2());
                hacerVisible(R.id.txtNota2, v);
            }
            ((TextView)v.findViewById(R.id.txtNota1)).setText("Rendida el " +AppUtils.formatFecha(nota.getFECHA1()) +" nota " +nota.getNOTA1());
            hacerVisible(R.id.txtNota1, v);
        }

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

    private void hacerInvisible(int id, View v){
        ((TextView)v.findViewById(id)).setHeight(0);
    }

    private void hacerVisible(int id, View v){
        ((TextView)v.findViewById(id)).setHeight(60);
    }

}
