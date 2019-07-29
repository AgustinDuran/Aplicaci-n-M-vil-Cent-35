package agustin.prototipoapp.fragments.menuAlumno;


import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import agustin.prototipoapp.R;
import agustin.prototipoapp.adapters.AdapterMesa;
import agustin.prototipoapp.entidades.Mesa;
import agustin.prototipoapp.fragments.login.IniciarSesionFragment;
import agustin.prototipoapp.util.AppUtils;
import agustin.prototipoapp.ventanas.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class InscFinalesFragment extends Fragment implements Callback<List<Mesa>> {

    private ListView lstVwMesas;
    private ProgressBar prgrssBrMesas;
    private Button btnFinalAnotado;
    private TextView txtMesas;

    ArrayList<Mesa> arrayListMesas;

    // Fecha actual con milisegundos
    private Calendar now = Calendar.getInstance();
    // Fecha actual sin milisegundos
    Date fechaActual = new Date(now.get(Calendar.YEAR) -1900, now.get(Calendar.MONTH),
            now.get(Calendar.DATE));

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_insc_finales, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        arrayListMesas = new ArrayList<Mesa>();

        btnFinalAnotado = getView().findViewById(R.id.btnFinalAnotado);
        txtMesas = getView().findViewById(R.id.txtMesasInscFin);
        // Inicializo el ListView
        lstVwMesas = getView().findViewById(R.id.lstVwMesas);

        prgrssBrMesas = getView().findViewById(R.id.prgrssBrMesas);
        prgrssBrMesas.setVisibility(View.VISIBLE);
        prgrssBrMesas.getIndeterminateDrawable()
                .setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        // Consulto la fecha actual del sistema, para ir a la mesa más próxima
        String mes;
        if (fechaActual.getMonth()+1 >= 11)
            mes = "5-Diciembre";
        else if (fechaActual.getMonth()+1 >= 9)
            mes = "4-Septiembre";
        else if (fechaActual.getMonth()+1 >= 7 && fechaActual.getDay() >= 20 || fechaActual.getMonth()+1 >= 8)
            mes = "3-Agosto";
        else if (fechaActual.getMonth()+1 >= 4 && fechaActual.getDay() >= 20 || fechaActual.getMonth()+1 >= 5)
            mes = "2-Mayo";
        else
            mes = "1-Marzo";

        txtMesas.setText("Mesas de " +mes.substring(2,mes.length()));

        cargarJson(mes);

        btnFinalAnotado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.menuAlumnoFlag = true;
                Fragment fragment = new FinalAnotadoFragment();

                getActivity().getSupportFragmentManager().beginTransaction()
                        // Le aplica una animación que está guardada como archivos '.xml' en 'res/anim'
                        .setCustomAnimations(R.anim.deslizar_fragment_izquierda_entrada, R.anim.deslizar_fragment_izquierda_salida)
                        .replace(R.id.contenedor, fragment)
                        .commit();
            }
        });

    }

    Call<List<Mesa>> call;
    private void cargarJson(String mes) {
        call = MainActivity.servicio.getMesa(IniciarSesionFragment.user.getCODIGO(), fechaActual.getYear()+1900, mes);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<List<Mesa>> call, Response<List<Mesa>> response) {
        btnFinalAnotado.setVisibility(View.VISIBLE);
        txtMesas.setVisibility(View.VISIBLE);
        // Obtengo el ArrayList del objeto "conversor"
        List<Mesa> mesas = response.body();

        if (mesas != null) {
            for (Mesa mesa : mesas) {
                arrayListMesas.add(mesa);
            }
            // Manda a crear la vista que se va a acoplar al ListView.
            AdapterMesa adapter = new AdapterMesa(getActivity(), arrayListMesas, getContext());
            lstVwMesas.setAdapter(adapter);
        } else {
            getView().findViewById(R.id.txtNoFinales).setVisibility(View.VISIBLE);
        }
        prgrssBrMesas.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onFailure(Call<List<Mesa>> call, Throwable throwable) {
        if (flagStop == false) {
            prgrssBrMesas.setVisibility(View.INVISIBLE);
            AppUtils.validarConexion(getContext());
            getView().findViewById(R.id.imagenRepInscFin).setVisibility(View.VISIBLE);
        }
    }

    private boolean flagStop = false;
    @Override
    public void onStop() {
        super.onStop();
        flagStop = true;
        call.cancel();
    }
}
