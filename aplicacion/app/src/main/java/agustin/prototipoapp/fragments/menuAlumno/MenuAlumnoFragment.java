package agustin.prototipoapp.fragments.menuAlumno;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import agustin.prototipoapp.R;
import agustin.prototipoapp.fragments.login.IniciarSesionFragment;
import agustin.prototipoapp.ventanas.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuAlumnoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu_alumno, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        CardView btnNota = getView().findViewById(R.id.btnNota);
        CardView btnCerrarSesion = getView().findViewById(R.id.btnCerrarSesion);
        CardView btnDatosAlumno = getView().findViewById(R.id.btnDatosAlumno);
        CardView btnMesas = getView().findViewById(R.id.btnMesas);
        CardView btnInscMaterias = getView().findViewById(R.id.btnInscMaterias);

        btnDatosAlumno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.menuAlumnoFlag = true;
                Fragment fragment = new PerfilAlumnoFragment();

                getActivity().getSupportFragmentManager().beginTransaction()
                        // Le aplica una animación que está guardada como archivos '.xml' en 'res/anim'
                        .setCustomAnimations(R.anim.deslizar_fragment_izquierda_entrada, R.anim.deslizar_fragment_izquierda_salida)
                        .replace(R.id.contenedor, fragment)
                        //.addToBackStack(null)
                        .commit();
            }
        });

        btnNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.menuAlumnoFlag = true;
                Fragment fragment = new NotasFragment();

                getActivity().getSupportFragmentManager().beginTransaction()
                        // Le aplica una animación que está guardada como archivos '.xml' en 'res/anim'
                        .setCustomAnimations(R.anim.deslizar_fragment_izquierda_entrada, R.anim.deslizar_fragment_izquierda_salida)
                        .replace(R.id.contenedor, fragment)
                        //.addToBackStack(null)
                        .commit();
            }
        });

        btnMesas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.menuAlumnoFlag = true;
                Fragment fragment = new InscFinalesFragment();

                getActivity().getSupportFragmentManager().beginTransaction()
                        // Le aplica una animación que está guardada como archivos '.xml' en 'res/anim'
                        .setCustomAnimations(R.anim.deslizar_fragment_izquierda_entrada, R.anim.deslizar_fragment_izquierda_salida)
                        .replace(R.id.contenedor, fragment)
                        //.addToBackStack(null)
                        .commit();
            }
        });

        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new IniciarSesionFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        // Le aplica una animación que está guardada como archivos '.xml' en 'res/anim'
                        .setCustomAnimations(R.anim.deslizar_fragment_izquierda_entrada, R.anim.deslizar_fragment_izquierda_salida)
                        .replace(R.id.contenedor, fragment)
                        //.addToBackStack(null)
                        .commit();
                IniciarSesionFragment.user = null;
                MainActivity.menuAlumnoFlag = false;
            }
        });

        btnInscMaterias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.menuAlumnoFlag = true;
                Fragment fragment = new InscMateriasFragment();

                getActivity().getSupportFragmentManager().beginTransaction()
                        // Le aplica una animación que está guardada como archivos '.xml' en 'res/anim'
                        .setCustomAnimations(R.anim.deslizar_fragment_izquierda_entrada, R.anim.deslizar_fragment_izquierda_salida)
                        .replace(R.id.contenedor, fragment)
                        //.addToBackStack(null)
                        .commit();
            }
        });

    }
}