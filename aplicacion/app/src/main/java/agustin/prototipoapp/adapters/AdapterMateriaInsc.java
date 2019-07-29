package agustin.prototipoapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import agustin.prototipoapp.R;
import agustin.prototipoapp.entidades.Nota;

public class AdapterMateriaInsc extends BaseAdapter {

        private String
                colorSI = "#088A08",
                colorSIverHorarios = "#D7DF01";

        // El activity en el que va a actuar
        private Activity activity;

        // Dentro del ArrayList vamos a insertar los registros de Carrera
        private ArrayList<Nota> arrayListMateriasInsc;

        // Variable contexto que vamos a utilizar para redireccionar al usuario a la URL WEB
        private Context context;

        // Constructor
        public AdapterMateriaInsc(Activity activity, ArrayList<Nota> arrayListMateriasInsc, Context context) {
            this.activity = activity;
            this.arrayListMateriasInsc = arrayListMateriasInsc;
            this.context = context;
        }

        /**
         * Devuelve la la cantidad de registros que posee el ArrayList
         *
         * @return cantidad de registros en el ArrayList
         */
        public int getCount() {
            return arrayListMateriasInsc.size();
        }

        /**
         * Devuelve el registro de la posición que se manda como parámetro
         *
         * @param posicion
         */
        public Object getItem(int posicion) {
            return arrayListMateriasInsc.get(posicion);
        }

        // No se va a utilizar. Aunque es necesario tener el método implementado
        public long getItemId(int posicion) {
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
                v = inf.inflate(R.layout.item_materia_layout, null);
            }

            // Creamos objeto de la clase Nota y le colocamos el registro de la posición
            final Nota MATERIA = arrayListMateriasInsc.get(posicion);

            // Inicializamos view's
            TextView txtNombreMateria = v.findViewById(R.id.txtNombreMateriaInsc);
            TextView txtTipoMateria = v.findViewById(R.id.txtTipoMateriaInsc);
            TextView txtPuedeInsc = v.findViewById(R.id.txtPuedeInsc);

            // Lo hago invisible para las materias que no terminan en 1 (Ejemplo: SO12)
            TextView txtAnioNota = v.findViewById(R.id.txtAnioNotaInsc);
            txtAnioNota.setHeight(0);

            // Evalúa cual es la primer materia del ciclo
            if (posicion == 0) {
                txtAnioNota.setHeight(75);
                txtAnioNota.setText("PRIMER AÑO");
            } else if (MATERIA.getCICLO().equals("2") && arrayListMateriasInsc.get(posicion - 1).getCICLO().equals("1")) {
                txtAnioNota.setHeight(75);
                txtAnioNota.setText("SEGUNDO AÑO");
            } else if (MATERIA.getCICLO().equals("3") && arrayListMateriasInsc.get(posicion - 1).getCICLO().equals("2")) {
                txtAnioNota.setHeight(75);
                txtAnioNota.setText("TERCER AÑO");
            }

            /* Le aplicamos a cada view el nombre de carrera y su descripción */
            txtNombreMateria.setText(MATERIA.getMATERIA() + " " + MATERIA.getNOMBREMATERIA());


            SpannableString sColored = new SpannableString("¿PUEDE INSCRIBIRSE? " +MATERIA.getPUEDECUR());;
            if (MATERIA.getPUEDECUR().replace("  ", "").equalsIgnoreCase("SI ver horarios")) {
                sColored.setSpan(new ForegroundColorSpan(Color.parseColor(colorSIverHorarios)), 19, MATERIA.getPUEDECUR().length() + 20, 0);
            } else {
                sColored.setSpan(new ForegroundColorSpan(Color.parseColor(colorSI)), 19, MATERIA.getPUEDECUR().length() + 20, 0);
            }

            txtPuedeInsc.setText(sColored);
            txtTipoMateria.setText("TIPO MATERIA: " +MATERIA.getTIPOMAT());

            return v;
        }

    }