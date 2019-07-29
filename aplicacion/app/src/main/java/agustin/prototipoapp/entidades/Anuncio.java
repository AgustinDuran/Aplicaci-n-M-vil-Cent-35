package agustin.prototipoapp.entidades;

/**
 * Clase POJO que va a simular el objeto de un Anuncio
 * dónde se van a cargar los datos con el JSON
 * para luego utilizarla en la cartelera
 *
 * Created by agustin on 29/12/17.
 */

public class Anuncio {

    // Datos
    private String asunto, descripcion, fecha;

    /* *** GETTERS *** */

    /*
     * Sólo se van a necesitar los getters, para devolver la información.
     * la libreria de Gson se va a encargar de hacer el trabajo de parseo
     * de objeto json a objeto java de Anuncio
     */

    public String getAsunto() {
        return asunto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getFecha() {
        return fecha;
    }

}
