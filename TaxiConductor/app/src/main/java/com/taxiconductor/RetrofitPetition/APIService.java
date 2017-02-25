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
    @POST("services/update_driver_location.php")
    Call<MSG> updateCoordenadas(@Field("ID_CHOFER") int idchofer,
                                  @Field("LATITUD") double latitud,
                                  @Field("LONGITUD") double longitud);

    @FormUrlEncoded
    @POST("services/update_driver_estatus.php")
    Call<MSG> updateStatus(@Field("ID_CHOFER") int idchofer,
                                @Field("ESTATUS") int estado);

    @FormUrlEncoded
    @POST("services/get_driver_credential.php")
    Call<MSG> getCredential(@Field("USUARIO") String usuario);

    @GET("obtener_choferes_por_user.php")
    Call<getDriverCredential> getLogin(@Query("USUARIO") String usuario);
}
