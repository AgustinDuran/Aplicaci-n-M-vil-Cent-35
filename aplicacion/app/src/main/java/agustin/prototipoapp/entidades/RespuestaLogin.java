package agustin.prototipoapp.entidades;

public class RespuestaLogin {
    private boolean flag, cambioPass;
    private String mensaje;

    public boolean isDatosCorrectos() {
        return flag;
    }

    public void setDatosCorrectos(boolean datosCorrectos) {
        this.flag = datosCorrectos;
    }

    public boolean isCambioPass() {
        return cambioPass;
    }

    public void setCambioPass(boolean cambioPass) {
        this.cambioPass = cambioPass;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String motivo) {
        this.mensaje = motivo;
    }
}
