package agustin.prototipoapp.entidades;

public class RespuestaInscMaterias {

    String mensaje;
    boolean flag;

    public boolean isSuccess() {
        return flag;
    }

    public void setSuccess(boolean success) {
        this.flag = success;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
