package agustin.prototipoapp.fragments.menuAlumno;


import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import agustin.prototipoapp.R;
import agustin.prototipoapp.fragments.login.IniciarSesionFragment;
import agustin.prototipoapp.util.AppUtils;

public class PerfilAlumnoFragment extends Fragment {


    private ProgressBar prgrssBrDatos;
    private TextView txtProvNacim, txtCarrera, txtDireccion, txtUltAct, txtLegajo,  txtLibreta,
    txtMail, txtNombre, txtTelefono, txtPaisNacim, txtAnioCur, txtDebeFinales, txtEsRegular,
    txtFechaIng, txtFechaNac, txtMateriasAprob, txtLocNacim,
    txtNumDoc, txtMateriasCur;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perfil_alumno, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        prgrssBrDatos = getView().findViewById(R.id.prgrssBrDatos);
        prgrssBrDatos.setVisibility(View.VISIBLE);
        prgrssBrDatos.getIndeterminateDrawable()
                .setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        txtCarrera = getView().findViewById(R.id.txtCarreraData);
        txtDireccion = getView().findViewById(R.id.txtDireccionData);
        txtLegajo = getView().findViewById(R.id.txtLegajoData);
        txtLibreta = getView().findViewById(R.id.txtLibretaData);
        txtUltAct = getView().findViewById(R.id.txtUltActData);
        txtMail = getView().findViewById(R.id.txtMailData);
        txtNombre = getView().findViewById(R.id.txtNombreData);
        txtTelefono = getView().findViewById(R.id.txtTelefonoData);
        txtAnioCur = getView().findViewById(R.id.txtAnioCurData);
        txtDebeFinales = getView().findViewById(R.id.txtDebeFinalesData);
        txtEsRegular = getView().findViewById(R.id.txtEsRegularData);
        txtFechaIng = getView().findViewById(R.id.txtFechaIngData);
        txtPaisNacim = getView().findViewById(R.id.txtPaisNacimData);
        txtProvNacim = getView().findViewById(R.id.txtProvNacimData);
        txtLocNacim = getView().findViewById(R.id.txtLocNacimData);
        txtNumDoc = getView().findViewById(R.id.txtNumDocData);
        txtMateriasCur = getView().findViewById(R.id.txtMateriasCurData);
        txtMateriasAprob = getView().findViewById(R.id.txtMateriasAprobData);
        txtFechaNac = getView().findViewById(R.id.txtFechaNacData);

        txtCarrera.setText(txtCarrera.getText() +IniciarSesionFragment.user.getCARRERA());
        txtDireccion.setText(txtDireccion.getText() +IniciarSesionFragment.user.getDIRECC());
        txtLegajo.setText(txtLegajo.getText() +"" +IniciarSesionFragment.user.getCODIGO());
        txtLibreta.setText(txtLibreta.getText() +IniciarSesionFragment.user.getLIBRETA());
        txtUltAct.setText(txtUltAct.getText() + AppUtils.formatFecha(IniciarSesionFragment.user.getFECHAPOR()));
        txtMail.setText(txtMail.getText() +IniciarSesionFragment.user.getMAIL());
        txtNombre.setText(txtNombre.getText() +IniciarSesionFragment.user.getNOMBRE());
        txtTelefono.setText(txtTelefono.getText() +IniciarSesionFragment.user.getTELEFONO());
        txtAnioCur.setText(txtAnioCur.getText() +IniciarSesionFragment.user.getGRADOSAL());
        txtDebeFinales.setText(Integer.parseInt(IniciarSesionFragment.user.getDEBEFINAL()) == 1 ? "El alumno debe finales" : "El alumno NO debe finales");
        txtEsRegular.setText(Integer.parseInt(IniciarSesionFragment.user.getESREGULAR()) == 1 ? "El alumno es regular" : "El alumno NO es regular");
        txtFechaIng.setText(txtFechaIng.getText() +AppUtils.formatFecha(IniciarSesionFragment.user.getFINGRE()));
        txtPaisNacim.setText(txtPaisNacim.getText() +IniciarSesionFragment.user.getPAISNACIM());
        txtProvNacim.setText(txtProvNacim.getText() +IniciarSesionFragment.user.getPROVNACIM());
        txtLocNacim.setText(txtLocNacim.getText() +IniciarSesionFragment.user.getLOCNACIM());
        txtNumDoc.setText(txtNumDoc.getText() +" " +IniciarSesionFragment.user.getTIPDOC() +" " +IniciarSesionFragment.user.getNUMDOC());
        txtMateriasCur.setText(txtMateriasCur.getText() +IniciarSesionFragment.user.getMATCURSA());
        txtMateriasAprob.setText(txtMateriasAprob.getText() +IniciarSesionFragment.user.getAPROBADAS());
        txtFechaNac.setText(txtFechaNac.getText() +AppUtils.formatFecha(IniciarSesionFragment.user.getFNACIM()));

        prgrssBrDatos.setVisibility(View.INVISIBLE);
    }

}
