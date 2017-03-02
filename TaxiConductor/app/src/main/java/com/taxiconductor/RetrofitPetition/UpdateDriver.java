package com.taxiconductor.RetrofitPetition;

import com.google.gson.annotations.SerializedName;

/**
 * Created by carlos on 23/02/17.
 */

public class UpdateDriver {

    @SerializedName("ID_CHOFER")
    private int idchofer;
    @SerializedName("LATITUD")
    private double latitud;
    @SerializedName("LONGITUD")
    private double longitud;

    public UpdateDriver(){

    }

    public UpdateDriver(int idchofer, double latitud, double longitud) {
        this.idchofer = idchofer;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public int getIdusario() {
        return idchofer;
    }

    public void setIdusario(int idusario) {
        this.idchofer = idusario;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
}
