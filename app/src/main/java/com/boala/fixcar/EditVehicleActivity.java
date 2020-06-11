package com.boala.fixcar;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditVehicleActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int RESULT_LOAD_IMAGE = 100;
    private static final int RESULT_LOAD_DOCUMENT = 102;
    private EditText docTypeET,docNotesET,fechaITV, fechaNeumaticos, fechaAceite, fechaRevision, marca, modelo, matricula, motor, kilometraje, seguro,insuranceNote,itvNote,tiresNote,oilNote,reviewNote;
    private ImageView header, addDocIMG, delEnsurance, delITV, delTires,delOil, delReview;
    private int id = -1;
    private int pos = -1;
    private Button delButton,addDoc;
    private Vehiculo getresult;
    private FloatingActionButton fab;
    private SharedPreferences pref;
    private Uri selectedImage;
    private Uri selectedDocImg;
    private Toolbar toolbar;
    private CollapsingToolbarLayout toolbarLayout;
    private RecyclerView rvDocs;
    private DocAdapter adapter;
    public static ArrayList<DocumentFixCar> docsData;
    private CardView addDocCard;
    private LinearLayout ensurancePH,itvPH,tiresPH,oilPH,revisionPH, ensuranceLayout, itvLayout, tiresLayout, revisionLayout, oilLayout;
    private String fechaITVText, fechaNeumaticosText, fechaAceiteText, fechaRevisionText, marcaText, modeloText, matriculaText, motorText, kilometrajeText, seguroText,insuranceNoteText,itvNoteText,tiresNoteText,oilNoteText,reviewNoteText;
    private int docId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_vehicle);
        toolbar = findViewById(R.id.toolbar);
        toolbarLayout = findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab);

        delEnsurance = findViewById(R.id.delEnsurance);
        delEnsurance.setOnClickListener(this);
        delITV = findViewById(R.id.delITV);
        delITV.setOnClickListener(this);
        delTires = findViewById(R.id.delTires);
        delTires.setOnClickListener(this);
        delOil = findViewById(R.id.delOil);
        delOil.setOnClickListener(this);
        delReview = findViewById(R.id.delReview);
        delReview.setOnClickListener(this);



        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        addDocCard = findViewById(R.id.addDocCard);

        docTypeET = findViewById(R.id.docTypeET);
        docNotesET = findViewById(R.id.docNotesET);
        addDocIMG = findViewById(R.id.addDocIMG);
        addDocIMG.setOnClickListener(this);
        addDoc = findViewById(R.id.addDocBT);
        addDoc.setOnClickListener(this);
        docTypeET.setOnClickListener(this);

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
        kilometraje = findViewById(R.id.kilometraje);
        seguro = findViewById(R.id.seguro);
        header = findViewById(R.id.header);
        header.setOnClickListener(this);
        delButton = findViewById(R.id.delVehicle);
        delButton.setOnClickListener(this);
        seguro.setOnClickListener(this);
        insuranceNote = findViewById(R.id.insuranceNote);
        itvNote = findViewById(R.id.itvNote);
        tiresNote = findViewById(R.id.tiresNote);
        oilNote = findViewById(R.id.oilNote);
        reviewNote = findViewById(R.id.reviewNote);

        ensurancePH = findViewById(R.id.ensurancePH);
        ensurancePH.setOnClickListener(this);
        itvPH = findViewById(R.id.itvPH);
        itvPH.setOnClickListener(this);
        tiresPH = findViewById(R.id.tiresPH);
        tiresPH.setOnClickListener(this);
        oilPH = findViewById(R.id.oilPH);
        oilPH.setOnClickListener(this);
        revisionPH = findViewById(R.id.revisionPH);
        revisionPH.setOnClickListener(this);

        ensuranceLayout = findViewById(R.id.ensuranceLayout);
        itvLayout = findViewById(R.id.itvLayout);
        tiresLayout = findViewById(R.id.tiresLayout);
        oilLayout = findViewById(R.id.oilLayout);
        revisionLayout = findViewById(R.id.revisionLayout);


        docsData = new ArrayList<>();
        rvDocs = findViewById(R.id.docsRV);
        Context context;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvDocs.setLayoutManager(linearLayoutManager);
        adapter = new DocAdapter(this, docsData);
        rvDocs.setAdapter(adapter);

        id = getIntent().getIntExtra("idVeh", -1);
        pos = getIntent().getIntExtra("pos", -1);
        /**Boton de guardar cambios**/
        fab.setOnClickListener(this);
        if (id >= 0) {
            getVehicle(id);
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

    /**
     * Onclick listener general
     **/
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
            case R.id.seguro:
                showDatePickerDialog(seguro);
                break;
            case R.id.fab:
                try {
                    if (id > -1) {
                        editVehicle();
                    } else {
                        addVehicle();
                    }
                    break;
                } catch (Exception e) {
                    Snackbar.make(view, "Campos vacios", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Log.e("error", e.getMessage());
                }
                break;
            case R.id.header:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},132);
                }else {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RESULT_LOAD_IMAGE);
                }
                break;
            case R.id.delVehicle:
                new AlertDialog.Builder(this)
                        .setIcon(R.drawable.delete_empty)
                        .setTitle("Eliminando vehiculo")
                        .setMessage("¿deseas eliminar este vehiculo?,\nse eliminará de manera permanente.")
                        .setPositiveButton("eliminar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                delVehicle(id);
                            }
                        })
                        .setNegativeButton("no eliminar", null)
                        .show();
                break;
            case R.id.addDocIMG:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},132);
                }else {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RESULT_LOAD_DOCUMENT);
                }
                break;
            case R.id.addDocBT:
                if (addDocCard.getVisibility() == View.GONE){
                    addDocCard.setVisibility(View.VISIBLE);
                    addDoc.setText("Guardar");
                }else{
                    addDocCard.setVisibility(View.GONE);
                    addDoc.setText("Añadir Documento");
                    uploadDocument();
                }
                break;
            case R.id.ensurancePH:
                seguro.setVisibility(View.VISIBLE);
                insuranceNote.setVisibility(View.VISIBLE);
                ensurancePH.setVisibility(View.GONE);
                delEnsurance.setVisibility(View.VISIBLE);
                break;
            case R.id.revisionPH:
                fechaRevision.setVisibility(View.VISIBLE);
                reviewNote.setVisibility(View.VISIBLE);
                revisionPH.setVisibility(View.GONE);
                delReview.setVisibility(View.VISIBLE);
                break;
            case R.id.oilPH:
                fechaAceite.setVisibility(View.VISIBLE);
                oilNote.setVisibility(View.VISIBLE);
                oilPH.setVisibility(View.GONE);
                delOil.setVisibility(View.VISIBLE);
                break;
            case R.id.tiresPH:
                fechaNeumaticos.setVisibility(View.VISIBLE);
                tiresNote.setVisibility(View.VISIBLE);
                tiresPH.setVisibility(View.GONE);
                delTires.setVisibility(View.VISIBLE);
                break;
            case R.id.itvPH:
                fechaITV.setVisibility(View.VISIBLE);
                itvNote.setVisibility(View.VISIBLE);
                itvPH.setVisibility(View.GONE);
                delITV.setVisibility(View.VISIBLE);
                break;
            case R.id.delEnsurance:
                if (ensurancePH.getVisibility() == View.GONE) {
                    new AlertDialog.Builder(this)
                            .setIcon(R.drawable.delete_empty)
                            .setTitle("Eliminando Recordatorio")
                            .setMessage("¿deseas eliminar este recordatorio?,\nse eliminará de manera permanente.")
                            .setPositiveButton("eliminar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    seguro.setText("");
                                    insuranceNote.setText("");
                                    seguro.setVisibility(View.GONE);
                                    insuranceNote.setVisibility(View.GONE);
                                    ensurancePH.setVisibility(View.VISIBLE);
                                    delEnsurance.setVisibility(View.GONE);
                                }
                            })
                            .setNegativeButton("no eliminar", null)
                            .show();
                }
                break;
            case R.id.delITV:
                if (itvPH.getVisibility() == View.GONE) {
                    new AlertDialog.Builder(this)
                            .setIcon(R.drawable.delete_empty)
                            .setTitle("Eliminando Recordatorio")
                            .setMessage("¿deseas eliminar este recordatorio?,\nse eliminará de manera permanente.")
                            .setPositiveButton("eliminar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    fechaITV.setText("");
                                    itvNote.setText("");
                                    fechaITV.setVisibility(View.GONE);
                                    itvNote.setVisibility(View.GONE);
                                    itvPH.setVisibility(View.VISIBLE);
                                    delITV.setVisibility(View.GONE);
                                }
                            })
                            .setNegativeButton("no eliminar", null)
                            .show();
                }
                break;
            case R.id.delTires:
                if (tiresPH.getVisibility() == View.GONE) {
                    new AlertDialog.Builder(this)
                            .setIcon(R.drawable.delete_empty)
                            .setTitle("Eliminando Recordatorio")
                            .setMessage("¿deseas eliminar este recordatorio?,\nse eliminará de manera permanente.")
                            .setPositiveButton("eliminar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    fechaNeumaticos.setText("");
                                    tiresNote.setText("");
                                    fechaNeumaticos.setVisibility(View.GONE);
                                    tiresNote.setVisibility(View.GONE);
                                    tiresPH.setVisibility(View.VISIBLE);
                                    delTires.setVisibility(View.GONE);
                                }
                            })
                            .setNegativeButton("no eliminar", null)
                            .show();
                }
                break;
            case R.id.delOil:
                if (oilPH.getVisibility() == View.GONE) {
                    new AlertDialog.Builder(this)
                            .setIcon(R.drawable.delete_empty)
                            .setTitle("Eliminando Recordatorio")
                            .setMessage("¿deseas eliminar este recordatorio?,\nse eliminará de manera permanente.")
                            .setPositiveButton("eliminar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    fechaAceite.setText("");
                                    oilNote.setText("");
                                    fechaAceite.setVisibility(View.GONE);
                                    oilNote.setVisibility(View.GONE);
                                    oilPH.setVisibility(View.VISIBLE);
                                    delOil.setVisibility(View.GONE);
                                }
                            })
                            .setNegativeButton("no eliminar", null)
                            .show();
                }
                break;
            case R.id.delReview:
                if (revisionPH.getVisibility() == View.GONE) {
                    new AlertDialog.Builder(this)
                            .setIcon(R.drawable.delete_empty)
                            .setTitle("Eliminando Recordatorio")
                            .setMessage("¿deseas eliminar este recordatorio?,\nse eliminará de manera permanente.")
                            .setPositiveButton("eliminar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    fechaRevision.setText("");
                                    reviewNote.setText("");
                                    fechaRevision.setVisibility(View.GONE);
                                    reviewNote.setVisibility(View.GONE);
                                    revisionPH.setVisibility(View.VISIBLE);
                                    delReview.setVisibility(View.GONE);
                                }
                            })
                            .setNegativeButton("no eliminar", null)
                            .show();
                }
                break;
            case R.id.docTypeET:
                final String[] selectedItem = new String[1];
                Context context;
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final String[] docTypes = new String[]{
                        "Documentos de identidad",
                        "Mis seguros",
                        "Mis datos ITV",
                        "Mis revisiones",
                        "Otros datos"
                };
                builder.setSingleChoiceItems(docTypes, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedItem[0] = Arrays.asList(docTypes).get(i);
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        docTypeET.setText(selectedItem[0]);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
        }

    }
    private void parseTextViews(){
        if (kilometraje.getText()==null){
            kilometrajeText = "";
        }else{
            kilometrajeText = kilometraje.getText().toString();
        }
        if (modelo.getText()==null){
            modeloText = "";
        }else{
            modeloText = modelo.getText().toString();
        }
        if (marca.getText()==null){
            marcaText = "";
        }else{
            marcaText = marca.getText().toString();
        }
        if (motor.getText()==null){
            motorText = "";
        }else{
            motorText = motor.getText().toString();
        }
        if (matricula.getText()==null){
            matriculaText = "";
        }else{
            matriculaText = matricula.getText().toString();
        }
        if (fechaITV.getText()==null || fechaITV.getText().toString().isEmpty()){
            fechaITVText = "00-00-0000";
        }else{
            fechaITVText = Vehiculo.dateToString2(Vehiculo.stringToDate(fechaITV.getText().toString()));
        }
        if (itvNote.getText()==null){
            itvNoteText = "";
        }else{
            itvNoteText = itvNote.getText().toString();
        }
        if (fechaAceite.getText()==null || fechaAceite.getText().toString().isEmpty()){
            fechaAceiteText = "00-00-0000";
        }else{
            fechaAceiteText = Vehiculo.dateToString2(Vehiculo.stringToDate(fechaAceite.getText().toString()));
        }
        if (oilNote.getText()==null){
            oilNoteText = "";
        }else{
            oilNoteText = oilNote.getText().toString();
        }
        if (fechaNeumaticos.getText()==null || fechaNeumaticos.getText().toString().isEmpty()){
            fechaNeumaticosText = "00-00-0000";
        }else{
            fechaNeumaticosText = Vehiculo.dateToString2(Vehiculo.stringToDate(fechaNeumaticos.getText().toString()));
        }
        if (tiresNote.getText()==null){
            tiresNoteText = "";
        }else{
            tiresNoteText = tiresNote.getText().toString();
        }
        if (fechaRevision.getText()==null || fechaRevision.getText().toString().isEmpty()){
            fechaRevisionText = "00-00-0000";
        }else{
            fechaRevisionText = Vehiculo.dateToString2(Vehiculo.stringToDate(fechaRevision.getText().toString()));
        }
        if (reviewNote.getText()==null){
            reviewNoteText = "";
        }else{
            reviewNoteText = reviewNote.getText().toString();
        }
        if (seguro.getText()==null || seguro.getText().toString().isEmpty()){
            seguroText = "00-00-0000";
        }else{
            seguroText = Vehiculo.dateToString2(Vehiculo.stringToDate(seguro.getText().toString()));
        }
        if (insuranceNote.getText()==null){
            insuranceNoteText = "";
        }else{
            insuranceNoteText = insuranceNote.getText().toString();
        }
    }

    /**
     * Funcion que guarda los cambios en la base datos
     **/
    private void editVehicle() {
        parseTextViews();
        Call<Boolean> call = FixCarClient.getInstance().getApi().putVehicle(id,
                kilometrajeText,
                modeloText,
                marcaText,
                motorText,
                matriculaText
        );
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, retrofit2.Response<Boolean> response) {
                if (!response.isSuccessful()) {
                    Log.e("error", String.valueOf(response.code()));
                }
                Call<Boolean> call1 = FixCarClient.getInstance().getApi().reminderPut(id,
                        fechaITVText,
                        itvNoteText,
                        fechaNeumaticosText,
                        tiresNoteText,
                        fechaAceiteText,
                        oilNoteText,
                        fechaRevisionText,
                        reviewNoteText,
                        seguroText,
                        insuranceNoteText);
                call1.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (!response.isSuccessful()) {
                            Log.e("error", String.valueOf(response.code()));
                        }
                        if (selectedImage != null){
                            uploadPicture(selectedImage);
                        }else {
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Log.e("error2:", t.getMessage());
                    }
                });
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("error2:", t.getMessage());
            }
        });

    }

    /**
     * Funcion que guarda el vehiculo nuevo
     **/
    private void addVehicle() {
        parseTextViews();
        Call<Integer> call = FixCarClient.getInstance().getApi().postVehicle(String.valueOf(pref.getInt("userId", -1)),
                kilometrajeText,
                modeloText,
                marcaText,
                motorText,
                matriculaText);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, retrofit2.Response<Integer> response) {
                if (!response.isSuccessful()) {
                    Log.e("error", String.valueOf(response.code()));
                }
                id = response.body();
                Call<Boolean> call1 = FixCarClient.getInstance().getApi().reminderPut(id,
                        fechaITVText,
                        itvNoteText,
                        fechaNeumaticosText,
                        tiresNoteText,
                        fechaAceiteText,
                        oilNoteText,
                        fechaRevisionText,
                        reviewNoteText,
                        seguroText,
                        insuranceNoteText);
                call1.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (!response.isSuccessful()) {
                            Log.e("error", String.valueOf(response.code()));
                        }
                        if (selectedImage != null){
                            uploadPicture(selectedImage);
                        }else {
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Log.e("error2:", t.getMessage());
                    }
                });
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e("error2:", t.getMessage());
            }
        });
    }

    /**
     * Funcion que recoge los datos del vehiculo seleccionado
     **/
    private void getVehicle(int vehId) {
        kilometraje.setEnabled(false);
        fechaITV.setEnabled(false);
        fechaNeumaticos.setEnabled(false);
        fechaAceite.setEnabled(false);
        fechaRevision.setEnabled(false);
        modelo.setEnabled(false);
        marca.setEnabled(false);
        motor.setEnabled(false);
        seguro.setEnabled(false);
        matricula.setEnabled(false);
        delButton.setEnabled(false);
        header.setEnabled(false);
        fab.setEnabled(false);
        insuranceNote.setEnabled(false);
        reviewNote.setEnabled(false);
        tiresNote.setEnabled(false);
        oilNote.setEnabled(false);
        itvNote.setEnabled(false);

        Call<VehiculoExpandable> call = FixCarClient.getInstance().getApi().getVehicle(vehId);
        call.enqueue(new Callback<VehiculoExpandable>() {
            @Override
            public void onResponse(Call<VehiculoExpandable> call, retrofit2.Response<VehiculoExpandable> response) {
                if (!response.isSuccessful()) {
                    Log.e("Code: ", String.valueOf(response.code()));
                    return;
                }
                Vehiculo respuesta = response.body();
                getresult = new Vehiculo(respuesta);
                updateUI();
                kilometraje.setEnabled(true);
                fechaITV.setEnabled(true);
                fechaNeumaticos.setEnabled(true);
                fechaAceite.setEnabled(true);
                fechaRevision.setEnabled(true);
                modelo.setEnabled(true);
                marca.setEnabled(true);
                motor.setEnabled(true);
                seguro.setEnabled(true);
                matricula.setEnabled(true);
                delButton.setEnabled(true);
                header.setEnabled(true);
                fab.setEnabled(true);
                insuranceNote.setEnabled(true);
                reviewNote.setEnabled(true);
                tiresNote.setEnabled(true);
                oilNote.setEnabled(true);
                itvNote.setEnabled(true);
            }

            @Override
            public void onFailure(Call<VehiculoExpandable> call, Throwable t) {
                Log.e("error: ", t.getMessage());
            }
        });
    }

    /**
     * Funcion que elimina el vehiculo
     **/
    private void delVehicle(int id) {
        if (id >= 0) {
            Call<Boolean> call = FixCarClient.getInstance().getApi().delVehicle(id);
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
        } else {
            finish();
        }
    }

    /**
     * Funcion que actualiza la interfaz con los datos del vehiculo seleccionado
     **/
    private void updateUI() {
        Date zero = Vehiculo.stringToDate("01/01/1900");
        if (!getresult.getItvDate().before(zero)) {
            fechaITV.setText(Vehiculo.dateToString(getresult.getItvDate()));
            fechaITV.setVisibility(View.VISIBLE);
            itvNote.setVisibility(View.VISIBLE);
            itvPH.setVisibility(View.GONE);
            delITV.setVisibility(View.VISIBLE);
        }else {
            fechaITV.setText("");
            fechaITV.setVisibility(View.GONE);
            itvNote.setVisibility(View.GONE);
            itvPH.setVisibility(View.VISIBLE);
            delITV.setVisibility(View.GONE);
        }
        if (!getresult.getTiresDate().before(zero)) {
            fechaNeumaticos.setText(Vehiculo.dateToString(getresult.getTiresDate()));
            fechaNeumaticos.setVisibility(View.VISIBLE);
            tiresNote.setVisibility(View.VISIBLE);
            tiresPH.setVisibility(View.GONE);
            delTires.setVisibility(View.VISIBLE);
        }else {
            fechaNeumaticos.setText("");
            fechaNeumaticos.setVisibility(View.GONE);
            tiresNote.setVisibility(View.GONE);
            tiresPH.setVisibility(View.VISIBLE);
            delTires.setVisibility(View.GONE);
        }
        if (!getresult.getOilDate().before(zero)) {
            fechaAceite.setText(Vehiculo.dateToString(getresult.getOilDate()));
            fechaAceite.setVisibility(View.VISIBLE);
            oilNote.setVisibility(View.VISIBLE);
            oilPH.setVisibility(View.GONE);
            delOil.setVisibility(View.VISIBLE);
        }else {
            fechaAceite.setText("");
            fechaAceite.setVisibility(View.GONE);
            oilNote.setVisibility(View.GONE);
            oilPH.setVisibility(View.VISIBLE);
            delOil.setVisibility(View.GONE);
        }
        if (!getresult.getRevisionDate().before(zero)) {
            fechaRevision.setText(Vehiculo.dateToString(getresult.getRevisionDate()));
            fechaRevision.setVisibility(View.VISIBLE);
            reviewNote.setVisibility(View.VISIBLE);
            revisionPH.setVisibility(View.GONE);
            delReview.setVisibility(View.VISIBLE);
        }else {
            fechaRevision.setText("");
            fechaRevision.setVisibility(View.GONE);
            reviewNote.setVisibility(View.GONE);
            revisionPH.setVisibility(View.VISIBLE);
            delReview.setVisibility(View.GONE);
        }
        marca.setText(getresult.getBrand());
        modelo.setText(getresult.getModel());
        matricula.setText(getresult.getLicencePlate());
        motor.setText(getresult.getEngine());
        kilometraje.setText(String.valueOf(getresult.getKmVehicle()));
        if (!getresult.getInsuranceDate().before(zero)) {
            seguro.setText(Vehiculo.dateToString(getresult.getInsuranceDate()));
            seguro.setVisibility(View.VISIBLE);
            insuranceNote.setVisibility(View.VISIBLE);
            ensurancePH.setVisibility(View.GONE);
        }else {
            seguro.setText("");
            seguro.setVisibility(View.GONE);
            insuranceNote.setVisibility(View.GONE);
            ensurancePH.setVisibility(View.VISIBLE);
        }
        toolbarLayout.setTitle(getresult.getBrand()+" "+getresult.getModel());

        if (getresult.getImage()!=null && getresult.getImage().length()>1) {
            Picasso.get().load("https://fixcarcesur.herokuapp.com/"+getresult.getImage().substring(2)).into(header);
        }

        header.setScaleType(ImageView.ScaleType.CENTER_CROP);
        itvNote.setText(getresult.getItv_note());
        insuranceNote.setText(getresult.getVehicle_note());
        oilNote.setText(getresult.getOil_note());
        tiresNote.setText(getresult.getWheels_note());
        reviewNote.setText(getresult.getReview_note());
        getDocuments();

    }
    private void getDocuments(){
        Call<List<DocumentFixCar>> call = FixCarClient.getInstance().getApi().getDocuments(pref.getInt("userId",-1));
        call.enqueue(new Callback<List<DocumentFixCar>>() {
            @Override
            public void onResponse(Call<List<DocumentFixCar>> call, Response<List<DocumentFixCar>> response) {
                if (!response.isSuccessful()) {
                    Log.e("Code: ", String.valueOf(response.code()));
                    return;
                }
                List<DocumentFixCar> documents = response.body();
                docsData.clear();
                if (documents!=null){
                    for (DocumentFixCar document : documents){
                        if (document.getIdVehicle() == id){
                            docsData.add(new DocumentFixCar(document));
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<DocumentFixCar>> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**Se recoge le resultado del intent para guardar la imagen selecionada**/
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            selectedImage = data.getData();

            header.setImageURI(selectedImage);
            header.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        if (requestCode == RESULT_LOAD_DOCUMENT && resultCode == RESULT_OK && null != data) {
            selectedDocImg = data.getData();

            addDocIMG.setImageURI(selectedDocImg);
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

    private void uploadPicture(Uri fileUri){
        File file = new File(getRealPathFromURI(fileUri));
        MediaType contentType;
        RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)), file);

        Call<Void> call = FixCarClient.getInstance().getApi().uploadVehImage(id, requestFile);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Log.e("error", String.valueOf(response.code()));
                    return;
                }
                Log.d("exito","se ha subido la imagen");
                finish();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    private void uploadDocumentIMG(Uri fileUri){
        File file = new File(getRealPathFromURI(fileUri));
        MediaType contentType;
        RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)), file);

        Call<Void> call = FixCarClient.getInstance().getApi().uploadDocImage(docId, requestFile);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Log.e("error", String.valueOf(response.code()));
                    return;
                }
                Log.d("exito","se ha subido la imagen");
                selectedDocImg = null;
                docTypeET.setText("");
                docNotesET.setText("");
                addDocIMG.setImageResource(R.drawable.image_plus);
                getDocuments();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    private void uploadDocument(){
        Call<Integer> call = FixCarClient.getInstance().getApi().postDocument(docTypeET.getText().toString(),docNotesET.getText().toString(),String.valueOf(pref.getInt("userId",-1)),String.valueOf(id));
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (!response.isSuccessful()) {
                    Log.e("error", String.valueOf(response.code()));
                    return;
                }
                docId = response.body();
                if (selectedDocImg != null){
                    uploadDocumentIMG(selectedDocImg);
                }else{
                docTypeET.setText("");
                docNotesET.setText("");
                addDocIMG.setImageResource(R.drawable.image_plus);
                getDocuments();}
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
