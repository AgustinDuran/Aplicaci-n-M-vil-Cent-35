package agustin.prototipoapp.entidades;

import android.graphics.Color;

public class RespuestaNota {
    private String infor;
    private int colorTexto;

    public RespuestaNota(String infor, int colorTexto) {
        this.infor = infor;
        this.colorTexto = colorTexto;
    }

    public String getInfor() {
        return infor;
    }

    public void setInfor(String infor) {
        this.infor = infor;
    }

    public int getColorTexto() {
        return colorTexto;
    }

    public void setColorTexto(int colorTexto) {
        this.colorTexto = colorTexto;
    }
}
