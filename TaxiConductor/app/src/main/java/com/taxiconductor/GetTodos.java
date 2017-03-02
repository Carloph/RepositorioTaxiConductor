package com.taxiconductor;

import com.taxiconductor.RetrofitPetition.Servicio;
import com.taxiconductor.RetrofitPetition.getDriverCredential;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Sandoval on 25/02/2017.
 */

public interface GetTodos {
    @GET("obtener_choferes_por_user.php")
    Call<getDriverCredential> getLogin(@Query("USUARIO") String usuario);

    @GET("obtener_solicitudes_por_id.php")
    Call<Servicio> escucha(@Query("ID_CHOFER") int id);
}
