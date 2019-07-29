package agustin.prototipoapp.fragments.cartelera;


import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import agustin.prototipoapp.R;
import agustin.prototipoapp.adapters.AdapterAnuncio;
import agustin.prototipoapp.entidades.Anuncio;
import agustin.prototipoapp.util.AppUtils;
import agustin.prototipoapp.ventanas.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Clase dónde va a estar toda la lógica del apartado 'Cartelera'
 *
 * Pull refresh: https://www.youtube.com/watch?v=nM5qyYZexg4
 * https://www.hell-desk.com/swipe-refresh-en-android/
 */
public class CarteleraFragment extends Fragment implements Callback<List<Anuncio>> {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cartelera, container, false);
    }

    // Creamos los view's
    private ListView lstVwCartelera;
    private TextView txtSinRegistros;
    private PullRefreshLayout pullRefresh;
    private ProgressBar prgrssBrCartelera;

    // la variable 'id' (unívoca) que va a tener cada anuncio
    // <Esto se hace para luego saber cuales anuncios fueron vistos por el usuario y cuales no.
    private int idAnuncio;


    ArrayList<Anuncio> arrayListAnuncio;

    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        arrayListAnuncio = new ArrayList<Anuncio>();

        // Inicializo el ListView
        lstVwCartelera = getView().findViewById(R.id.lstVwCartelera);

        // Este TextView aparecerá sólo si el objeto 'cjeo' devuelve un array nulo
        txtSinRegistros = getView().findViewById(R.id.txtSinRegistros);
        txtSinRegistros.setVisibility(View.INVISIBLE);

        prgrssBrCartelera = getView().findViewById(R.id.prgrssBrCartelera);
        prgrssBrCartelera.setVisibility(View.VISIBLE);
        prgrssBrCartelera.getIndeterminateDrawable()
                .setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

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

    Call<List<Anuncio>> call;
    private void cargarJson() {
        txtSinRegistros.setVisibility(View.INVISIBLE);
        // Que no repita registros
        arrayListAnuncio.clear();
        call = MainActivity.servicio.getAnuncios();
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<List<Anuncio>> call, Response<List<Anuncio>> response) {
        // Obtengo el ArrayList del objeto "conversor"
        List<Anuncio> anuncios = response.body();
        if (anuncios != null) {

            for (Anuncio anuncio : anuncios) {
                arrayListAnuncio.add(anuncio);
            }

            // Manda a crear la vista que se va a acoplar al ListView.
            AdapterAnuncio adapter = new AdapterAnuncio(getActivity(), arrayListAnuncio, getContext());
            lstVwCartelera.setAdapter(adapter);

            //TODO En caso de que hayan notificaciones activas (en el panel superior), las destruye.
            /*if(ServicioConsultaAnuncios.notiManager != null){
                ServicioConsultaAnuncios.notiManager.cancelAll();
            }*/
        } else {
            /* Si el objeto cjeo devuelve un array nulo se 'activa'
             *  el TextView dónde le informa al
             * usuario que no hay anuncios */
            txtSinRegistros.setVisibility(View.VISIBLE);
            // Vaciar el ListView
            lstVwCartelera.setAdapter(null);
        }
        prgrssBrCartelera.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onFailure(Call<List<Anuncio>> call, Throwable throwable) {
        if (flagStop == false) {
            prgrssBrCartelera.setVisibility(View.INVISIBLE);
            /*
             * Si entra en el catch, significa que el .json está vacío
             * <O que, alguno de los parámetros (url,nombre) están mal>
             *     <o que, no tiene internet el usuario>
             *
             * Si está vacío significa que el usuario mediante la aplicación de escritorio
             * decidió vacíar el archivo que contiene los anuncios.
             */

            // Si hubo problemas al momento de leer el objeto JSON advierto al usuario de lo ocurrido
            AppUtils.validarConexion(getContext());

            // imagen de reparación
            getView().findViewById(R.id.imagenRepCartelera).setVisibility(View.VISIBLE);
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