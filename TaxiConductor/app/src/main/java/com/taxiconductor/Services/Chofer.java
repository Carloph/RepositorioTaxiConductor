package com.taxiconductor.Services;

/**
 * Created by Sandoval on 23/02/2017.
 */

public class Chofer {
    private String ID_CHOFER;
    private String NOMBRE;
    private String APELLIDOS;
    private String EMAIL;
    private String CONTRASENIA;
    private String NUMERO_CELULAR;
    private String NUMERO_TAXI;
    private String PLACAS_TAXI;

    public Chofer(String ID_CHOFER, String NOMBRE, String APELLIDOS, String EMAIL, String CONTRASENIA, String NUMERO_CELULAR, String NUMERO_TAXI, String PLACAS_TAXI) {
        this.ID_CHOFER = ID_CHOFER;
        this.NOMBRE = NOMBRE;
        this.APELLIDOS = APELLIDOS;
        this.EMAIL = EMAIL;
        this.CONTRASENIA = CONTRASENIA;
        this.NUMERO_CELULAR = NUMERO_CELULAR;
        this.NUMERO_TAXI = NUMERO_TAXI;
        this.PLACAS_TAXI = PLACAS_TAXI;
    }

    public String getID_CHOFER() {
        return ID_CHOFER;
    }

    public void setID_CHOFER(String ID_CHOFER) {
        this.ID_CHOFER = ID_CHOFER;
    }

    public String getNOMBRE() {
        return NOMBRE;
    }

    public void setNOMBRE(String NOMBRE) {
        this.NOMBRE = NOMBRE;
    }

    public String getAPELLIDOS() {
        return APELLIDOS;
    }

    public void setAPELLIDOS(String APELLIDOS) {
        this.APELLIDOS = APELLIDOS;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getCONTRASENIA() {
        return CONTRASENIA;
    }

    public void setCONTRASENIA(String CONTRASENIA) {
        this.CONTRASENIA = CONTRASENIA;
    }

    public String getNUMERO_CELULAR() {
        return NUMERO_CELULAR;
    }

    public void setNUMERO_CELULAR(String NUMERO_CELULAR) {
        this.NUMERO_CELULAR = NUMERO_CELULAR;
    }

    public String getNUMERO_TAXI() {
        return NUMERO_TAXI;
    }

    public void setNUMERO_TAXI(String NUMERO_TAXI) {
        this.NUMERO_TAXI = NUMERO_TAXI;
    }

    public String getPLACAS_TAXI() {
        return PLACAS_TAXI;
    }

    public void setPLACAS_TAXI(String PLACAS_TAXI) {
        this.PLACAS_TAXI = PLACAS_TAXI;
    }
}
