package com.boala.fixcar;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public class UserTest extends AppCompatActivity {
/**Clase para probar funciones, su contenido es irrelevante**/
   /** TextView tv;

    FixCarApi fixCarApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_test);

        tv = findViewById(R.id.testView);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new
                OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request.Builder builder = originalRequest.newBuilder().header("Authorization", Credentials.basic("Cesur","FixCar"));

                Request newRequest = builder.build();
                return chain.proceed(newRequest);
            }
        }).addInterceptor(loggingInterceptor).build();
        Gson gson = new GsonBuilder().setLenient().serializeNulls().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://fixcarcesur.herokuapp.com/model/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
        fixCarApi = retrofit.create(FixCarApi.class);

        delVehicle();

    }
    private void getUsers(){
        Call<List<Usuario>> call = fixCarApi.getUsers();
        call.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, retrofit2.Response<List<Usuario>> response) {
                if (!response.isSuccessful()){
                    tv.setText("Code: "+response.code());
                    return;
                }

                List<Usuario> usuarios = response.body();

                for (Usuario usuario : usuarios){
                    String text = "ID: "+usuario.getIdusuario()+"\n";
                    text += "Nombre: "+usuario.getName()+"\n";
                    text += "Password: "+usuario.getPassword()+"\n";
                    text += "Direccion: "+usuario.getAdress()+"\n";
                    text += "Localidad: "+usuario.getCity()+"\n";
                    text += "Telefono: "+usuario.getPhoneNumber()+"\n";
                    text += "Email: "+usuario.getEmail()+"\n";
                    tv.append(text);
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                tv.setText(t.getMessage());
            }
        });
    }
    private void getUser(int userId){
        Call<Usuario> call = fixCarApi.getUser(userId);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, retrofit2.Response<Usuario> response) {
                if (!response.isSuccessful()){
                    tv.setText("Code: "+response.code());
                    return;
                }

                Usuario usuario = response.body();
                String text = "ID: "+usuario.getIdusuario()+"\n";
                text += "Nombre: "+usuario.getName()+"\n";
                text += "Password: "+usuario.getPassword()+"\n";
                text += "Direccion: "+usuario.getAdress()+"\n";
                text += "Localidad: "+usuario.getCity()+"\n";
                text += "Telefono: "+usuario.getPhoneNumber()+"\n";
                text += "Email: "+usuario.getEmail()+"\n";
                tv.append(text);
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                tv.setText(t.getMessage());
            }
        });
    }
    public void getVehicles(){
        Call<List<VehiculoExpandable>> call = fixCarApi.getVehicles();
        call.enqueue(new Callback<List<VehiculoExpandable>>() {
            @Override
            public void onResponse(Call<List<VehiculoExpandable>> call, retrofit2.Response<List<VehiculoExpandable>> response) {
                if (!response.isSuccessful()){
                    tv.setText("Code: "+response.code());
                }

                List<VehiculoExpandable> vehiculos = response.body();

                for (VehiculoExpandable vehiculo : vehiculos){
                    String text = "ID: "+vehiculo.getIdVehiculo()+"\n";
                    text += "IdUsuario: "+vehiculo.getIdUsuario()+"\n";
                    text += "Marca: "+vehiculo.getBrand()+"\n";
                    text += "Modelo: "+vehiculo.getModel()+"\n";
                    text += "ITV: "+vehiculo.getItvDate()+"\n";
                    text += "expanded: "+vehiculo.isExpanded()+"\n";
                    tv.append(text);
                }
            }

            @Override
            public void onFailure(Call<List<VehiculoExpandable>> call, Throwable t) {
                tv.setText(t.getMessage());
            }
        });
    }
    private void getVehicle(int vehId){
        Call<VehiculoExpandable> call = fixCarApi.getVehicle(vehId);
        call.enqueue(new Callback<VehiculoExpandable>() {
            @Override
            public void onResponse(Call<VehiculoExpandable> call, retrofit2.Response<VehiculoExpandable> response) {
                if (!response.isSuccessful()){
                    tv.setText("Code: "+response.code());
                    return;
                }

                VehiculoExpandable vehiculo = response.body();
                String text = "ID: "+vehiculo.getIdVehiculo()+"\n";
                text += "IdUsuario: "+vehiculo.getIdUsuario()+"\n";
                text += "Marca: "+vehiculo.getBrand()+"\n";
                text += "Modelo: "+vehiculo.getModel()+"\n";
                text += "ITV: "+Vehiculo.dateToString(vehiculo.getItvDate())+"\n";
                tv.append(text);
            }

            @Override
            public void onFailure(Call<VehiculoExpandable> call, Throwable t) {
                tv.setText(t.getMessage());
            }
        });
    }
    /**private void addVehicle(){
        Call<Boolean> call = fixCarApi.postVehicle("3","254687","2020-05-15","2020-05-15","2020-05-15","2020-05-15","panda","fiat","1.1","2020-05-15","2837DC","panda.jpg");
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, retrofit2.Response<Boolean> response) {
                if (!response.isSuccessful()){
                    Log.e("error",String.valueOf(response.code()));
                }

            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("error2:",t.getMessage());
            }
        });
    }
    private void editVehicle() {
        Call<Boolean> call = fixCarApi.putVehicle(361, "3",
                "200",
                "2020-10-10",
                "2020-10-10",
                "2020-10-10",
                "2020-10-10",
                "test2",
                "test2",
                "",
                "2020-10-10",
                "test2",
                "imagen.jpg");
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, retrofit2.Response<Boolean> response) {
                if (!response.isSuccessful()) {
                    Log.e("error", String.valueOf(response.code()));
                }
                finish();
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("error2:", t.getMessage());
            }
        });
    }

    private void delVehicle() {
        Call<Boolean> call = fixCarApi.delVehicle(381);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, retrofit2.Response<Boolean> response) {
                if (!response.isSuccessful()) {
                    Log.e("error", String.valueOf(response.code()));
                }
                finish();
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("error2:", t.getMessage());
            }
        });
    }

    /**private void editVehicle2() {

        Vehiculo nuevo = new Vehiculo();

        Call<Boolean> call = fixCarApi.putVehicle(361, nuevo);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, retrofit2.Response<Boolean> response) {
                if (!response.isSuccessful()) {
                    Log.e("error", String.valueOf(response.code()));
                }
                finish();
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("error2:", t.getMessage());
            }
        });
    }**/
}
