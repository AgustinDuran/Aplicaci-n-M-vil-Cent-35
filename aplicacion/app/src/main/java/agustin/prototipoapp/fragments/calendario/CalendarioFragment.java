package agustin.prototipoapp.fragments.calendario;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import agustin.prototipoapp.R;
import agustin.prototipoapp.adapters.AdapterFechaEvento;
import agustin.prototipoapp.entidades.FechaEvento;
import agustin.prototipoapp.fragments.inicio.InicioFragment;
import agustin.prototipoapp.util.AppUtils;
import agustin.prototipoapp.ventanas.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Clase que contiene to.do lo relacionado al calendario académico
 * Se utiliza un CompactCalendarView. Para utilizar dicho view especial fue necesario
 * agregar "compile 'com.github.sundeepk:compact-calendar-view:2.0.3-beta'" en el apartado
 * "dependencies" del "build.gradle" (o 'app').
 *
 * https://github.com/SundeepK/CompactCalendarView
 * https://www.youtube.com/watch?v=xs5406vApTo
 *
 */
//@RequiresApi(api = Build.VERSION_CODES.N)
public class CalendarioFragment extends Fragment implements Callback<List<FechaEvento>> {

    // Elementos del fragment
    private CompactCalendarView calendarioView;
    private TextView txtMesAñoTitulo;
    private ListView lstVwEventos;
    private ProgressBar prgrssBrCalendario;

    ArrayList<FechaEvento> arrayListFechaEvento;

    // Fecha actual, con milisegundos
    private Calendar now = Calendar.getInstance();

    // Array con los meses del año, se va a utilizar para determinar el mes actual, luego.
    String[] arrayMeses = new String[]{
            "ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE",
            "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendario, container, false);
    }

    /**arrayMeses[now.get(Calendar.MONTH)] +" " +now.get(Calendar.YEAR)
     * En este método puedo hacer acciones con los view's del fragment
     * @param state
     */
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        arrayListFechaEvento = new ArrayList<FechaEvento>();

        // Inicializo view's
        txtMesAñoTitulo = getView().findViewById(R.id.txtMesAñoTitulo);
        txtMesAñoTitulo.setVisibility(View.INVISIBLE);

        lstVwEventos = getView().findViewById(R.id.lstVwEventos);
        lstVwEventos.setVisibility(View.INVISIBLE);

        calendarioView = getView().findViewById(R.id.calendario);
        calendarioView.setVisibility(View.INVISIBLE);

        prgrssBrCalendario = getView().findViewById(R.id.prgrssBrCalendario);
        prgrssBrCalendario.setVisibility(View.VISIBLE);
        prgrssBrCalendario.getIndeterminateDrawable()
                .setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        // Aplica el estilo de tres letras en el día del calendar (Ejemplo: Lun, Mar)
        calendarioView.setUseThreeLetterAbbreviation(true);

        cargarJson();

