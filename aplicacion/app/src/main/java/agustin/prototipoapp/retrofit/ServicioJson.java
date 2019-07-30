package agustin.prototipoapp.retrofit;

import java.util.List;

import agustin.prototipoapp.entidades.Alumno;
import agustin.prototipoapp.entidades.Anuncio;
import agustin.prototipoapp.entidades.Ausencia;
import agustin.prototipoapp.entidades.Carrera;
import agustin.prototipoapp.entidades.FechaEvento;
import agustin.prototipoapp.entidades.FinalAnotado;
import agustin.prototipoapp.entidades.Materia;
import agustin.prototipoapp.entidades.Mesa;
import agustin.prototipoapp.entidades.Nota;
import agustin.prototipoapp.entidades.RespuestaCambioPassword;
import agustin.prototipoapp.entidades.RespuestaInscFinal;
import agustin.prototipoapp.entidades.RespuestaInscMaterias;
import agustin.prototipoapp.entidades.RespuestaLogin;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * https://www.youtube.com/watch?v=oIm8nBdtso4
 * http://square.github.io/retrofit/#download
 * https://www.youtube.com/watch?v=lc_r1-hU3DI
 *
 * Pasar Parametros
 * https://www.beeva.com/beeva-view/desarrollo/retrofit-una-libreria-para-desarrollo-android-y-java/
 * https://stackoverflow.com/questions/36730086/retrofit-2-url-query-parameter
 *
 * TODO Se puede agregar el campo pass en todos los GET para hacer más seguro el sistema y no se pueda ingresar
 * a los datos de un alumno con solo usar la url y el legajo a mano.
 */
public interface ServicioJson {

    // Dirección web dónde se encuentra el json en la nube
    final String URL_ANUNCIO = "prototipo3/Anuncio.json";
    final String URL_CARRERA = "prototipo3/CarreraJson.json";
    final String URL_FECHA_EVENTO = "prototipo3/FechaEventoJson.json";
    final String URL_NOTA = "prototipo3/notas.php";
    final String URL_MATERIA = "prototipo3/dbfMaterias.php";
    final String URL_ALUMNO = "prototipo3/alumnos.php";
    final String URL_MESA = "prototipo3/mesas.php";
    final String URL_AUSENCIAS = "prototipo3/jsonPruebaAusenciaDocente.json";
    final String URL_MATERIASINSC = "prototipo3/materiasPuedeCursar.php";
    final String URL_FINALANOTADO = "prototipo3/finalAnotadoOld.php";
    final String URL_INSCRIPCIONMATERIA = "prototipo3/ejemploInscripcionMateria2.json";
    final String URL_INSCRIPCIONFINAL = "prototipo3/ejemploInscripcionFinal1.json";
    // COMPROBACIÓN USUARIO ALUMNO
    final String URL_LOGIN_ALUMNO = "prototipo3/ejemploUsuarioLogin2.json";
    // CAMBIAR A NUEVA CONTRASEÑA
    final String URL_PASSWORD_NUEVA = "prototipo3/ejemploCambioPassword1.json";

    // SERVIDOR DE FACU
    final String URL_ANUNCIO_FACU = "centapp/public/info/anuncios";
    final String URL_FECHA_EVENTO_FACU = "centapp/public/info/fechas";
    final String URL_LOGIN_ALUMNO_FACU = "centapp/public/login-alumno";
    final String URL_PASSWORD_NUEVA_FACU = "centapp/public/alumno-changepass";
    final String URL_INSCRIPCIONMATERIA_FACU = "centapp/public/inscripcion/materias";
    final String URL_INSCRIPCIONFINAL_FACU = "centapp/public/inscripcion/examenes";
    final String URL_CARRERA_FACU = "centapp/public/info/carreras";
    final String URL_NOTA_FACU = "centapp/public/info/notas";
    final String URL_MATERIA_FACU = "centapp/public/info/materia";
    final String URL_ALUMNO_FACU = "centapp/public/info/alumno";
    final String URL_MESA_FACU = "centapp/public/info/mesas";
    final String URL_AUSENCIAS_FACU = "centapp/public/info/ausencias";
    final String URL_MATERIASINSC_FACU = "centapp/public/info/materias-disponibles";
    final String URL_FINALANOTADO_FACU = "centapp/public/info/finales";


