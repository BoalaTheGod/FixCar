package com.boala.fixcar;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int RESULT_LOAD_IMAGE = 101;
    private Usuario user;
    private SharedPreferences pref;
    private EditText etnombre, etemail, etDireccion, etLocalidad, etTelefono, etFechaNac;
    private TextView nameLabel, emailLabel, direccionLabel, localidadLabel, telefonoLabel, fechaNacLabel, nombreTop, telefonoTop, emailTop;
    private String nombre, email, direccion, localidad, telefono, fechaNac;
    private ImageView edit;
    private Boolean isReady = false;
    private CircleImageView profilePicture;
    private Uri selectedImage;

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
        profilePicture = findViewById(R.id.profilePicture);
        profilePicture.setOnClickListener(this);

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
                user = new Usuario(response.body());
                if (user.getImage()!=null && user.getImage().length()>1) {
                    Picasso.get().load("https://fixcarcesur.herokuapp.com" + user.getImage().substring(2)).
                            placeholder(R.drawable.profile_placeholder).error(R.drawable.profile_placeholder).into(profilePicture);
                }
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
                    Date zero = Vehiculo.stringToDate("01/01/1900");
                    if (!user.getDate().before(zero)) {
                        etFechaNac.setText(Vehiculo.dateToString(user.getDate()));
                    }
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
                t.printStackTrace();
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
            case R.id.profilePicture:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},132);
                }else {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RESULT_LOAD_IMAGE);
                }
                break;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**Se recoge le resultado del intent para guardar la imagen selecionada**/
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            selectedImage = data.getData();

            profilePicture.setImageURI(selectedImage);

            uploadFile(selectedImage);
        }

    }
    private String getRealPathFromURI(Uri contentUri){
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private void uploadFile(Uri fileUri){
        File file = new File(getRealPathFromURI(fileUri));
        MediaType contentType;
        RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)), file);

        Call<Void> call = FixCarClient.getInstance().getApi().uploadUserImage(pref.getInt("userId",-1), requestFile);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Log.e("error", String.valueOf(response.code()));
                    return;
                }
                Log.d("exito","se ha subido la imagen");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
