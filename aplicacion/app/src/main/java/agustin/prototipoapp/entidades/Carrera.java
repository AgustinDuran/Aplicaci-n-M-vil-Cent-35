package agustin.prototipoapp.entidades;

/**
 * Clase POJO que va a simular el objeto de una Carrera
 * dónde se van a cargar los datos con el JSON
 * para luego utilizarla en el apartado de Ingresantes
 *
 * Created by agustin on 24/12/17.
 */
public class Carrera {

    // Nombre de la carrera: Ejemplo Desarrollo de Software
    private String nombreCarrera;

    // Descripción escueta de cual es la función de un profesional egresado de dicha carrera
    private String descripcionCarrera;

    // Dirección web de la carrera respecto a la página del CENT 35
    private String urlCarrera;

    /*
     * Sólo se van a necesitar los getters, para devolver la información.
     * la libreria de Gson se va a encargar de hacer el trabajo de parseo
     * de objeto json a objeto java de Carrera
     */

    public String getNombreCarrera() {
        return nombreCarrera;
    }

    public String getDescripcionCarrera() {
        return descripcionCarrera;
    }

    public String getUrlCarrera() {
        return urlCarrera;
    }
}
