package agustin.prototipoapp.entidades;

public class Alumno {

    // nombres de las columnas que figuran en la base de datos
    private int legajo;
    private String CARRERA, TITULOSEC, LIBRETA, FLIBRETA, NOMBRE, SEXO, DIRECC, TELEFONO, FINGRE,
            FEGRE, FNACIM, LOCNACIM, PROVNACIM, PAISNACIM, TIPDOC, NUMDOC, MAIL, MAILMALO, CONTACTO,
            OBSERV, FECHAPOR, FECHAFICHA, PROCESAR, MODIPOR, GRADOSAL, AUXILIAR, DEBEFINAL, ESREGULAR, ACTIVO,
            APROBADAS, IESTECOK, MATCURSA;

    private String pass;

    // begin Probar singleton
    private static Alumno alumno;

    // Constructor privado
    private Alumno(int legajo, String pass){
        this.legajo = legajo;
        this.pass = pass;
    }

    public static Alumno getSingletonInstance(int legajo, String pass){
        alumno = new Alumno(legajo, pass);
        return alumno;
    }

    // end Probar singleton


    public int getCODIGO() {
        return legajo;
    }

    public void setCODIGO(int CODIGO) {
        this.legajo = CODIGO;
    }

    public String getCARRERA() {
        return CARRERA;
    }

    public void setCARRERA(String CARRERA) {
        this.CARRERA = CARRERA;
    }

    public String getTITULOSEC() {
        return TITULOSEC;
    }

    public String getPass() {
        return pass;
    }

    public void setTITULOSEC(String TITULOSEC) {
        this.TITULOSEC = TITULOSEC;
    }

    public String getLIBRETA() {
        return LIBRETA;
    }

    public void setLIBRETA(String LIBRETA) {
        this.LIBRETA = LIBRETA;
    }

    public String getFLIBRETA() {
        return FLIBRETA;
    }

    public void setFLIBRETA(String FLIBRETA) {
        this.FLIBRETA = FLIBRETA;
    }

    public String getNOMBRE() {
        return NOMBRE;
    }

    public void setNOMBRE(String NOMBRE) {
        this.NOMBRE = NOMBRE;
    }

    public String getSEXO() {
        return SEXO;
    }

    public void setSEXO(String SEXO) {
        this.SEXO = SEXO;
    }

    public String getDIRECC() {
        return DIRECC;
    }

    public void setDIRECC(String DIRECC) {
        this.DIRECC = DIRECC;
    }

    public String getTELEFONO() {
        return TELEFONO;
    }

    public void setTELEFONO(String TELEFONO) {
        this.TELEFONO = TELEFONO;
    }

    public String getFINGRE() {
        return FINGRE;
    }

    public void setFINGRE(String FINGRE) {
        this.FINGRE = FINGRE;
    }

    public String getFEGRE() {
        return FEGRE;
    }

    public void setFEGRE(String FEGRE) {
        this.FEGRE = FEGRE;
    }

    public String getFNACIM() {
        return FNACIM;
    }

    public void setFNACIM(String FNACIM) {
        this.FNACIM = FNACIM;
    }

    public String getLOCNACIM() {
        return LOCNACIM;
    }

    public void setLOCNACIM(String LOCNACIM) {
        this.LOCNACIM = LOCNACIM.replace("  ","");
    }

    public String getPROVNACIM() {
        return PROVNACIM;
    }

    public void setPROVNACIM(String PROVNACIM) {
        this.PROVNACIM = PROVNACIM.replace("  ", "");
    }

    public String getPAISNACIM() {
        return PAISNACIM;
    }

    public void setPAISNACIM(String PAISNACIM) {
        this.PAISNACIM = PAISNACIM.replace(" ", "");
    }

    public String getTIPDOC() {
        return TIPDOC;
    }

    public void setTIPDOC(String TIPDOC) {
        this.TIPDOC = TIPDOC;
    }

    public String getOBSERV() {
        return OBSERV;
    }

    public void setOBSERV(String OBSERV) {
        this.OBSERV = OBSERV;
    }

    public String getNUMDOC() {
        return NUMDOC;
    }

    public void setNUMDOC(String NUMDOC) {
        this.NUMDOC = NUMDOC;
    }

    public String getMAIL() {
        return MAIL;
    }

    public void setMAIL(String MAIL) {
        this.MAIL = MAIL;
    }

    public String getMAILMALO() {
        return MAILMALO;
    }

    public void setMAILMALO(String MAILMALO) {
        this.MAILMALO = MAILMALO;
    }

    public String getCONTACTO() {
        return CONTACTO;
    }

    public void setCONTACTO(String CONTACTO) {
        this.CONTACTO = CONTACTO;
    }

    public String getFECHAPOR() {
        return FECHAPOR;
    }

    public void setFECHAPOR(String FECHAPOR) {
        this.FECHAPOR = FECHAPOR;
    }

    public String getFECHAFICHA() {
        return FECHAFICHA;
    }

    public void setFECHAFICHA(String FECHAFICHA) {
        this.FECHAFICHA = FECHAFICHA;
    }

    public String getPROCESAR() {
        return PROCESAR;
    }

    public void setPROCESAR(String PROCESAR) {
        this.PROCESAR = PROCESAR;
    }

    public String getMODIPOR() {
        return MODIPOR;
    }

    public void setMODIPOR(String MODIPOR) {
        this.MODIPOR = MODIPOR;
    }

    public String getGRADOSAL() {
        return GRADOSAL;
    }

    public void setGRADOSAL(String GRADOSAL) {
        this.GRADOSAL = GRADOSAL;
    }

    public String getAUXILIAR() {
        return AUXILIAR;
    }

    public void setAUXILIAR(String AUXILIAR) {
        this.AUXILIAR = AUXILIAR;
    }

    public String getDEBEFINAL() {
        return DEBEFINAL;
    }

    public void setDEBEFINAL(String DEBEFINAL) {
        this.DEBEFINAL = DEBEFINAL;
    }

    public String getESREGULAR() {
        return ESREGULAR;
    }

    public void setESREGULAR(String ESREGULAR) {
        this.ESREGULAR = ESREGULAR;
    }

    public String getACTIVO() {
        return ACTIVO;
    }

    public void setACTIVO(String ACTIVO) {
        this.ACTIVO = ACTIVO;
    }

    public String getAPROBADAS() {
        return APROBADAS;
    }

    public void setAPROBADAS(String APROBADAS) {
        this.APROBADAS = APROBADAS;
    }

    public String getIESTECOK() {
        return IESTECOK;
    }

    public void setIESTECOK(String IESTECOK) {
        this.IESTECOK = IESTECOK;
    }

    public String getMATCURSA() {
        return MATCURSA;
    }

    public void setMATCURSA(String MATCURSA) {
        this.MATCURSA = MATCURSA;
    }

}
