package com.taxiconductor.RetrofitPetition;

import com.google.gson.annotations.SerializedName;

/**
 * Created by carlos on 23/02/17.
 */

public class UpdateDriver {

    @SerializedName("ID_UBICACION")
    private int id;
    @SerializedName("ID_CHOFER")
    private int idchofer;
    @SerializedName("LATITUD")
    private double latitud;
    @SerializedName("LONGITUD")
    private double longitud;
    @SerializedName("ESTATUS")
    private int estatus;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdchofer() {
        return idchofer;
    }

    public void setIdchofer(int idchofer) {
        this.idchofer = idchofer;
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

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }
}
