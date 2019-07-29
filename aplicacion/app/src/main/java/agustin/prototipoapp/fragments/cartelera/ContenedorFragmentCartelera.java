package agustin.prototipoapp.fragments.cartelera;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import agustin.prototipoapp.R;
import agustin.prototipoapp.adapters.AdapterSeccion;
import agustin.prototipoapp.util.AppUtils;

/**
 * Como crear tabs
 * https://www.youtube.com/watch?v=FE_3cqoLEPA
 * https://www.youtube.com/watch?v=orFWvZowdNE
 */
public class ContenedorFragmentCartelera extends Fragment {

    // Pestañas y tab
    private AppBarLayout appBar;
    private TabLayout pestanias;
    private ViewPager viewPager;
    View vista;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_contenedor_fragment_cartelera, container, false);
        // Views de los tabs
        if (AppUtils.rotacion==0) {
            View parent = (View) container.getParent();
            if (appBar == null) {
                appBar = parent.findViewById(R.id.appBar);
                pestanias = new TabLayout(getActivity());
                pestanias.setTabTextColors(Color.parseColor("#000000"),Color.parseColor("#FFFFFF"));
                appBar.addView(pestanias);
                viewPager = vista.findViewById(R.id.viewPagerPestanias);
                llenarViewPager(viewPager);
                viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                    }
                });
                pestanias.setupWithViewPager(viewPager);
            }
        } else {
            AppUtils.rotacion = 1;
        }

        return vista;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (AppUtils.rotacion==0) {
            appBar.removeView(pestanias);
        }
    }

    private void llenarViewPager(ViewPager viewPager) {
        AdapterSeccion as = new AdapterSeccion(getFragmentManager());

        as.addFragment(new CarteleraFragment(), "Avisos");
        as.addFragment(new AusenciasFragment(), "Ausencia docente");

        viewPager.setAdapter(as);
    }

}
