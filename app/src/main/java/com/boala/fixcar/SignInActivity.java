package com.boala.fixcar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etnombre, etemail, etpass, etpassCon, etDireccion, etLocalidad, etTelefono, etFechaNac;
    private Button send, completeForm;
    private TextView alredyReg, nameLabel, passConLabel, passLabel, emailLabel, direccionLabel, localidadLabel, telefonoLabel, fechaNacLabel, dismissForm;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private String nombre,email,pass,passCon,direccion,localidad,telefono,fechaNac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        etnombre = findViewById(R.id.nombre);
        etemail = findViewById(R.id.email);
        etpass = findViewById(R.id.password);
        etpassCon = findViewById(R.id.passwordConfirm);
        send = findViewById(R.id.send);
        send.setOnClickListener(this);
        alredyReg = findViewById(R.id.alredyReg);
        alredyReg.setOnClickListener(this);
        nameLabel = findViewById(R.id.nameLabel);
        passConLabel = findViewById(R.id.passConLabel);
        emailLabel = findViewById(R.id.emailLabel);
        passLabel = findViewById(R.id.passLabel);
        etDireccion = findViewById(R.id.direccion);
        etLocalidad = findViewById(R.id.localidad);
        etTelefono = findViewById(R.id.telefono);
        etFechaNac = findViewById(R.id.fechaNac);
        etFechaNac.setOnClickListener(this);
        direccionLabel = findViewById(R.id.direccionLabel);
        localidadLabel = findViewById(R.id.localidadLabel);
        telefonoLabel = findViewById(R.id.telefonoLabel);
        fechaNacLabel = findViewById(R.id.fechaNacLabel);
        completeForm = findViewById(R.id.completeForm);
        completeForm.setOnClickListener(this);
        dismissForm = findViewById(R.id.dismissForm);
        dismissForm.setOnClickListener(this);
    }
    /**Funcion para encriptar la contraseña**/
    public String md5(String string) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");

            digest.update(string.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    /**Variable auxiliar para saber cuando hacer login o registro**/
    Boolean auxSign = false;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send:
                //Calendar today = Calendar.getInstance();
                //String fecha = today.get(Calendar.YEAR)+"-"+(today.get(Calendar.MONTH)+1)+"-"+today.get(Calendar.DAY_OF_MONTH);
                if (etnombre.getText().toString().isEmpty() && etnombre.getVisibility() == View.VISIBLE) {
                    etnombre.setError("campo requerido");
                    etnombre.requestFocus();
                    return;
                }
                if (etemail.getText().toString().isEmpty()) {
                    etemail.setError("campo requerido");
                    etemail.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(etemail.getText().toString()).matches()) {
                    etemail.setError("el email no es valido");
                    etemail.requestFocus();
                    return;
                }
                if (etpass.getText().toString().isEmpty()) {
                    etpass.setError("campo requerido");
                    etpass.requestFocus();
                    return;
                }
                if (etpass.getText().toString().length() < 6) {
                    etpass.setError("minimo 6 caracteres");
                    etpass.requestFocus();
                    return;
                }
                if (etpassCon.getText().toString().isEmpty() && etpassCon.getVisibility() == View.VISIBLE) {
                    etpassCon.setError("campo requerido");
                    etpassCon.requestFocus();
                    return;
                }
                if (!etpassCon.getText().toString().equals(etpass.getText().toString()) && etpassCon.getVisibility() == View.VISIBLE) {
                    etpassCon.setError("no coincide");
                    etpassCon.requestFocus();
                    return;
                }

                nombre = etnombre.getText().toString();
                email = etemail.getText().toString();
                pass = etpass.getText().toString();
                passCon = etpassCon.getText().toString();

                if (!auxSign) {

                    signIn();

                } else {
                    logIn();
                }
                break;
            case R.id.alredyReg:
                etnombre.setVisibility(View.GONE);
                etpassCon.setVisibility(View.GONE);
                nameLabel.setVisibility(View.GONE);
                passConLabel.setVisibility(View.GONE);
                send.setText("Iniciar sesión");
                alredyReg.setVisibility(View.GONE);
                auxSign = true;
                break;
            case R.id.fechaNac:
                showDatePickerDialog(etFechaNac);
                break;
            case R.id.dismissForm:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                break;
            case R.id.completeForm:
                try {
                    direccion = etDireccion.getText().toString();
                }catch (NullPointerException e){
                    direccion = "";
                    e.printStackTrace();
                }
                try {
                    localidad = etLocalidad.getText().toString();
                }catch (NullPointerException e){
                    localidad = "";
                    e.printStackTrace();
                }
                try {
                    telefono = etTelefono.getText().toString();
                }catch (NullPointerException e){
                    telefono = "";
                    e.printStackTrace();
                }
                try {
                    fechaNac = Vehiculo.dateToString2(Vehiculo.stringToDate(etFechaNac.getText().toString()));
                }catch (NullPointerException e){
                    fechaNac = "0000-00-00";
                    e.printStackTrace();
                }
                Call<Boolean> call = FixCarClient.getInstance().getApi().putUser(pref.getInt("userId",-1),String.valueOf(pref.getInt("userId",-1)),nombre,direccion,localidad,telefono,email,fechaNac,"1");
                call.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (!response.isSuccessful()) {
                            Log.e("error", String.valueOf(response.code()));
                            return;
                        }
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Log.e("error", t.getMessage());
                    }
                });



        }

    }
    /**Iniciar sesion con cuenta existente**/
    private void logIn() {
        Call<Integer> call = FixCarClient.getInstance().getApi().getUserByMail(email);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (!response.isSuccessful()) {
                    Log.e("error", String.valueOf(response.code()));
                    return;
                }
                int id = response.body();
                editor.putInt("userId", id);
                editor.apply();
                Log.e("exito", "el id del usuario es:" + pref.getInt("userId", -1));
                Call<Usuario> call2 = FixCarClient.getInstance().getApi().getUser(pref.getInt("userId", -1));
                call2.enqueue(new Callback<Usuario>() {
                    @Override
                    public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                        if (!response.isSuccessful()) {
                            Log.e("error", String.valueOf(response.code()));
                            return;
                        }
                        Usuario usuario = response.body();
                        if (usuario.getPassword().equals(md5(etpass.getText().toString()))) {
                            editor.putBoolean("loggedIn", true);
                            editor.apply();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        } else {
                            etpass.setError("Contraseña incorrecta");
                            return;
                        }
                    }

                    @Override
                    public void onFailure(Call<Usuario> call, Throwable t) {
                        Log.e("error", t.getMessage());
                    }
                });
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e("error", t.getMessage());
                etemail.setError("El usuario no existe");
            }
        });
    }
    /**Crear una nueva cuenta**/
    private void signIn() {
        Call<Boolean> call = FixCarClient.getInstance().getApi().postUser(nombre, "", "", "", email, "", md5(pass));
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (!response.isSuccessful()) {
                    Log.e("error", String.valueOf(response.code()));
                    return;
                }
                Call<Integer> call2 = FixCarClient.getInstance().getApi().getUserByMail(etemail.getText().toString());
                call2.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        if (!response.isSuccessful()) {
                            Log.e("error", String.valueOf(response.code()));
                            return;
                        }
                        int id = response.body();
                        editor.putInt("userId", id);
                        editor.putBoolean("loggedIn", true);
                        editor.apply();

                        etnombre.setVisibility(View.GONE);
                        nameLabel.setVisibility(View.GONE);
                        etemail.setVisibility(View.GONE);
                        emailLabel.setVisibility(View.GONE);
                        etpass.setVisibility(View.GONE);
                        passLabel.setVisibility(View.GONE);
                        etpassCon.setVisibility(View.GONE);
                        passConLabel.setVisibility(View.GONE);
                        send.setVisibility(View.GONE);
                        alredyReg.setVisibility(View.GONE);

                        direccionLabel.setVisibility(View.VISIBLE);
                        etDireccion.setVisibility(View.VISIBLE);
                        localidadLabel.setVisibility(View.VISIBLE);
                        etLocalidad.setVisibility(View.VISIBLE);
                        telefonoLabel.setVisibility(View.VISIBLE);
                        etTelefono.setVisibility(View.VISIBLE);
                        fechaNacLabel.setVisibility(View.VISIBLE);
                        etFechaNac.setVisibility(View.VISIBLE);
                        completeForm.setVisibility(View.VISIBLE);
                        dismissForm.setVisibility(View.VISIBLE);

                        //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        //finish();
                        Log.e("exito", "el id del usuario es:" + pref.getInt("userId", -1));
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        Log.e("error", t.getMessage());
                    }
                });
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }
    /**Funcion que recoge el id del usuario y lo guarda en preferencias**/
    private void getId(String email) {
        Call<Integer> call = FixCarClient.getInstance().getApi().getUserByMail(email);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (!response.isSuccessful()) {
                    Log.e("error", String.valueOf(response.code()));
                    return;
                }
                int id = response.body();
                editor.putInt("userId", id);
                editor.apply();
                Log.e("exito", "el id del usuario es:" + pref.getInt("userId", -1));
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (etnombre.getVisibility() == View.GONE && !pref.getBoolean("loggedIn",false)) {
            etnombre.setVisibility(View.VISIBLE);
            etpassCon.setVisibility(View.VISIBLE);
            nameLabel.setVisibility(View.VISIBLE);
            passConLabel.setVisibility(View.VISIBLE);
            send.setText("Regístrate");
            alredyReg.setVisibility(View.VISIBLE);
            auxSign = false;
        } else {
            super.onBackPressed();
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
