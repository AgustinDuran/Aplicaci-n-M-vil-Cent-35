package agustin.prototipoapp.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import java.math.BigInteger;
import java.util.ArrayList;

import agustin.prototipoapp.R;
import agustin.prototipoapp.entidades.Mesa;
import agustin.prototipoapp.entidades.RespuestaInscFinal;
import agustin.prototipoapp.fragments.login.IniciarSesionFragment;
import agustin.prototipoapp.util.AlgoritmosDeCifrado;
import agustin.prototipoapp.util.AppUtils;
import agustin.prototipoapp.ventanas.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Esta clase va a cargar la información que se necesita y devolverá los views (de 1 en 1)
 * que se van a poder cargar en cualquier ListView.
 * <p>
 * Popups al confirmar
 * https://www.youtube.com/watch?v=67j1yIFa48s
 * <p>
 * NO SE VA A USAR:
 * https://es.stackoverflow.com/questions/75684/soltar-un-bot%C3%B3n-en-android-studio
 */

public class AdapterMesa extends BaseAdapter {

    // El activity en el que va a actuar
    private Activity activity;

    private ProgressDialog prgrssDlgCargando;

    // Dentro del ArrayList vamos a insertar los registros de Carrera
    private ArrayList<Mesa> arrayListMesas;

    // Variable contexto que vamos a utilizar para redireccionar al usuario a la URL WEB
    private Context context;

    // Constructor
    public AdapterMesa(Activity activity, ArrayList<Mesa> arrayListMesas, Context context) {
        this.activity = activity;
        this.arrayListMesas = arrayListMesas;
        this.context = context;
    }

    /**
     * Devuelve la la cantidad de registros que posee el ArrayList
     *
     * @return cantidad de registros en el ArrayList
     */
    public int getCount() {
        return arrayListMesas.size();
    }

    /**
     * Devuelve el registro de la posición que se manda como parámetro
     *
     * @param posicion
     */
    public Object getItem(int posicion) {
        return arrayListMesas.get(posicion);
    }

    // No se va a utilizar. Aunque es necesario tener el método implementado
    public long getItemId(int posicion) {
        return 0;
    }

