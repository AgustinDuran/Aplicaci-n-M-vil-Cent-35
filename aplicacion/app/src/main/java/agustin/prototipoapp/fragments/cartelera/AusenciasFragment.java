package agustin.prototipoapp.fragments.cartelera;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import agustin.prototipoapp.R;
import agustin.prototipoapp.adapters.AdapterAusencia;
import agustin.prototipoapp.entidades.Ausencia;
import agustin.prototipoapp.util.AppUtils;
import agustin.prototipoapp.ventanas.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AusenciasFragment extends Fragment implements Callback<List<Ausencia>> {


    private ArrayList<Ausencia> arrayListAusencia;
    private ListView lstVwAusencias;
    private PullRefreshLayout pullRefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ausencias, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        arrayListAusencia = new ArrayList<Ausencia>();
        // Inicializo el ListView
        lstVwAusencias = getView().findViewById(R.id.lstVwAusencias);
        /*
         * Objeto del tipo SwipeRefreshLayout, para hacer uso de él debemos insertar en el
         * 'build.gradle' la sentencia:
         *  compile "com.baoyz.pullrefreshlayout:library:1.2.0"
         * Este view lo que hace es permitirnos 'refrescar' el ListView si hacermos un movimiento
         * con el dedo corriendo el ListView hacia abajo.
         */
        pullRefresh = getView().findViewById(R.id.swipeRefreshLayout);

        // Le aplico el listener al SwipeRefreshLayout
        pullRefresh.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullRefresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cargarJson();
                        pullRefresh.setRefreshing(false);
                    }
                }, 3000);
            }
        });

        // Otros estilos de carga (animación)
        //pullRefresh.setRefreshStyle(PullRefreshLayout.STYLE_SMARTISAN);
        //pullRefresh.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);
        pullRefresh.setRefreshStyle(PullRefreshLayout.STYLE_RING);

        cargarJson();
    }

    Call<List<Ausencia>> call;
    private void cargarJson() {
        //txtSinRegistros.setVisibility(View.INVISIBLE);
        arrayListAusencia.clear();
        call = MainActivity.servicio.getAusencias();
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<List<Ausencia>> call, Response<List<Ausencia>> response) {
        // Obtengo el ArrayList del objeto "conversor"
        List<Ausencia> ausencias = response.body();
        getView().findViewById(R.id.txtAusenciasTitulo).setVisibility(View.VISIBLE);
        if (ausencias != null) {
            for (Ausencia aus : ausencias) {
                arrayListAusencia.add(aus);
            }
            // Manda a crear la vista que se va a acoplar al ListView.
            AdapterAusencia adapter = new AdapterAusencia(getActivity(), arrayListAusencia, getContext());
            lstVwAusencias.setAdapter(adapter);
        } else {
            // Vaciar el ListView
            lstVwAusencias.setAdapter(null);
        }
    }

    @Override
    public void onFailure(Call<List<Ausencia>> call, Throwable throwable) {
        if (flagStop == false) {
            // Si hubo problemas al momento de leer el objeto JSON advierto al usuario de lo ocurrido
            AppUtils.validarConexion(getContext());
            // imagen de reparación
            getView().findViewById(R.id.imagenRepAusencias).setVisibility(View.VISIBLE);
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
