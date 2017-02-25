package com.taxiconductor.RetrofitPetition;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by carlos on 23/02/17.
 */

public interface APIService {

    @FormUrlEncoded
    @POST("services/update_driver_location.php")
    Call<MSG> updateCoordenadas(@Field("ID_CHOFER") int idchofer,
                                  @Field("LATITUD") double latitud,
                                  @Field("LONGITUD") double longitud);

    @FormUrlEncoded
    @POST("services/update_driver_estatus.php")
    Call<MSG> updateStatus(@Field("ID_CHOFER") int idchofer,
                                @Field("ESTATUS") int estado);



}
