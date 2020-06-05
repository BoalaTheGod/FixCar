package com.boala.fixcar;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
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
    Call<Integer> postVehicle(
            @Field("idusuario") String idusuario,
            @Field("km_vehiculo") String km_vehiculo,
            @Field("modelo") String modelo,
            @Field("marca") String marca,
            @Field("motor") String motor,
            @Field("matricula") String matricula
    );

    @PUT("vehiculoput{idvehiculo}")
    Call<Boolean> putVehicle(
            @Path("idvehiculo") int id,
            @Query("km_vehiculo") String km_vehiculo,
            @Query("modelo") String modelo,
            @Query("marca") String marca,
            @Query("motor") String motor,
            @Query("matricula") String matricula
    );

    @PUT("reminder{idvehiculo}")
    Call<Boolean> reminderPut(
            @Path("idvehiculo") int id,
            @Query("itv_fecha") String itv_fecha,
            @Query("itv_note") String itv_note,
            @Query("fecha_ruedas") String fecha_ruedas,
            @Query("wheels_note") String wheels_note,
            @Query("fecha_aceite") String fecha_aceite,
            @Query("oil_note") String oil_note,
            @Query("fecha_revision") String fecha_revision,
            @Query("review_note") String review_note,
            @Query("seguro") String seguro,
            @Query("vehicle_note") String vehicle_note
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
            @Query("fecha") String fecha,
            @Query("estado") String estado

    );

     @GET("taller")
    Call<List<WorkShop>> getTalleres();

    @GET("idtaller{idtaller}")
    Call<WorkShop> getTaller(@Path("idtaller") int id);

    @Multipart
    @POST("vehiclepic{id}")
    Call<Void> uploadVehImage(@Path("id") int id, @Part("image\"; filename=\"myfile.jpg\" ") RequestBody file);
    @Multipart
    @POST("userpic{id}")
    Call<Void> uploadUserImage(@Path("id") int id, @Part("image\"; filename=\"myfile.jpg\" ") RequestBody file);

    @GET("documentidusuario{id}")
    Call<List<DocumentFixCar>> getDocuments(@Path("id") int id);

    @FormUrlEncoded
    @POST("documentpost")
    Call<Integer> postDocument(
            @Field("type_document") String type_document,
            @Field("notes") String notes,
            @Field("iduser") String iduser,
            @Field("idvehicle") String idvehicle
    );
    @Multipart
    @POST("documentpic{id}")
    Call<Void> uploadDocImage(@Path("id") int id, @Part("image\"; filename=\"myfile.jpg\" ") RequestBody file);
    @DELETE("documentdel{id}")
    Call<Boolean> delDocument(
            @Path("id") int id
    );

}
