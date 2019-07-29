package agustin.prototipoapp.ventanas;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import agustin.prototipoapp.fragments.calendario.CalendarioFragment;
import agustin.prototipoapp.fragments.cartelera.ContenedorFragmentCartelera;
import agustin.prototipoapp.fragments.ingresantes.IngresantesFragment;
import agustin.prototipoapp.fragments.login.IniciarSesionFragment;
import agustin.prototipoapp.fragments.menuAlumno.MenuAlumnoFragment;
import agustin.prototipoapp.retrofit.ServicioJson;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import agustin.prototipoapp.R;
import agustin.prototipoapp.util.Config;
import agustin.prototipoapp.fragments.inicio.InicioFragment;
import agustin.prototipoapp.util.NotificationUtils;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Clase principal, ésta contiene la ventana donde iniciara la app, también tiene
 * la parte de funcionalidades (listeners) en los botones.
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /* Objeto del tipo fragment que se inicializa en nulo. Se declara como variable global
     * para mejorar rendimientos en la app y no crear objetos innecesarios */
    private Fragment fragment = null;

    // Necesario para correr firebase
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    // Url dónde se encuentran los web service
    // 192.168.43.7
    private final String URL_SERVICIO_WEB_AGUS = "http://192.168.43.7/";
    private final String URL_SERVICIO_WEB_FACU = "http://192.168.43.142/";

    // El fragment de inicio consulta si hay notificaciones nuevas
    public static boolean hayNotificacionesNuevas;

    private NavigationView navigationView;
    private DrawerLayout drawer;

    private static Retrofit retrofit;
    public static ServicioJson servicio;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* Crea la comunicación entre el retrofit y el web service  */
        Gson gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder()
                .baseUrl(URL_SERVICIO_WEB_FACU)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        servicio = retrofit.create(ServicioJson.class);

        // Comunicación firebase-app
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Checkea el tipo de filtro de intent. Si es la primera vez lo suscribe
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // Registro satisf. Suscribe al tópico global para recibir notificaciones
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // Nueva notificación push recibida
                    //TODO Cambiar, cada vez que recibe una notificación
                    String descripcion = intent.getStringExtra("descripcion");
                    Toast.makeText(getApplicationContext(), "Push notification: " + descripcion, Toast.LENGTH_LONG).show();
                    //txtMessage.setText(message);
                }
            }
        };

        // Navigation drawer
        drawer = findViewById(R.id.drawer_layout);

        // Listeners del botón que despliega el NavigationDrawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // NavigationView
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragment = new InicioFragment();
        abrirFragment(fragment, true, 0, R.id.itemInicio, getBaseContext().getString(R.string.titulo_inicio));
    }

    public static boolean menuAlumnoFlag = false;

    // Eventos de abrir y cerrar el navigation drawer (Botón atrás)
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if ( ( fragment.getClass().equals(MenuAlumnoFragment.class) || fragment.getClass().equals(IniciarSesionFragment.class)) && menuAlumnoFlag == true ) {
            fragment = new MenuAlumnoFragment();
            abrirFragment(fragment, true, 2, R.id.itemAlumnos, getBaseContext().getString(R.string.titulo_alumnos));
            menuAlumnoFlag = false;
        } else if (!fragment.getClass().equals(InicioFragment.class)) {
            fragment = new InicioFragment();
            abrirFragment(fragment, true, 0, R.id.itemInicio, getBaseContext().getString(R.string.titulo_inicio));
        } else {
            super.onBackPressed();
        }
    }

    public void abrirFragment(Fragment fragment, boolean aplicarAnimacion, int idMenu, int idItem, String titulo) {
        // Vuelve a inicio

        // No aplica la animación
        if (aplicarAnimacion == false) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contenedor, fragment)
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    // Le aplica una animación que está guardada como archivos '.xml' en 'res/anim'
                    .setCustomAnimations(R.anim.deslizar_fragment_izquierda_entrada, R.anim.deslizar_fragment_izquierda_salida)
                    .replace(R.id.contenedor, fragment)
                    .commit();
        }

        // Hace que esté en foco dicho Item del Menú
        navigationView.getMenu().getItem(idMenu).setChecked(true);
        // Cambia el nombre del título superior
        getSupportActionBar().setTitle(titulo);
        //idBuffer = R.id.itemInicio;
        idBuffer = idItem;
    }

    /* este idBuffer va a servir para que no tenga que cargar el mismo fragment si ya está
    * cargado actualmente, es decir, cargar dos veces el mismo fragment innecesariamente.
    * por defecto, el idBuffer va a contener el id del itemInicio, ya que, va a ser el primer
    * fragment que visualizaremos al abrir la aplicación */
    private int idBuffer;

    /**
     * Método que controla las acciones de cada botón
     * del NavigationView
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // ingresa el id perteneciente al item del navigation drawer
        int id = item.getItemId();

        /* Si no cambiamos de fragment, será false.
         Cuando cambie de fragment se volverá true. */
        boolean transicionDeFragment = false;

        /* Permuta de contenido en contenido
         Nota: To.do está metido en el mismo activity (contenedor.xml),
         lo único que hace, es cambiar su contenido con fragments */
        // Según el id se lo redirecciona a un fragment u otro
        switch (id) {

            case R.id.itemInicio:
                fragment = new InicioFragment();
                // Cambia el título del activity
                getSupportActionBar().setTitle(R.string.titulo_inicio);
                // Compara el idBuffer con el id actual
                if (idBuffer != id)
                    transicionDeFragment = true;
                // Le asigna su valor
                idBuffer = id;
                break;

            case R.id.itemIngresantes:
                fragment = new IngresantesFragment();
                if (idBuffer != id)
                    transicionDeFragment = true;
                idBuffer = id;
                getSupportActionBar().setTitle(R.string.titulo_ingresantes);
                break;

            case R.id.itemCalendario:
                fragment = new CalendarioFragment();
                if (idBuffer != id)
                    transicionDeFragment = true;
                idBuffer = id;
                getSupportActionBar().setTitle(R.string.titulo_calendario);
                break;

            case R.id.itemCartelera:
                fragment = new ContenedorFragmentCartelera();
                if (idBuffer != id)
                    transicionDeFragment = true;
                idBuffer = id;
                getSupportActionBar().setTitle(R.string.titulo_cartelera);
                break;

            case R.id.itemAlumnos:
                if (IniciarSesionFragment.user != null)
                    fragment = new MenuAlumnoFragment();
                else
                    fragment = new IniciarSesionFragment();

                if (idBuffer != id)
                    transicionDeFragment = true;
                idBuffer = id;
                getSupportActionBar().setTitle(R.string.titulo_alumnos);
                break;

            case R.id.itemWeb:
                /* Cuando el usuario clickea el botón de WEB. es redireccionado
                 * a través de su navegador de internet por defecto, a la página del
                 * Cent 35. Ésta funcionalidad se hace en el siguiente método */
                ingresarWeb("http://www.cent35.edu.ar");
                break;

            case R.id.itemFacebook:
                ingresarFacebook("367845673305549", "https://www.facebook.com/centtreintaycinco/");
                break;
        }

        // Evalua en el booleano, si hubo cambio de fragment
        if (transicionDeFragment == true) {
            menuAlumnoFlag = false;

            // cambia el contenido del activity, reemplazándolo por el del fragment
            getSupportFragmentManager().beginTransaction()
                    // Le aplica una animación que está guardada como archivos '.xml' en 'res/anim'
                    .setCustomAnimations(R.anim.deslizar_fragment_izquierda_entrada, R.anim.deslizar_fragment_izquierda_salida)
                    .replace(R.id.contenedor, fragment)
                    //.addToBackStack(null)
                    .commit();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Método que va a redireccionar al usuario mediante su explorador web por
     * defecto hacia la url que se pasa como parámetro.
     *
     * @see "www.developer.android.com/guide/components/intents-common.html?hl=es-419#Browser"
     * @param urlWeb
     */
    private void ingresarWeb(String urlWeb){
        /* Se instancia un intent en el que se pasa información
         * para abrir una página web. Se usa la acción ACTION_VIEW
         * y se especifica la URL web en los datos de la intent. */
        Intent intentWeb = new Intent(Intent.ACTION_VIEW, Uri.parse(urlWeb));
        startActivity(intentWeb);
    }

    /**
     * Método que va a redireccionar al usuario mediante la aplicación de Facebook
     * hacia la dirección en que se encuentra la página de la institución en Facebook.
     * En caso de no tener la aplicación se abrirá mediante el explorador por defecto.
     *
     * @see "https://es.stackoverflow.com/questions/68903/intent-para-abrir-perfil-de-con-la-app-de-facebook-android"
     * @see "https://findmyfbid.com/"
     * @param idFacebook Se necesita la id del grupo, ésta se puede conseguir en la pagina findmyfbid.com
     * @param urlWeb se utiliza por si, el usuario no tiene instalada la APP de facebook.
     */
    private void ingresarFacebook(String idFacebook, String urlWeb){
        // Se arma la url que direcciona mediante la app de facebook
        String urlFacebook = "fb://page/" +idFacebook;

        Intent intentFacebook;

        /*
         * Si el usuario no tiene la app de Facebook va a haber una excepción
         * la solución a este problema es que ingrese a la URL mediante el explorador por defecto.
         */
        try{
            intentFacebook = new Intent(Intent.ACTION_VIEW, Uri.parse(urlFacebook));
            startActivity(intentFacebook);
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, R.string.toast_no_app,
                    Toast.LENGTH_SHORT).show();
            // Aplico un sleep para que se pueda ver el Toast
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            intentFacebook = new Intent(Intent.ACTION_VIEW, Uri.parse(urlWeb));
            startActivity(intentFacebook);
        }
    }

    /**
     * Inserta un 'badge' en el ItemMenu 'Cartelera' del NavigationDrawer. El número va a ser
     * el que se pase como parámetro
     * @param num
     */
    public void insertarNumeroDeNotificacion(int num){

        // String que va a aparecer por defecto
        final String original = "Cartelera";

        // Obtenemos el elemento del MenuItem ('cartelera')
        Menu menuNav = navigationView.getMenu();
        MenuItem element = menuNav.findItem(R.id.itemCartelera);

        // Si el número es 0, entonces se deja el texto original
        if (num == 0) {
            //Para el estado de 0 notificaciones
            element.setTitle(original);
            // Aviso que no hay nuevas notificaciones
            hayNotificacionesNuevas = false;
        } else {
            //hayNotificacionesNuevas.setText(R.string.inicio_notificaciones);
            String badge = Integer.toString(num);
            String s = original +"   " +badge +" ";

            // El badge va a ser, simplemente, un String con un estilo distinto
            // de fondo y remarcado. Haciendo parecer que es un badge real.
            SpannableString sColored = new SpannableString( s );

            // Establece colores de fondo y de letra
            sColored.setSpan(new BackgroundColorSpan( Color.BLUE ), s.length()-3, s.length(), 0);
            sColored.setSpan(new ForegroundColorSpan( Color.WHITE ), s.length()-3, s.length(), 0);

            // Lo inserta en el título de menú
            element.setTitle(sColored);
            // Aviso que hay nuevas notificaciones
            hayNotificacionesNuevas = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

}