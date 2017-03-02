package com.taxiconductor.RetrofitPetition;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by carlos on 23/02/17.
 */

public interface APIService {

    @FormUrlEncoded
    @POST("services/update_driver_location.php  ")
    Call<MSG> updateCoordenadas(@Field("ID_CHOFER") int idchofer,
                                  @Field("LATITUD") double latitud,
                                  @Field("LONGITUD") double longitud);

    @FormUrlEncoded
    @POST("services/update_driver_estatus.php")
    Call<MSG> updateStatus(@Field("ID_CHOFER") int idchofer,
                                @Field("ESTATUS") int estado);

    @FormUrlEncoded
    @POST("services/insert_driver_location.php")
    Call<MSG> insertLocation(@Field("ID_CHOFER") int idchofer,
                             @Field("LATITUD") double latitud,
                             @Field("LONGITUD") double longitud,
                             @Field("ESTATUS") int estatus);

    @FormUrlEncoded
    @POST("services/delete_solicitud.php")
    Call<MSG> deleteSolicitud(@Field("ID_CHOFER") int idchofer);

    @GET("obtener_choferes_por_user.php")
    Call<getDriverCredential> getLogin(@Query("USUARIO") String usuario);

    @FormUrlEncoded
    @POST("services/delete_location.php")
    Call<MSG> deleteLocation(@Field("ID_CHOFER") int id);
}
