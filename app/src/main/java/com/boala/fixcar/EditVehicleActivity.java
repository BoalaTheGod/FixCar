package com.boala.fixcar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.IOException;

public class EditVehicleActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int RESULT_LOAD_IMAGE = 1;
    EditText fechaITV, fechaNeumaticos, fechaAceite, fechaRevision, marca, modelo, matricula, motor, color, kilometraje, seguro;
    ImageView header;
    int pos = -1;

    FixCarApi fixCarApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_vehicle);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);

        fechaITV = findViewById(R.id.fechaItv);
        fechaITV.setOnClickListener(this);
        fechaNeumaticos = findViewById(R.id.fechaNeumaticos);
        fechaNeumaticos.setOnClickListener(this);
        fechaAceite = findViewById(R.id.fechaAceite);
        fechaAceite.setOnClickListener(this);
        fechaRevision = findViewById(R.id.fechaRevision);
        fechaRevision.setOnClickListener(this);
        marca = findViewById(R.id.marca);
        modelo = findViewById(R.id.modelo);
        matricula = findViewById(R.id.matricula);
        motor = findViewById(R.id.motor);
        color = findViewById(R.id.color);
        kilometraje = findViewById(R.id.kilometraje);
        seguro = findViewById(R.id.seguro);
        header = findViewById(R.id.header);
        header.setOnClickListener(this);

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

        fab.setOnClickListener(this);
        pos = getIntent().getIntExtra("idVeh",-1);
        if (pos >= 0){
            fechaITV.setText(Vehiculo.dateToString(MainActivity.vehData.get(pos).getFechaItv()));
            fechaNeumaticos.setText(Vehiculo.dateToString(MainActivity.vehData.get(pos).getFechaRuedas()));
            fechaAceite.setText(Vehiculo.dateToString(MainActivity.vehData.get(pos).getFechaAceite()));
            fechaRevision.setText(Vehiculo.dateToString(MainActivity.vehData.get(pos).getFechaRevision()));
            marca.setText(MainActivity.vehData.get(pos).getMarca());
            modelo.setText(MainActivity.vehData.get(pos).getModelo());
            matricula.setText(MainActivity.vehData.get(pos).getMatricula());
            motor.setText(MainActivity.vehData.get(pos).getMotor());
            color.setText(MainActivity.vehData.get(pos).getColor());
            kilometraje.setText(String.valueOf(MainActivity.vehData.get(pos).getKmVehiculo()));
            seguro.setText(MainActivity.vehData.get(pos).getSeguro());
        }
    }

    private void showDatePickerDialog(final EditText editText){
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                final String selectedDate = day+"/"+(month+1)+"/"+year;
                editText.setText(selectedDate);
            }
        });

        newFragment.show(getSupportFragmentManager(),"datePicker");
    }
    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.fechaItv:
                showDatePickerDialog(fechaITV);
                break;
            case R.id.fechaNeumaticos:
                showDatePickerDialog(fechaNeumaticos);
                break;
            case R.id.fechaAceite:
                showDatePickerDialog(fechaAceite);
                break;
            case R.id.fechaRevision:
                showDatePickerDialog(fechaRevision);
                break;
            case R.id.fab:
                try {
                    if (pos > -1) {
                        editVehicle();
                        finish();
                    }else{
                        addVehicle();
                    }
                    break;
                }catch (Exception e){
                    Snackbar.make(view, "Campos vacios", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                break;
            case R.id.header:
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent,RESULT_LOAD_IMAGE);
                break;
        }

    }
    private void editVehicle(){
        Vehiculo editable = MainActivity.vehData.get(pos);
        editable.setFechaItv(Vehiculo.stringToDate(fechaITV.getText().toString()));
        editable.setFechaAceite(Vehiculo.stringToDate(fechaAceite.getText().toString()));
        editable.setFechaRevision(Vehiculo.stringToDate(fechaRevision.getText().toString()));
        editable.setFechaRuedas(Vehiculo.stringToDate(fechaNeumaticos.getText().toString()));
        editable.setColor(color.getText().toString());
        editable.setKmVehiculo(Integer.parseInt(kilometraje.getText().toString()));
        editable.setMarca(marca.getText().toString());
        editable.setModelo(modelo.getText().toString());
        editable.setSeguro(seguro.getText().toString());
        editable.setMotor(motor.getText().toString());
        editable.setMatricula(matricula.getText().toString());

    }
    private void addVehicle(){
        Vehiculo nuevo = new Vehiculo();

        Call<Vehiculo> call = fixCarApi.postVehicle(nuevo);
        call.enqueue(new Callback<Vehiculo>() {
            @Override
            public void onResponse(Call<Vehiculo> call, retrofit2.Response<Vehiculo> response) {
                if (!response.isSuccessful()){
                    Log.e("error",String.valueOf(response.code()));
                    return;
                }

            }

            @Override
            public void onFailure(Call<Vehiculo> call, Throwable t) {
                Log.e("error:",t.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data){
            Uri selectedImage = data.getData();

                header.setImageURI(selectedImage);
            header.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

    }
}
