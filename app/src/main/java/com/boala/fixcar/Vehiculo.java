package com.boala.fixcar;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.Nullable;

public class Vehiculo {
    @SerializedName("idvehiculo")
    private int idVehiculo;
    @SerializedName("idusuario")
    private int idUsuario;
    @SerializedName("km_vehiculo")
    private int kmVehicle;
    @SerializedName("itv_fecha")
    private Date itvDate;
    @SerializedName("fecha_ruedas")
    private Date tiresDate;
    @SerializedName("fecha_aceite")
    private Date oilDate;
    @SerializedName("fecha_revision")
    private Date revisionDate;
    @SerializedName("modelo")
    private String model;
    @SerializedName("marca")
    private String brand;
    @SerializedName("motor")
    private String engine;
    @SerializedName("seguro")
    private Date ensuranceDate;
    @SerializedName("matricula")
    private String licencePlate;
    @SerializedName("imagen")
    private String image;


    public Vehiculo(int kmVehicle, Date itvDate, Date tiresDate, Date oilDate, Date revisionDate, String model, String brand, String engine, Date ensuranceDate, String licencePlate, String image) {
        this.kmVehicle = kmVehicle;
        this.itvDate = itvDate;
        this.tiresDate = tiresDate;
        this.oilDate = oilDate;
        this.revisionDate = revisionDate;
        this.model = model;
        this.brand = brand;
        this.engine = engine;
        this.ensuranceDate = ensuranceDate;
        this.licencePlate = licencePlate;
        this.image = image;
    }

    public Vehiculo(Vehiculo vehiculo) {
        this.idVehiculo = vehiculo.idVehiculo;
        this.idUsuario = vehiculo.idUsuario;
        this.kmVehicle = vehiculo.kmVehicle;
        this.itvDate = vehiculo.itvDate;
        this.tiresDate = vehiculo.tiresDate;
        this.oilDate = vehiculo.oilDate;
        this.revisionDate = vehiculo.revisionDate;
        this.model = vehiculo.model;
        this.brand = vehiculo.brand;
        this.engine = vehiculo.engine;
        this.ensuranceDate = vehiculo.ensuranceDate;
        this.licencePlate = vehiculo.licencePlate;
        this.image = vehiculo.image;
    }

    public Vehiculo() {
        idVehiculo = 0;
        this.kmVehicle = 0;
        this.itvDate = new Date();
        this.tiresDate = new Date();
        this.oilDate = new Date();
        this.revisionDate = new Date();
        this.model = "";
        this.brand = "";
        this.engine = "";
        this.ensuranceDate = new Date();
        this.licencePlate = "";
        this.image = "";
    }

    public int getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(int idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getKmVehicle() {
        return kmVehicle;
    }

    public void setKmVehicle(int kmVehicle) {
        this.kmVehicle = kmVehicle;
    }

    public Date getItvDate() {
        return itvDate;
    }

    public void setItvDate(Date itvDate) {
        this.itvDate = itvDate;
    }

    public Date getTiresDate() {
        return tiresDate;
    }

    public void setTiresDate(Date tiresDate) {
        this.tiresDate = tiresDate;
    }

    public Date getOilDate() {
        return oilDate;
    }

    public void setOilDate(Date oilDate) {
        this.oilDate = oilDate;
    }

    public Date getRevisionDate() {
        return revisionDate;
    }

    public void setRevisionDate(Date revisionDate) {
        this.revisionDate = revisionDate;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public Date getEnsuranceDate() {
        return ensuranceDate;
    }

    public void setEnsuranceDate(Date ensuranceDate) {
        this.ensuranceDate = ensuranceDate;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public static String dateToString(Date raw) {
        String formatted = "";
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        formatted = formatter.format(raw);
        return formatted;
    }

    public static String dateToString2(Date raw) {
        String formatted = "";
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        formatted = formatter.format(raw);
        return formatted;
    }

    public static Date stringToDate(String formatted) {
        Date raw = new Date();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            raw = formatter.parse(formatted);
        } catch (ParseException e) {
            Log.e("error", "parse error");
        }
        return raw;
    }

    @Override
    public String toString() {
        return "Vehiculo{" +
                "idVehiculo=" + idVehiculo +
                ", idUsuario=" + idUsuario +
                ", kmVehiculo=" + kmVehicle +
                ", fechaItv=" + itvDate +
                ", fechaRuedas=" + tiresDate +
                ", fechaAceite=" + oilDate +
                ", fechaRevision=" + revisionDate +
                ", modelo='" + model + '\'' +
                ", marca='" + brand + '\'' +
                ", motor='" + engine + '\'' +
                ", seguro='" + ensuranceDate + '\'' +
                ", matricula='" + licencePlate + '\'' +
                '}';
    }

    public boolean equals(@Nullable Vehiculo vehiculo) {
        return idVehiculo == vehiculo.idVehiculo;
    }
}