    /**
     * Va a devolver un objeto del tipo View que se va a poder agregar al ListView, con la información
     * ya cargada.
     */
    @SuppressLint("ClickableViewAccessibility")
    public View getView(int posicion, View convertView, ViewGroup parent) {
        // Generamos un View por motivos de eficiencia
        View v = convertView;

        // Cargamos el layout de la lista que hemos creado 'item_lista_Notas_layout.xml'
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.item_lista_insc_finales_layout, null);
        }

        // Creamos objeto de la clase Nota y le colocamos el registro de la posición
        final Mesa MESA = arrayListMesas.get(posicion);

        // Inicializamos view's
        TextView txtNombreMateria = v.findViewById(R.id.txtNombreMateriaFinal);
        TextView txtFechaFinal = v.findViewById(R.id.txtFechaFinal);
        TextView txtAnioFinal = v.findViewById(R.id.txtAnioFinal);
        TextView txtLlamado = v.findViewById(R.id.txtLlamadoFinal);
        TextView txtMesaAbierta = v.findViewById(R.id.txtMesaAbierta);
        final LinearLayout boxContainer = v.findViewById(R.id.box_container);

        txtAnioFinal.setHeight(0);

        if (MESA.getLLAMADO().startsWith("1"))
            txtLlamado.setText(R.string.primer_llamado);
        else if (MESA.getLLAMADO().startsWith("2"))
            txtLlamado.setText(R.string.segundo_llamado);

        final String fecha = AppUtils.getDiaSemana(MESA.getFECHA()) + " " + AppUtils.formatFecha(MESA.getFECHA());
        txtFechaFinal.setText(fecha);

        // Evalúa cual es la primer materia del ciclo
        if (posicion == 0) {
            switch (MESA.getCICLO()) {
                case "1":
                    txtAnioFinal.setText(R.string.primer_anio);
                    txtAnioFinal.setHeight(75);
                    break;
                case "2":
                    txtAnioFinal.setText(R.string.segundo_anio);
                    txtAnioFinal.setHeight(75);
                    break;
                case "3":
                    txtAnioFinal.setText(R.string.tercer_anio);
                    txtAnioFinal.setHeight(75);
                    break;
                default:
                    txtAnioFinal.setText(R.string.libre);
                    txtAnioFinal.setHeight(75);
                    break;
            }
        } else if (MESA.getCICLO().equals("2") && arrayListMesas.get(posicion - 1).getCICLO().equals("1")) {
            txtAnioFinal.setText(R.string.segundo_anio);
            txtAnioFinal.setHeight(75);
        } else if (MESA.getCICLO().equals("3") && arrayListMesas.get(posicion - 1).getCICLO().equals("2")) {
            txtAnioFinal.setText(R.string.tercer_anio);
            txtAnioFinal.setHeight(75);
        } else if ((MESA.getTIPOMAT().replace(" ", "")).equalsIgnoreCase("LIBRE")) {
            txtAnioFinal.setText(R.string.libre);
            txtAnioFinal.setHeight(75);
        }


        if (MESA.getACTA().replace(" ", "").isEmpty() && MESA.getFECHA() != null) {
            txtMesaAbierta.setText(R.string.insc_finales_mesa_abierta);
            txtMesaAbierta.setTextColor(Color.parseColor("#088A08"));
        } else {
            txtMesaAbierta.setText(R.string.insc_finales_mesa_cerrada);
            txtMesaAbierta.setTextColor(Color.parseColor("#FF0000"));
        }

        /* Le aplicamos a cada view el nombre de carrera y su descripción */
        txtNombreMateria.setText(MESA.getMATERIA() + " " + MESA.getNOMBREMATERIA());

        final AlertDialog DIALOG = crearAlertDialog(MESA, boxContainer, fecha);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Si la Inscripcion esta abierta
                if (MESA.getACTA().replace(" ", "").isEmpty()
                        && MESA.getFECHA() != null) {
                    DIALOG.show();
                } else {
                    // Inscripción cerrada
                    Toast.makeText(context, R.string.insc_finales_mesa_cerrada_toast,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    /**
     * Crea el objeto AlertDialog
     *
     * @return
     */
    private AlertDialog crearAlertDialog(final Mesa mesa, LinearLayout contenedor, final String fecha) {

        // Creamos el objeto de tipo builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inf.inflate(R.layout.dialog_inscripcion_mesa, null);

        builder.setView(v);

        // Le agregamos el título y mensaje
        builder.setTitle(mesa.getMATERIA() + " " + mesa.getNOMBREMATERIA());
        builder.setMessage("\n" + mesa.getPRESIDENTE() + "\n\n" + fecha);

        final ProgressBar prgrssBrConfirm = v.findViewById(R.id.prgrssBrConfirm);
        final EditText dtxtPassword = v.findViewById(R.id.dialogMesaPassword);

        builder.setPositiveButton(R.string.inscribirse_boton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {

                /* if(dtxtPassword.getText().toString().isEmpty()){
                    Toast.makeText(context,"Ingrese contraseña", Toast.LENGTH_SHORT).show();
                    return;
                } */

                // SI la inscripcion a la mesa está abierta
                prgrssBrConfirm.setVisibility(View.VISIBLE);
                // convierte la contraseña a MD5 para compararla con la del usuario
                byte[] passmd5 = dtxtPassword.getText().toString().getBytes();
                BigInteger md5Data = new BigInteger(1, AlgoritmosDeCifrado.getCifradoMD5(passmd5));
                String password = md5Data.toString(16);

                // valida contraseña y realiza la inscripción devolviendo un json con dicha consulta.
                if (password.equals(IniciarSesionFragment.user.getPass())) {
                    Call<RespuestaInscFinal> callRIF = MainActivity.servicio.getRespuestaInscFinal(IniciarSesionFragment.user.getCODIGO(), mesa.getMATERIA(), mesa.getFECHA());
                    callRIF.enqueue(new Callback<RespuestaInscFinal>() {
                        @Override
                        public void onResponse(Call<RespuestaInscFinal> call, Response<RespuestaInscFinal> response) {

                            RespuestaInscFinal RIF = new RespuestaInscFinal();
                            RIF.setMensaje(response.body().getMensaje());
                            RIF.setSuccess(response.body().isSuccess());

                            final Dialog dialogInterno = new Dialog(context);
                            if (RIF.isSuccess()) {
                                //Positivo
                                dialogInterno.setContentView(R.layout.popup_positive);
                                Button btnCerrarPositivePopup = dialogInterno.findViewById(R.id.btnCerrarPopupPositive);
                                btnCerrarPositivePopup.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialogInterno.dismiss();
                                    }
                                });
                            } else {
                                //Negativo
                                dialogInterno.setContentView(R.layout.popup_negative);
                                String strError = RIF.getMensaje();
                                Button btnCerrarNegativePopup = dialogInterno.findViewById(R.id.btnCerrarPopupNegative);
                                TextView txtErrorPopup = dialogInterno.findViewById(R.id.txtErrorPopupNegative);
                                txtErrorPopup.setText("Error " + strError);
                                btnCerrarNegativePopup.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialogInterno.dismiss();
                                    }
                                });

                            }

                            prgrssBrConfirm.setVisibility(View.INVISIBLE);
                            dialogInterno.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialogInterno.show();
                            dialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<RespuestaInscFinal> call, Throwable t) {
                            prgrssDlgCargando.dismiss();
                            Toast.makeText(context, R.string.error_sin_internet,
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    prgrssBrConfirm.setVisibility(View.INVISIBLE);
                    dtxtPassword.setText("");
                    Toast.makeText(context, R.string.pass_incorrecta,
                            Toast.LENGTH_SHORT).show();
                }


            }
        });

        // El botón que cierra el mensaje
        builder.setNegativeButton(R.string.cerrar_boton, new DialogInterface.OnClickListener() {
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

}