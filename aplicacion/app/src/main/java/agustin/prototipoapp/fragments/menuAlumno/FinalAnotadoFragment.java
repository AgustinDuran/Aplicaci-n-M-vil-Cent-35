package agustin.prototipoapp.fragments.menuAlumno;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import agustin.prototipoapp.R;
import agustin.prototipoapp.adapters.AdapterFinalAnotado;
import agustin.prototipoapp.entidades.FinalAnotado;
import agustin.prototipoapp.fragments.login.IniciarSesionFragment;
import agustin.prototipoapp.util.AppUtils;
import agustin.prototipoapp.ventanas.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FinalAnotadoFragment extends Fragment implements Callback<List<FinalAnotado>> {


    private ArrayList<FinalAnotado> arrayListFinalAnotado;
    private TextView txtAnotaFinTitulo;
    private ListView lstVwFinalAnotado;
    private ProgressBar pbAnotaFin;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_final_anotado, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        arrayListFinalAnotado = new ArrayList<FinalAnotado>();
        // Inicializo el ListView
        lstVwFinalAnotado = getView().findViewById(R.id.lstVwFinalAnotado);
        txtAnotaFinTitulo = getView().findViewById(R.id.txtAnotaFinTitulo);
        pbAnotaFin = getView().findViewById(R.id.prgrssBrAnotaFin);

        cargarJson();
    }

    private Call<List<FinalAnotado>> call;
    private void cargarJson() {
        call = MainActivity.servicio.getFinalAnotado( IniciarSesionFragment.user.getCODIGO() );
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<List<FinalAnotado>> call, Response<List<FinalAnotado>> response) {
        // Obtengo el ArrayList del objeto "conversor"
        List<FinalAnotado> finalesAnotados = response.body();
        if (finalesAnotados != null) {

            for (FinalAnotado fAnotado : finalesAnotados) {
                arrayListFinalAnotado.add(fAnotado);
            }

            // Manda a crear la vista que se va a acoplar al ListView.


            AdapterFinalAnotado adapter = new AdapterFinalAnotado(getActivity(), arrayListFinalAnotado, getContext());
            pbAnotaFin.setVisibility(View.INVISIBLE);
            lstVwFinalAnotado.setAdapter(adapter);

        } else {
            /* Si el objeto cjeo devuelve un array nulo se 'activa'
             *  el TextView dónde le informa al
             * usuario que no hay anuncios */
            txtAnotaFinTitulo.setText(R.string.anota_fin_fail);
            // Vaciar el ListView
            pbAnotaFin.setVisibility(View.INVISIBLE);
            lstVwFinalAnotado.setAdapter(null);
        }
    }

    @Override
    public void onFailure(Call<List<FinalAnotado>> call, Throwable throwable) {
        if (flagStop == false) {
            pbAnotaFin.setVisibility(View.INVISIBLE);
            AppUtils.validarConexion(getContext());
            txtAnotaFinTitulo.setVisibility(View.INVISIBLE);
            getView().findViewById(R.id.imagenRepAnotaFin).setVisibility(View.VISIBLE);
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
