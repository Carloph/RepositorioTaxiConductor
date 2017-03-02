package com.taxiconductor.RetrofitPetition;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sandoval on 25/02/2017.
 */

public class Servicio {
    @SerializedName("LATITUD_CLIENTE")
    private double LATITUD_CLIENTE;
    @SerializedName("LONGITUD_CLIENTE")
    private double LONGITUD_CLIENTE;
    @SerializedName("LATITUD_DESTINO")
    private double LATITUD_DESTINO;
    @SerializedName("LONGITUD_DESTINO")
    private double LONGITUD_DESTINO;

    public double getLATITUD_CLIENTE() {
        return LATITUD_CLIENTE;
    }

    public void setLATITUD_CLIENTE(double LATITUD_CLIENTE) {
        this.LATITUD_CLIENTE = LATITUD_CLIENTE;
    }

    public double getLONGITUD_CLIENTE() {
        return LONGITUD_CLIENTE;
    }

    public void setLONGITUD_CLIENTE(double LONGITUD_CLIENTE) {
        this.LONGITUD_CLIENTE = LONGITUD_CLIENTE;
    }

    public double getLATITUD_DESTINO() {
        return LATITUD_DESTINO;
    }

    public void setLATITUD_DESTINO(double LATITUD_DESTINO) {
        this.LATITUD_DESTINO = LATITUD_DESTINO;
    }

    public double getLONGITUD_DESTINO() {
        return LONGITUD_DESTINO;
    }

    public void setLONGITUD_DESTINO(double LONGITUD_DESTINO) {
        this.LONGITUD_DESTINO = LONGITUD_DESTINO;
    }
}
