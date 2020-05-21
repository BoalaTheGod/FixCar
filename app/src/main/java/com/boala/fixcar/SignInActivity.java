package com.boala.fixcar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText nombre, email, pass, passCon;
    private Button send;
    private TextView alredyReg, nameLabel, passConLabel;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        nombre = findViewById(R.id.nombre);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        passCon = findViewById(R.id.passwordConfirm);
        send = findViewById(R.id.send);
        send.setOnClickListener(this);
        alredyReg = findViewById(R.id.alredyReg);
        alredyReg.setOnClickListener(this);
        nameLabel = findViewById(R.id.nameLabel);
        passConLabel = findViewById(R.id.passConLabel);
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
                if (nombre.getText().toString().isEmpty() && nombre.getVisibility() == View.VISIBLE) {
                    nombre.setError("campo requerido");
                    nombre.requestFocus();
                    return;
                }
                if (email.getText().toString().isEmpty()) {
                    email.setError("campo requerido");
                    email.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    email.setError("el email no es valido");
                    email.requestFocus();
                    return;
                }
                if (pass.getText().toString().isEmpty()) {
                    pass.setError("campo requerido");
                    pass.requestFocus();
                    return;
                }
                if (pass.getText().toString().length() < 6) {
                    pass.setError("minimo 6 caracteres");
                    pass.requestFocus();
                    return;
                }
                if (passCon.getText().toString().isEmpty() && passCon.getVisibility() == View.VISIBLE) {
                    passCon.setError("campo requerido");
                    passCon.requestFocus();
                    return;
                }
                if (!passCon.getText().toString().equals(pass.getText().toString()) && passCon.getVisibility() == View.VISIBLE) {
                    passCon.setError("no coincide");
                    passCon.requestFocus();
                    return;
                }

                if (!auxSign) {

                    signIn();

                } else {
                    logIn();
                }
                break;
            case R.id.alredyReg:
                nombre.setVisibility(View.GONE);
                passCon.setVisibility(View.GONE);
                nameLabel.setVisibility(View.GONE);
                passConLabel.setVisibility(View.GONE);
                send.setText("Iniciar sesión");
                alredyReg.setVisibility(View.GONE);
                auxSign = true;

        }

    }
    /**Iniciar sesion con cuenta existente**/
    private void logIn() {
        Call<Integer> call = FixCarClient.getInstance().getApi().getUserByMail(email.getText().toString());
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
                        if (usuario.getPassword().equals(md5(pass.getText().toString()))) {
                            editor.putBoolean("loggedIn", true);
                            editor.apply();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        } else {
                            pass.setError("Contraseña incorrecta");
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
                email.setError("El usuario no existe");
            }
        });
    }
    /**Crear una nueva cuenta**/
    private void signIn() {
        Call<Boolean> call = FixCarClient.getInstance().getApi().postUser(nombre.getText().toString(), "", "", "", email.getText().toString(), "fecha", md5(pass.getText().toString()));
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (!response.isSuccessful()) {
                    Log.e("error", String.valueOf(response.code()));
                    return;
                }
                Call<Integer> call2 = FixCarClient.getInstance().getApi().getUserByMail(email.getText().toString());
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
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
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
        if (nombre.getVisibility() == View.GONE) {
            nombre.setVisibility(View.VISIBLE);
            passCon.setVisibility(View.VISIBLE);
            nameLabel.setVisibility(View.VISIBLE);
            passConLabel.setVisibility(View.VISIBLE);
            send.setText("Regístrate");
            alredyReg.setVisibility(View.VISIBLE);
            auxSign = false;
        } else {
            super.onBackPressed();
        }
    }
}
