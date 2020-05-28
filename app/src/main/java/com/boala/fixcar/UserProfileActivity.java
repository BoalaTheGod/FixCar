package com.boala.fixcar;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private Usuario user;
    private SharedPreferences pref;
    private EditText etnombre, etemail, etDireccion, etLocalidad, etTelefono, etFechaNac;
    private TextView nameLabel, emailLabel, direccionLabel, localidadLabel, telefonoLabel, fechaNacLabel, nombreTop, telefonoTop, emailTop;
    private String nombre, email, direccion, localidad, telefono, fechaNac;
    private ImageView edit;
    private Boolean isReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        etnombre = findViewById(R.id.nombre);
        etemail = findViewById(R.id.email);
        nameLabel = findViewById(R.id.nameLabel);
        emailLabel = findViewById(R.id.emailLabel);
        etDireccion = findViewById(R.id.direccion);
        etLocalidad = findViewById(R.id.localidad);
        etTelefono = findViewById(R.id.telefono);
        etFechaNac = findViewById(R.id.fechaNac);
        etFechaNac.setOnClickListener(this);
        direccionLabel = findViewById(R.id.direccionLabel);
        localidadLabel = findViewById(R.id.localidadLabel);
        telefonoLabel = findViewById(R.id.telefonoLabel);
        fechaNacLabel = findViewById(R.id.fechaNacLabel);
        edit = findViewById(R.id.edit);
        edit.setOnClickListener(this);
        nombreTop = findViewById(R.id.nombreTop);
        emailTop = findViewById(R.id.emailTop);
        telefonoTop = findViewById(R.id.telefonoTop);

        getUser();

    }

    public void getUser() {
        edit.setEnabled(false);
        Call<Usuario> call = FixCarClient.getInstance().getApi().getUser(pref.getInt("userId", -1));
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (!response.isSuccessful()) {
                    Log.e("error", String.valueOf(response.code()));
                    return;
                }
                user = response.body();
                try {
                    nombreTop.setText(user.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                    nombreTop.setText("");
                }
                try {
                    emailTop.setText(user.getEmail());
                } catch (Exception e) {
                    e.printStackTrace();
                    emailTop.setText("");
                }
                try {
                    telefonoTop.setText(String.valueOf(user.getPhoneNumber()));
                } catch (Exception e) {
                    e.printStackTrace();
                    telefonoTop.setText("");
                }
                try {
                    etnombre.setText(user.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                    etnombre.setText("");
                }
                try {
                    etemail.setText(user.getEmail());
                } catch (Exception e) {
                    e.printStackTrace();
                    etemail.setText("");
                }
                try {
                    etDireccion.setText(user.getAdress());
                } catch (Exception e) {
                    e.printStackTrace();
                    etDireccion.setText("");
                }
                try {
                    etFechaNac.setText(Vehiculo.dateToString(user.getDate()));
                } catch (Exception e) {
                    e.printStackTrace();
                    etFechaNac.setText("");
                }
                try {
                    etLocalidad.setText(user.getCity());
                } catch (Exception e) {
                    e.printStackTrace();
                    etLocalidad.setText("");
                }
                try {
                    etLocalidad.setText(user.getCity());
                } catch (Exception e) {
                    e.printStackTrace();
                    etLocalidad.setText("");
                }
                try {
                    etTelefono.setText(String.valueOf(user.getPhoneNumber()));
                } catch (Exception e) {
                    e.printStackTrace();
                    etTelefono.setText("");
                }


                edit.setEnabled(true);
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }

    public void saveData() {

        Call<Boolean> call = FixCarClient.getInstance().getApi().putUser(pref.getInt("userId", -1), String.valueOf(pref.getInt("userId", -1)), nombre,
                direccion, localidad, telefono, email, fechaNac,"1");
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (!response.isSuccessful()) {
                    Log.e("error", String.valueOf(response.code()));
                    return;
                }
                edit.setImageDrawable(getDrawable(R.drawable.pencil));
                nameLabel.setVisibility(View.GONE);
                etnombre.setVisibility(View.GONE);
                emailLabel.setVisibility(View.GONE);
                etemail.setVisibility(View.GONE);
                telefonoLabel.setVisibility(View.GONE);
                etTelefono.setVisibility(View.GONE);
                etFechaNac.setEnabled(false);
                etDireccion.setEnabled(false);
                etLocalidad.setEnabled(false);
                isReady = false;
                getUser();
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.fechaNac:
                showDatePickerDialog(etFechaNac);
                break;
            case R.id.edit:
                if (!isReady) {
                    nameLabel.setVisibility(View.VISIBLE);
                    etnombre.setVisibility(View.VISIBLE);
                    emailLabel.setVisibility(View.VISIBLE);
                    etemail.setVisibility(View.VISIBLE);
                    telefonoLabel.setVisibility(View.VISIBLE);
                    etTelefono.setVisibility(View.VISIBLE);
                    etDireccion.setEnabled(true);
                    etLocalidad.setEnabled(true);
                    etFechaNac.setEnabled(true);
                    edit.setImageDrawable(getDrawable(R.drawable.floppy2));
                    isReady = true;
                    break;
                } else {
                    nombre = etnombre.getText().toString();
                    email = etemail.getText().toString();
                    telefono = etTelefono.getText().toString();
                    direccion = etDireccion.getText().toString();
                    localidad = etLocalidad.getText().toString();
                    fechaNac = Vehiculo.dateToString2(Vehiculo.stringToDate(etFechaNac.getText().toString()));
                    nombre = etnombre.getText().toString();
                    nombre = etnombre.getText().toString();
                    saveData();
                }
        }

    }

    /**
     * Funcion que muestra el dialogo de elegir fecha
     **/
    private void showDatePickerDialog(final EditText editText) {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                final String selectedDate = day + "/" + (month + 1) + "/" + year;
                editText.setText(selectedDate);
            }
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
}