        /**
         * Le asigno Listeners al calendario. Cuando hay un cambio de día se ejecuta onDayClick(Date dateClicked)
         * y cuando hay un cambio de mes, es onMonthScroll(Date firtsDayOfNewMonth)
         */
        calendarioView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                    verificarSiHayEventosEnEseDia(dateClicked, arrayListFechaEvento);
            }

                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onMonthScroll(Date firstDayOfNewMonth) {
                    // El formato que se va a utilizar en el titulo superior "MES AÑO"
                    txtMesAñoTitulo.setText(arrayMeses[firstDayOfNewMonth.getMonth()] +" " +(firstDayOfNewMonth.getYear()+1900));
                    verificarSiHayEventosEnEseDia(firstDayOfNewMonth, arrayListFechaEvento);

                    // Evalúa los límites, que la fecha a buscar no sea mayor a 2020 ni menor a 2017, lo prohibe
                    if ((firstDayOfNewMonth.getYear() + 1900) < 2017) {
                        calendarioView.setCurrentDate(new Date(117, 0, 1));
                        txtMesAñoTitulo.setText(R.string.primer_mes);
                    } else if ((firstDayOfNewMonth.getYear() + 1900) > 2020) {
                        calendarioView.setCurrentDate(new Date(120, 11, 1));
                        txtMesAñoTitulo.setText(R.string.ultimo_mes);
                    }
                }
            });

        }

    Call<List<FechaEvento>> call;
    private void cargarJson() {
        call = MainActivity.servicio.getFechasEvento();
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<List<FechaEvento>> call, Response<List<FechaEvento>> response) {
        // Obtengo el ArrayList del objeto "conversor"
        List<FechaEvento> fechasEvento = response.body();
        if (fechasEvento != null) {
            for (FechaEvento fechaEvento : fechasEvento) {
                arrayListFechaEvento.add(fechaEvento);
            }
        }

        cargarPuntosFechas();
        prgrssBrCalendario.setVisibility(View.INVISIBLE);
        txtMesAñoTitulo.setVisibility(View.VISIBLE);
        lstVwEventos.setVisibility(View.VISIBLE);
        calendarioView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFailure(Call<List<FechaEvento>> call, Throwable throwable) {
        if (flagStop == false) {
            prgrssBrCalendario.setVisibility(View.INVISIBLE);
            // Si hubo problemas al momento de leer el objeto JSON advierto al usuario de lo ocurrido
            AppUtils.validarConexion(getContext());
            // imagen de reparación
            getView().findViewById(R.id.imagenRepCalendario).setVisibility(View.VISIBLE);
        }
    }

    private void cargarPuntosFechas() {

        FechaEvento fe;
        GregorianCalendar gregCal;
        //Este for va guardando los eventos en el calendario, es decir, los va marcando con un punto azul debajo
        for (int i=0; i < arrayListFechaEvento.size(); i++) {
            fe = arrayListFechaEvento.get(i);

            // GregorianCalendar con la fecha del registro 'i'
            gregCal = new GregorianCalendar(fe.getAño(), (fe.getMes() - 1), fe.getDia());
            // Transformo la fecha en formato de milisegundos (ya que así lo solicita para pasarlo como parámetro)
            calendarioView.addEvent(new Event(Color.BLUE, gregCal.getTimeInMillis(), fe.getDescripcion()));
        }

        // Carga el més actual en el título y verifica si en el día actual hay algún evento
        // Si esto no se hace, no aparece en el ListView apenas ingresamos al Calendario Académico
        txtMesAñoTitulo.setText(arrayMeses[now.get(Calendar.MONTH)] +" " +now.get(Calendar.YEAR));
        // Fecha actual sin milisegundos, va a servir para comparar con las demás fechas.
        Date fechaActual = new Date(now.get(Calendar.YEAR) -1900, now.get(Calendar.MONTH),
                now.get(Calendar.DATE));

        verificarSiHayEventosEnEseDia(fechaActual, arrayListFechaEvento);

    }

    public void verificarSiHayEventosEnEseDia(Date dateClicked, ArrayList<FechaEvento> registrosFechas){
        ArrayList<FechaEvento> registrosFechaTemporal = new ArrayList<FechaEvento>();

        for (int i=0; i < registrosFechas.size(); i++) {
            // Pasa la fecha a Date
            Date eventoFecha = new Date(registrosFechas.get(i).getAño() - 1900,
                    registrosFechas.get(i).getMes() - 1, registrosFechas.get(i).getDia());

            /* Compara la fecha que tiene el foco con cada fecha del Json
             * si hay eventos en el día que tiene el foco entonces se agrega al
             * ListView la vista con la información de que contiene ese evento */
            if (dateClicked.equals(eventoFecha))
                registrosFechaTemporal.add(registrosFechas.get(i));
        }

        // Manda a crear la vista que se va a acoplar al ListView.
        AdapterFechaEvento adapter = new AdapterFechaEvento(getActivity(), registrosFechaTemporal);
        lstVwEventos.setAdapter(adapter);
    }

    private boolean flagStop = false;
    @Override
    public void onStop() {
        super.onStop();
        flagStop = true;
        // Cancelo la petición al servidor si sale
        call.cancel();
    }

}
