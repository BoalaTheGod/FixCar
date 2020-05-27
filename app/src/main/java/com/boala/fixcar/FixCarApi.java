package com.boala.fixcar;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FixCarApi {
    @GET("usuario")
    Call<List<Usuario>> getUsers();

    @GET("idusuario{idusuario}")
    Call<Usuario> getUser(@Path("idusuario") int id);

    @GET("vehiculo")
    Call<List<VehiculoExpandable>> getVehicles();

    @GET("idvehiculo{idvehiculo}")
    Call<VehiculoExpandable> getVehicle(@Path("idvehiculo") int id);

    @POST("vehiculopost")
    Call<Vehiculo> postVehicle(@Body Vehiculo vehiculo);

    @GET("vehiculoidusuario{uid}")
    Call<List<VehiculoExpandable>> getVehiclesUID(@Path("uid") int id);

    @FormUrlEncoded
    @POST("vehiculopost")
    Call<Boolean> postVehicle(
            @Field("idusuario") String idusuario,
            @Field("km_vehiculo") String km_vehiculo,
            @Field("itv_fecha") String itv_fecha,
            @Field("fecha_ruedas") String fecha_ruedas,
            @Field("fecha_aceite") String fecha_aceite,
            @Field("fecha_revision") String fecha_revision,
            @Field("modelo") String modelo,
            @Field("marca") String marca,
            @Field("motor") String motor,
            @Field("seguro") String seguro,
            @Field("color") String color,
            @Field("matricula") String matricula,
            @Field("imagen") String imagen
    );

    @PUT("vehiculoput{idvehiculo}")
    Call<Boolean> putVehicle(
            @Path("idvehiculo") int id,
            @Query("idusuario") String idusuario,
            @Query("km_vehiculo") String km_vehiculo,
            @Query("itv_fecha") String itv_fecha,
            @Query("fecha_ruedas") String fecha_ruedas,
            @Query("fecha_aceite") String fecha_aceite,
            @Query("fecha_revision") String fecha_revision,
            @Query("modelo") String modelo,
            @Query("marca") String marca,
            @Query("motor") String motor,
            @Query("seguro") String seguro,
            @Query("color") String color,
            @Query("matricula") String matricula,
            @Query("imagen") String imagen
    );

    @DELETE("vehiculodel{idvehiculo}")
    Call<Boolean> delVehicle(
            @Path("idvehiculo") int id
    );

    @FormUrlEncoded
    @POST("usuariopost")
    Call<Boolean> postUser(
            @Field("nombre") String nombre,
            @Field("direccion") String direccion,
            @Field("localidad") String localidad,
            @Field("telefono") String telefono,
            @Field("email") String email,
            @Field("fecha") String fecha,
            @Field("password") String password
    );

    @GET("idmailusuario")
    Call<Integer> getUserByMail(@Query("email") String email);

    @PUT("usuarioput{id}")
    Call<Boolean> putUser(
            @Path("id") int id,
            @Query("idusuario") String idusuario,
            @Query("nombre") String nombre,
            @Query("direccion") String direccion,
            @Query("localidad") String localidad,
            @Query("telefono") String telefono,
            @Query("email") String email,
            @Query("fecha") String fecha

    );

     @GET("taller")
    Call<List<WorkShop>> getTalleres();

    @GET("idtaller{idtaller}")
    Call<WorkShop> getTaller(@Path("idtaller") int id);

}