    @GET(URL_ANUNCIO_FACU)
    Call<List<Anuncio>> getAnuncios();
    @GET(URL_CARRERA_FACU)
    Call<List<Carrera>> getCarreras();
    @GET(URL_FECHA_EVENTO_FACU)
    Call<List<FechaEvento>> getFechasEvento();
    @GET(URL_NOTA_FACU)
    Call<List<Nota>> getNota(@Query("legajo") int legajo);
    @GET(URL_MATERIA_FACU)
    Call<List<Materia>> getMateria(@Query("codigoMateria") String codigoMateria);
    @GET(URL_ALUMNO_FACU)
    Call<Alumno> getAlumno(@Query("legajo") int legajo);
    @GET(URL_MESA_FACU)
    Call<List<Mesa>> getMesa(@Query("legajo") int legajo,
                             @Query("anio") int anio,
                             @Query("turno") String turno);
    @GET(URL_AUSENCIAS_FACU)
    Call<List<Ausencia>> getAusencias();
    @GET(URL_MATERIASINSC_FACU)
    Call<List<Nota>> getMateriaInsc(@Query("legajo") int legajo);
    @GET(URL_FINALANOTADO_FACU)
    Call<List<FinalAnotado>> getFinalAnotado(@Query("legajo") int legajo);
    @GET(URL_LOGIN_ALUMNO_FACU)
    Call<RespuestaLogin> getRespuestaLogin(@Query("codigo") int legajo,
                                           @Query("password") String pass);
    @GET(URL_PASSWORD_NUEVA_FACU)
    Call<RespuestaCambioPassword> getRespuestaCambioPassword(@Query("codigo") int legajo,
                                                             @Query("nuevoPass") String newPass);
    @GET(URL_INSCRIPCIONMATERIA_FACU)
    Call<RespuestaInscMaterias> getRespuestaInscMaterias(@Query("codigo") int legajo);
    @GET(URL_INSCRIPCIONFINAL_FACU)
    Call<RespuestaInscFinal> getRespuestaInscFinal(@Query("codigo") int legajo,
                                                   @Query("materia") String materia,
                                                   @Query("fecha") String fecha);

    /*
    @GET(URL_ANUNCIO)
    Call<List<Anuncio>> getAnuncios();
    @GET(URL_CARRERA)
    Call<List<Carrera>> getCarreras();
    @GET(URL_FECHA_EVENTO)
    Call<List<FechaEvento>> getFechasEvento();
    @GET(URL_NOTA)
    Call<List<Nota>> getNota(@Query("legajo") int legajo);
    @GET(URL_MATERIA)
    Call<List<Materia>> getMateria(@Query("codigoMateria") String codigoMateria);
    @GET(URL_ALUMNO)
    Call<Alumno> getAlumno(@Query("legajo") int legajo);
    @GET(URL_MESA)
    Call<List<Mesa>> getMesa(@Query("legajo") int legajo,
                             @Query("anio") int anio,
                             @Query("turno") String turno);
    @GET(URL_AUSENCIAS)
    Call<List<Ausencia>> getAusencias();
    @GET(URL_MATERIASINSC)
    Call<List<Nota>> getMateriaInsc(@Query("legajo") int legajo);
    @GET(URL_FINALANOTADO)
    Call<List<FinalAnotado>> getFinalAnotado(@Query("legajo") int legajo);
    @GET(URL_LOGIN_ALUMNO)
    Call<RespuestaLogin> getRespuestaLogin(@Query("codigo") int legajo,
                                           @Query("password") String pass);
    @GET(URL_PASSWORD_NUEVA)
    Call<RespuestaCambioPassword> getRespuestaCambioPassword(@Query("codigo") int legajo,
                                                             @Query("nuevoPass") String newPass);
    @GET(URL_INSCRIPCIONMATERIA)
    Call<RespuestaInscMaterias> getRespuestaInscMaterias(@Query("codigo") int legajo);
    @GET(URL_INSCRIPCIONFINAL)
    Call<RespuestaInscFinal> getRespuestaInscFinal(@Query("codigo") int legajo,
                                                   @Query("materia") String materia,
                                                   @Query("fecha") String fecha);
    */
}
