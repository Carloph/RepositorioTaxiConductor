package com.taxiconductor.RetrofitPetition;

import com.google.gson.annotations.SerializedName;

/**
 * Created by carlos on 23/02/17.
 */

public class UpdateDriverStatus {

    @SerializedName("ID_CHOFER")
    private int idchofer;
    @SerializedName("ESTATUS")
    private int estatus;

    public UpdateDriverStatus(){

    }

    public UpdateDriverStatus(int idchofer, int estatus) {
        this.idchofer = idchofer;
        this.estatus = estatus;
    }

    public int getIdchofer() {
        return idchofer;
    }

    public void setIdchofer(int idchofer) {
        this.idchofer = idchofer;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }
}
