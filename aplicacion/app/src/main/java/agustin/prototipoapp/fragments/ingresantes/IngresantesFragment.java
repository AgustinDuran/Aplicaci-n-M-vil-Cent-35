package agustin.prototipoapp.fragments.ingresantes;


import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import agustin.prototipoapp.R;

import agustin.prototipoapp.adapters.AdapterCarrera;
import agustin.prototipoapp.entidades.Carrera;
import agustin.prototipoapp.util.AppUtils;
import agustin.prototipoapp.ventanas.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IngresantesFragment extends Fragment implements Callback<List<Carrera>> {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ingresantes, container, false);
    }

    private ListView lstVwCarreras;
    private ProgressBar prgrssBrIngresantes;

    ArrayList<Carrera> arrayListCarrera;

    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        arrayListCarrera = new ArrayList<Carrera>();

        // Inicializo el ListView
        lstVwCarreras = getView().findViewById(R.id.lstVwCarrera);

        prgrssBrIngresantes = getView().findViewById(R.id.prgrssBrIngresantes);
        prgrssBrIngresantes.setVisibility(View.VISIBLE);
        prgrssBrIngresantes.getIndeterminateDrawable()
                .setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        cargarJson();

        // Manda a crear la vista que se va a acoplar al ListView.
        AdapterCarrera adapter = new AdapterCarrera(getActivity(), arrayListCarrera, getContext());
        lstVwCarreras.setAdapter(adapter);
    }

    Call<List<Carrera>> call;
    private void cargarJson() {
        call = MainActivity.servicio.getCarreras();
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<List<Carrera>> call, Response<List<Carrera>> response) {
        // Obtengo el ArrayList del objeto "conversor"
        List<Carrera> carreras = response.body();
        if (carreras != null) {

            for (Carrera carrera : carreras) {
                arrayListCarrera.add(carrera);
            }

            // Manda a crear la vista que se va a acoplar al ListView.
            AdapterCarrera adapter = new AdapterCarrera(getActivity(), arrayListCarrera, getContext());
            lstVwCarreras.setAdapter(adapter);
        }
        prgrssBrIngresantes.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onFailure(Call<List<Carrera>> call, Throwable throwable) {
        if (flagStop == false) {
            // imagen de reparación
            getView().findViewById(R.id.imagenRepIngresantes).setVisibility(View.VISIBLE);
            prgrssBrIngresantes.setVisibility(View.INVISIBLE);
            // Si hubo problemas al momento de leer el objeto JSON advierto al usuario de lo ocurrido
            AppUtils.validarConexion(getContext());
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