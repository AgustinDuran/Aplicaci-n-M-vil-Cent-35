package agustin.prototipoapp.entidades;

/**
 * Clase POJO que va a simular el objeto de una Fecha
 * dónde se van a cargar los datos con el JSON
 * para luego utilizarla en el calendario
 */

public class FechaEvento {

    //Byte desde -128 a 127
    //Short desde -32768 a 32767

    /*
     * dia, mes y año representan la fecha en que va a ocurrir dicho evento
     * Ejemplo: ausencia de clases.
     * diaFin, mesFin, añoFin es en caso de que sea un evento que dure más de un día
     * Ejemplo: curso introductorio de inicio de año
     *
     * la descripcion es una información escueta de lo que representa dicho evento.
     */

    // Se utilizan datos de tipo byte y short para ahorrar espacio en memoria

    private byte dia;
    private byte mes;
    private short año;

    private byte diaFin;
    private byte mesFin;
    private short añoFin;

    private String descripcion;

    public FechaEvento() {}

    /*
     * Sólo se van a necesitar los getters, para devolver la información.
     * la libreria de Gson se va a encargar de hacer el trabajo de parseo
     * de objeto json a objeto java de FechaEvento
     */

    public byte getDia() {
        return dia;
    }

    public byte getMes() {
        return mes;
    }

    public short getAño() {
        return año;
    }

    public byte getDiaFin() {
        return diaFin;
    }

    public byte getMesFin() {
        return mesFin;
    }

    public short getAñoFin() {
        return añoFin;
    }

    public String getDescripcion() { return descripcion; }
}
