package com.boala.fixcar;

import java.util.Date;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

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
    @FormUrlEncoded
    @PUT("vehiculoput{idvehiculo}")
    Call<Boolean> putVehicle(
            @Path("idvehiculo") int id,
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
            @Body Vehiculo vehiculo
    );

}
