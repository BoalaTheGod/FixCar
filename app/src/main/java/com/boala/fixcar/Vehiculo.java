package com.boala.fixcar;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.Nullable;

public class Vehiculo {
    @SerializedName("idvehicle")
    private int idVehiculo;
    @SerializedName("uIdUser")
    private int idUsuario;
    @SerializedName("vehicle_km")
    private int kmVehicle;
    @SerializedName("date_itv")
    private Date itvDate;
    @SerializedName("wheels_date")
    private Date tiresDate;
    @SerializedName("oil_date")
    private Date oilDate;
    @SerializedName("review_date")
    private Date revisionDate;
    @SerializedName("vehicle_model")
    private String model;
    @SerializedName("vehicle_band")
    private String brand;
    @SerializedName("vehicle_engine")
    private String engine;
    @SerializedName("vehicle_insurance")
    private Date insuranceDate;
    @SerializedName("vehicle_registration")
    private String licencePlate;
    @SerializedName("image")
    private String image;
    @SerializedName("itv_note")
    private String itv_note;
    @SerializedName("wheels_note")
    private String wheels_note;
    @SerializedName("oil_note")
    private String oil_note;
    @SerializedName("review_note")
    private String review_note;
    @SerializedName("vehicle_note")
    private String vehicle_note;


    public Vehiculo(int idVehiculo, int idUsuario, int kmVehicle, Date itvDate, Date tiresDate, Date oilDate, Date revisionDate, String model, String brand, String engine, Date insuranceDate, String licencePlate, String image, String itv_note, String wheels_note, String oil_note, String review_note, String vehicle_note) {
        this.idVehiculo = idVehiculo;
        this.idUsuario = idUsuario;
        this.kmVehicle = kmVehicle;
        this.itvDate = itvDate;
        this.tiresDate = tiresDate;
        this.oilDate = oilDate;
        this.revisionDate = revisionDate;
        this.model = model;
        this.brand = brand;
        this.engine = engine;
        this.insuranceDate = insuranceDate;
        this.licencePlate = licencePlate;
        this.image = image;
        this.itv_note = itv_note;
        this.wheels_note = wheels_note;
        this.oil_note = oil_note;
        this.review_note = review_note;
        this.vehicle_note = vehicle_note;
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
        this.insuranceDate = vehiculo.insuranceDate;
        this.licencePlate = vehiculo.licencePlate;
        this.image = vehiculo.image;
        this.itv_note = vehiculo.itv_note;
        this.wheels_note = vehiculo.wheels_note;
        this.oil_note = vehiculo.oil_note;
        this.review_note = vehiculo.review_note;
        this.vehicle_note = vehiculo.vehicle_note;
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
        this.insuranceDate = new Date();
        this.licencePlate = "";
        this.image = "";
        this.itv_note = "";
        this.wheels_note = "";
        this.oil_note = "";
        this.review_note = "";
        this.vehicle_note = "";
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

    public Date getInsuranceDate() {
        return insuranceDate;
    }

    public void setInsuranceDate(Date insuranceDate) {
        this.insuranceDate = insuranceDate;
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

    public String getItv_note() {
        return itv_note;
    }

    public void setItv_note(String itv_note) {
        this.itv_note = itv_note;
    }

    public String getWheels_note() {
        return wheels_note;
    }

    public void setWheels_note(String wheels_note) {
        this.wheels_note = wheels_note;
    }

    public String getOil_note() {
        return oil_note;
    }

    public void setOil_note(String oil_note) {
        this.oil_note = oil_note;
    }

    public String getReview_note() {
        return review_note;
    }

    public void setReview_note(String review_note) {
        this.review_note = review_note;
    }

    public String getVehicle_note() {
        return vehicle_note;
    }

    public void setVehicle_note(String vehicle_note) {
        this.vehicle_note = vehicle_note;
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
                ", seguro='" + insuranceDate + '\'' +
                ", matricula='" + licencePlate + '\'' +
                '}';
    }

    public boolean equals(@Nullable Vehiculo vehiculo) {
        return idVehiculo == vehiculo.idVehiculo;
    }
}
