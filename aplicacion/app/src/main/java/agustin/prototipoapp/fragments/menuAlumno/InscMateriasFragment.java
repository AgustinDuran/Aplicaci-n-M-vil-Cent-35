package agustin.prototipoapp.fragments.menuAlumno;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import java.util.List;

import agustin.prototipoapp.R;
import agustin.prototipoapp.adapters.AdapterMateriaInsc;
import agustin.prototipoapp.entidades.Nota;
import agustin.prototipoapp.entidades.RespuestaInscMaterias;
import agustin.prototipoapp.fragments.login.IniciarSesionFragment;
import agustin.prototipoapp.util.AppUtils;
import agustin.prototipoapp.ventanas.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class InscMateriasFragment extends Fragment {

    private ListView lstVwMateriasInsc;
    ArrayList<Nota> arrayListMateriasInsc;
    private TextView txtTitulo, txtTitulo2;
    private Button btnInscribirse;
    private ProgressDialog prgrssDlgCargando;
    private ProgressBar pbInscMat;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_insc_materias, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        arrayListMateriasInsc = new ArrayList<Nota>();

        // Inicializo el ListView
        lstVwMateriasInsc = getView().findViewById(R.id.lstVwInscMaterias);
        txtTitulo = getView().findViewById(R.id.txtInscMatTitulo);
        txtTitulo2 = getView().findViewById(R.id.txtInscMatTitulo2);
        btnInscribirse = getView().findViewById(R.id.btnInscribirse);
        pbInscMat = getView().findViewById(R.id.prgrssBrInscMat);
        prgrssDlgCargando = new ProgressDialog(getContext());

        btnInscribirse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prgrssDlgCargando.setTitle(R.string.cargando_titulo);
                prgrssDlgCargando.setMessage("Por favor, espere mientras se realiza la inscripción");
                prgrssDlgCargando.show();

                Call<RespuestaInscMaterias> callRIM = MainActivity.servicio.getRespuestaInscMaterias(IniciarSesionFragment.user.getCODIGO());
                callRIM.enqueue(new Callback<RespuestaInscMaterias>() {
                    @Override
                    public void onResponse(Call<RespuestaInscMaterias> call, Response<RespuestaInscMaterias> response) {

                        RespuestaInscMaterias RIM = new RespuestaInscMaterias();
                        RIM.setMensaje(response.body().getMensaje());
                        RIM.setSuccess(response.body().isSuccess());

                        final Dialog dialogInterno = new Dialog(getContext());
                        if (RIM.isSuccess()){
                            //Positivo
                            dialogInterno.setContentView(R.layout.popup_positive);
                            Button btnCerrarPositivePopup = dialogInterno.findViewById(R.id.btnCerrarPopupPositive);
                            TextView txtMensaje = dialogInterno.findViewById(R.id.txtMensajePositivePopup);
                            txtMensaje.setText(R.string.positive_popup_text_insc_materias);
                            btnCerrarPositivePopup.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialogInterno.dismiss();
                                }
                            });
                        } else {
                            //Negativo
                            dialogInterno.setContentView(R.layout.popup_negative);
                            String strError = RIM.getMensaje();
                            Button btnCerrarNegativePopup = dialogInterno.findViewById(R.id.btnCerrarPopupNegative);
                            TextView txtErrorPopup = dialogInterno.findViewById(R.id.txtErrorPopupNegative);
                            txtErrorPopup.setText("Error " +strError);
                            btnCerrarNegativePopup.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialogInterno.dismiss();
                                }
                            });

                        }
                        prgrssDlgCargando.dismiss();
                        dialogInterno.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialogInterno.show();
                    }

                    @Override
                    public void onFailure(Call<RespuestaInscMaterias> call, Throwable t) {
                        prgrssDlgCargando.dismiss();
                        Toast.makeText(getContext(), R.string.error_sin_internet,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        cargarJson();

    }

    Call<List<Nota>> call;
    private void cargarJson() {
        call = MainActivity.servicio.getMateriaInsc(IniciarSesionFragment.user.getCODIGO());
        call.enqueue(new Callback<List<Nota>>() {
            @Override
            public void onResponse(Call<List<Nota>> call, Response<List<Nota>> response) {

                txtTitulo.setVisibility(View.VISIBLE);
                txtTitulo2.setVisibility(View.VISIBLE);
                btnInscribirse.setVisibility(View.VISIBLE);
                // boleano que controla que el usuario no mande inscripción si tiene
                // materias en SI VER HORARIOS
                boolean flagMateriaSI = false;

                // Obtengo el ArrayList del objeto "conversor"
                List<Nota> materias = response.body();

                if (materias != null && !materias.isEmpty()) {
                    for (Nota materia : materias) {
                        arrayListMateriasInsc.add(materia);
                        // Si hay materias con 'SI' habilita el botón para inscribirse
                        if (!flagMateriaSI && materia.getPUEDECUR().replace(" ", "").equalsIgnoreCase("SI")){
                            flagMateriaSI = true;
                        }
                    }
                    // Manda a crear la vista que se va a acoplar al ListView.
                    AdapterMateriaInsc adapter = new AdapterMateriaInsc(getActivity(), arrayListMateriasInsc, getContext());
                    lstVwMateriasInsc.setAdapter(adapter);
                    if (flagMateriaSI){
                        txtTitulo.setText(R.string.insc_materias_success);
                    } else {
                        txtTitulo.setText(R.string.insc_materias_fail);
                        btnInscribirse.setVisibility(View.INVISIBLE);
                    }
                } else {
                    txtTitulo.setText(R.string.insc_materias_fail);
                    getView().findViewById(R.id.txtInscMatTitulo2).setVisibility(View.INVISIBLE);
                    btnInscribirse.setVisibility(View.INVISIBLE);
                    lstVwMateriasInsc.setVisibility(View.INVISIBLE);
                }
                pbInscMat.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onFailure(Call<List<Nota>> call, Throwable t) {
                if (flagStop == false) {
                    // Si hubo problemas al momento de leer el objeto JSON advierto al usuario de lo ocurrido
                    AppUtils.validarConexion(getContext());
                    btnInscribirse.setVisibility(View.INVISIBLE);
                    txtTitulo2.setVisibility(View.INVISIBLE);
                    txtTitulo.setVisibility(View.INVISIBLE);
                    pbInscMat.setVisibility(View.INVISIBLE);
                    // imagen de reparación
                    getView().findViewById(R.id.imagenRepInscMat).setVisibility(View.VISIBLE);
                }
            }
        });
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
