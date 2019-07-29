package agustin.prototipoapp.fragments.menuAlumno;


import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import agustin.prototipoapp.R;
import agustin.prototipoapp.adapters.AdapterNota;
import agustin.prototipoapp.entidades.Nota;
import agustin.prototipoapp.fragments.login.IniciarSesionFragment;
import agustin.prototipoapp.util.AppUtils;
import agustin.prototipoapp.ventanas.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotasFragment extends Fragment implements Callback<List<Nota>> {

    private ListView lstVwNotas;
    private ProgressBar prgrssBrNotas;

    ArrayList<Nota> arrayListNotas;

    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        arrayListNotas = new ArrayList<Nota>();

        // Inicializo el ListView
        lstVwNotas = getView().findViewById(R.id.lstVwNotas);

        prgrssBrNotas = getView().findViewById(R.id.prgrssBrNotas);
        prgrssBrNotas.setVisibility(View.VISIBLE);
        prgrssBrNotas.getIndeterminateDrawable()
                .setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        cargarJson();
    }

    Call<List<Nota>> call;
    private void cargarJson() {
        call = MainActivity.servicio.getNota(IniciarSesionFragment.user.getCODIGO());
        call.enqueue(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notas, container, false);
    }

    @Override
    public void onResponse(Call<List<Nota>> call, Response<List<Nota>> response) {
        // Obtengo el ArrayList del objeto "conversor"
        List<Nota> notas = response.body();

        if (notas != null) {

            for (Nota nota : notas) {
                arrayListNotas.add(nota);
            }

            // Manda a crear la vista que se va a acoplar al ListView.
            AdapterNota adapter = new AdapterNota(getActivity(), arrayListNotas, getContext());
            lstVwNotas.setAdapter(adapter);
        }
        prgrssBrNotas.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onFailure(Call<List<Nota>> call, Throwable throwable) {
        if (flagStop == false) {
            prgrssBrNotas.setVisibility(View.INVISIBLE);
            // Si hubo problemas al momento de leer el objeto JSON advierto al usuario de lo ocurrido
            AppUtils.validarConexion(getContext());
            // imagen de reparación
            getView().findViewById(R.id.imagenRepNotas).setVisibility(View.VISIBLE);
        }
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
