package agustin.prototipoapp.fragments.login;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;

import agustin.prototipoapp.R;
import agustin.prototipoapp.entidades.Alumno;
import agustin.prototipoapp.entidades.RespuestaCambioPassword;
import agustin.prototipoapp.entidades.RespuestaLogin;
import agustin.prototipoapp.fragments.menuAlumno.MenuAlumnoFragment;
import agustin.prototipoapp.util.AlgoritmosDeCifrado;
import agustin.prototipoapp.util.AppUtils;
import agustin.prototipoapp.ventanas.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class IniciarSesionFragment extends Fragment {

    // El objeto que va a contener los datos del usuario del alumno
    public static Alumno user;
    private ProgressDialog prgrssDlgCargando;
    private final RespuestaLogin rl = new RespuestaLogin();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.iniciar_sesion, container, false);
    }

    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        // Inicializo view's
        final TextView txtAyuda = getView().findViewById(R.id.txtAyuda);
        final EditText dtxtLegajo = getView().findViewById(R.id.dtxtLegajoAlumno);
        final EditText dtxtPassword = getView().findViewById(R.id.dtxtPassword);
        final Button btnIngresar = getView().findViewById(R.id.btnIngresar);
        prgrssDlgCargando = new ProgressDialog(getContext());

        // Aplico los colores a los hint de los EditText's
        dtxtLegajo.setHintTextColor(getResources().getColor(R.color.colorHint));
        dtxtPassword.setHintTextColor(getResources().getColor(R.color.colorHint));

        // dialog de ayuda
        final Dialog dialogAyuda = new Dialog(getContext());

        // propiedades del dialog ayuda
        dialogAyuda.setContentView(R.layout.popup_help);
        Button btnCerrarPopup = dialogAyuda.findViewById(R.id.btnCerrarPopupHelp);
        btnCerrarPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAyuda.dismiss();
            }
        });
        dialogAyuda.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Listener del TextView de ayuda
        txtAyuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAyuda.show();
            }
        });

        // Listener del boton
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Valida los campos
                if (dtxtLegajo.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), R.string.validacion_legajo_vacio,
                            Toast.LENGTH_SHORT).show();
                    return;
                } else if (dtxtPassword.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), R.string.validacion_pass_vacio,
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                String legajoString = dtxtLegajo.getText().toString();
                // Cifrado en md5
                byte[] passmd5 = dtxtPassword.getText().toString().getBytes();
                BigInteger md5Data = new BigInteger(1, AlgoritmosDeCifrado.getCifradoMD5(passmd5));
                String password = md5Data.toString(16);

                // Oculta el teclado (por defecto no se oculta)
                inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(dtxtLegajo.getWindowToken(), 0);
                inputMethodManager.hideSoftInputFromWindow(dtxtPassword.getWindowToken(), 0);

                // Inicia la sesión
                iniciarSesion(legajoString, password);
            }
        });
    }
    private InputMethodManager inputMethodManager;

    private void cargarJson() {

        Call<Alumno> call = MainActivity.servicio.getAlumno(user.getCODIGO());
        call.enqueue(new Callback<Alumno>() {
            @Override
            public void onResponse(Call<Alumno> call, Response<Alumno> response) {
                cargarDatosAlumno(response);

                Fragment fragment = new MenuAlumnoFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.deslizar_fragment_izquierda_entrada, R.anim.deslizar_fragment_izquierda_salida)
                        .replace(R.id.contenedor, fragment)
                        .commit();

                prgrssDlgCargando.dismiss();
            }

            @Override
            public void onFailure(Call<Alumno> call, Throwable t) {
                prgrssDlgCargando.dismiss();
                user = null;
                Toast.makeText(getContext(), R.string.error_login, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void iniciarSesion(final String legajoString, final String password) {
        // Muestra una barra de cargando
        prgrssDlgCargando.setTitle(R.string.cargando_titulo);
        prgrssDlgCargando.setMessage("Por favor, espere mientras corroboramos los datos");
        prgrssDlgCargando.show();

        // Valida si el usuario existe en el servidor/database
        Call<RespuestaLogin> callLogin = MainActivity.servicio.getRespuestaLogin(Integer.parseInt(legajoString), password);
        callLogin.enqueue(new Callback<RespuestaLogin>() {
            @Override
            public void onResponse(Call<RespuestaLogin> call, Response<RespuestaLogin> response) {
                rl.setDatosCorrectos(response.body().isDatosCorrectos());
                rl.setMensaje(response.body().getMensaje());
                rl.setCambioPass(response.body().isCambioPass());

                if (rl.isDatosCorrectos()) {
                    if (rl.isCambioPass() == false) {
                        // CAMBIAR PASS
                        Dialog DIALOG = crearAlertDialogCambioPass();
                        user = null;
                        prgrssDlgCargando.dismiss();
                        DIALOG.show();
                    } else {
                        cargarJson();
                    }
                } else {
                    prgrssDlgCargando.dismiss();
                    Toast.makeText(getContext(), rl.getMensaje(),
                            Toast.LENGTH_LONG).show();
                    user = null;
                }
            }

            @Override
            public void onFailure(Call<RespuestaLogin> call, Throwable t) {
                prgrssDlgCargando.dismiss();
                AppUtils.validarConexion(getContext());
            }

            private Dialog crearAlertDialogCambioPass() {
                // Creamos el objeto de tipo builder
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                View v = getLayoutInflater().inflate(R.layout.registro, null);

                builder.setView(v);

                final EditText dtxtNewPass1 = v.findViewById(R.id.dtxtNuevaPass1);
                final EditText dtxtNewPass2 = v.findViewById(R.id.dtxtNuevaPass2);

                builder.setPositiveButton("ENVÍAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {

                        // CAMPOS VACIOS
                        if (dtxtNewPass1.getText().toString().isEmpty() ||
                                dtxtNewPass2.getText().toString().isEmpty()) {
                            Toast.makeText(getContext(), "Complete los campos", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Comparación de pass nuevas
                        if (!dtxtNewPass1.getText().toString().equals(dtxtNewPass2.getText().toString())) {
                            Toast.makeText(getContext(), "Las nuevas contraseñas no son iguales", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        byte[] newpassmd5 = dtxtNewPass1.getText().toString().getBytes();
                        BigInteger md5DataNew = new BigInteger(1, AlgoritmosDeCifrado.getCifradoMD5(newpassmd5));
                        final String passwordNew = md5DataNew.toString(16);

                        // valida contraseña y realiza la inscripción devolviendo un json con dicha consulta.
                        Call<RespuestaCambioPassword> callRCP = MainActivity.servicio.getRespuestaCambioPassword(Integer.parseInt(legajoString), passwordNew);
                        callRCP.enqueue(new Callback<RespuestaCambioPassword>() {
                            @Override
                            public void onResponse(Call<RespuestaCambioPassword> call, Response<RespuestaCambioPassword> response) {

                                RespuestaCambioPassword RCP = new RespuestaCambioPassword();
                                RCP.setMensaje(response.body().getMensaje());
                                RCP.setSuccess(response.body().isSuccess());

                                //final Dialog dialogInterno = new Dialog(getContext());
                                if (RCP.isSuccess()) {
                                    Toast.makeText(getContext(), RCP.getMensaje(),
                                            Toast.LENGTH_SHORT).show();
                                    iniciarSesion(legajoString, passwordNew);
                                } else {
                                    /*dialogInterno.setContentView(R.layout.popup_negative);
                                      String strError = RIF.getMensaje();
                                      Button btnCerrarNegativePopup = dialogInterno.findViewById(R.id.btnCerrarPopupNegative);
                                      TextView txtErrorPopup = dialogInterno.findViewById(R.id.txtErrorPopupNegative);
                                      txtErrorPopup.setText("Error " + strError);
                                      btnCerrarNegativePopup.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                               dialogInterno.dismiss();
                                          }
                                      });*/
                                    Toast.makeText(getContext(), RCP.getMensaje(),
                                            Toast.LENGTH_LONG).show();
                                }
                                /*dialogInterno.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialogInterno.show();*/

                                dialog.dismiss();
                                // Oculta el teclado (por defecto no se oculta)
                                //TODO cual de todos estos funciona?
                                inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                inputMethodManager.hideSoftInputFromWindow(dtxtNewPass1.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                                inputMethodManager.hideSoftInputFromWindow(dtxtNewPass2.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                                inputMethodManager.hideSoftInputFromWindow(dtxtNewPass1.getWindowToken(), 0);
                                inputMethodManager.hideSoftInputFromWindow(dtxtNewPass2.getWindowToken(), 0);
                                inputMethodManager.hideSoftInputFromWindow( getActivity().getCurrentFocus().getWindowToken(), 0 );
                            }

                            @Override
                            public void onFailure(Call<RespuestaCambioPassword> call, Throwable t) {
                                AppUtils.validarConexion(getContext());
                            }
                        });
                    }
                });

                // El botón que cierra el mensaje
                builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
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
        });

        // Genera el objeto singleton de alumno
        user = Alumno.getSingletonInstance(Integer.parseInt(legajoString), password);

    }

    private void cargarDatosAlumno(Response<Alumno> response) {
        user.setAPROBADAS(response.body().getAPROBADAS());
        user.setACTIVO(response.body().getACTIVO());
        user.setAUXILIAR(response.body().getAUXILIAR());
        user.setCARRERA(response.body().getCARRERA());
        user.setCONTACTO(response.body().getCONTACTO());
        user.setDEBEFINAL(response.body().getDEBEFINAL());
        user.setESREGULAR(response.body().getESREGULAR());
        user.setDIRECC(response.body().getDIRECC());
        user.setFECHAFICHA(response.body().getFECHAFICHA());
        user.setFECHAPOR(response.body().getFECHAPOR());
        user.setFINGRE(response.body().getFINGRE());
        user.setFEGRE(response.body().getFEGRE());
        user.setFLIBRETA(response.body().getFLIBRETA());
        user.setFNACIM(response.body().getFNACIM());
        user.setGRADOSAL(response.body().getGRADOSAL());
        user.setIESTECOK(response.body().getIESTECOK());
        user.setTITULOSEC(response.body().getTITULOSEC());
        user.setLIBRETA(response.body().getLIBRETA());
        user.setNOMBRE(response.body().getNOMBRE());
        user.setSEXO(response.body().getSEXO());
        user.setTELEFONO(response.body().getTELEFONO());
        user.setLOCNACIM(response.body().getLOCNACIM());
        user.setPROVNACIM(response.body().getPROVNACIM());
        user.setPAISNACIM(response.body().getPAISNACIM());
        user.setMATCURSA(response.body().getMATCURSA());
        user.setMODIPOR(response.body().getMODIPOR());
        user.setPROCESAR(response.body().getPROCESAR());
        user.setTIPDOC(response.body().getTIPDOC());
        user.setOBSERV(response.body().getOBSERV());
        user.setNUMDOC(response.body().getNUMDOC());
        user.setMAIL(response.body().getMAIL());
        user.setMAILMALO(response.body().getMAILMALO());
    }

}
