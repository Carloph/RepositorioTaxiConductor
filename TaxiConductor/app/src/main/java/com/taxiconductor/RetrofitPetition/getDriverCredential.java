package com.taxiconductor.RetrofitPetition;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sandoval on 24/02/2017.
 */

public class getDriverCredential {
    @SerializedName("ID_CHOFER")
    int ID_CHOFER;
    @SerializedName("CONTRASENIA")
    String CONTRASENIA;

    public int getID_CHOFER() {
        return ID_CHOFER;
    }

    public void setID_CHOFER(int ID_CHOFER) {
        this.ID_CHOFER = ID_CHOFER;
    }

    public String getCONTRASENIA() {
        return CONTRASENIA;
    }

    public void setCONTRASENIA(String CONTRASENIA) {
        this.CONTRASENIA = CONTRASENIA;
    }
}
