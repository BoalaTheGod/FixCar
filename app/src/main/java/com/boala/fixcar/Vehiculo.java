package com.boala.fixcar;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.Nullable;

public class Vehiculo {
    @SerializedName("idvehiculo")
    private int idVehiculo;
    @SerializedName("idusuario")
    private int idUsuario;
    @SerializedName("km_vehiculo")
    private int kmVehiculo;
    @SerializedName("itv_fecha")
    private Date fechaItv;
    @SerializedName("fecha_ruedas")
    private Date fechaRuedas;
    @SerializedName("fecha_aceite")
    private Date fechaAceite;
    @SerializedName("fecha_revision")
    private Date fechaRevision;
    private String modelo;
    private String marca;
    private String motor;
    private String seguro;
    private String color;
    private String matricula;
    private String imagen;


    public Vehiculo(int kmVehiculo, Date fechaItv, Date fechaRuedas, Date fechaAceite, Date fechaRevision, String modelo, String marca, String motor, String seguro, String color, String matricula, String imagen) {
        this.kmVehiculo = kmVehiculo;
        this.fechaItv = fechaItv;
        this.fechaRuedas = fechaRuedas;
        this.fechaAceite = fechaAceite;
        this.fechaRevision = fechaRevision;
        this.modelo = modelo;
        this.marca = marca;
        this.motor = motor;
        this.seguro = seguro;
        this.color = color;
        this.matricula = matricula;
        this.imagen = imagen;
    }

    public Vehiculo(Vehiculo vehiculo){
        this.idVehiculo = vehiculo.idVehiculo;
        this.idUsuario = vehiculo.idUsuario;
        this.kmVehiculo = vehiculo.kmVehiculo;
        this.fechaItv = vehiculo.fechaItv;
        this.fechaRuedas = vehiculo.fechaRuedas;
        this.fechaAceite = vehiculo.fechaAceite;
        this.fechaRevision = vehiculo.fechaRevision;
        this.modelo = vehiculo.modelo;
        this.marca = vehiculo.marca;
        this.motor = vehiculo.motor;
        this.seguro = vehiculo.seguro;
        this.color = vehiculo.color;
        this.matricula = vehiculo.matricula;
        this.imagen = vehiculo.imagen;
    }

    public Vehiculo(){
        idVehiculo = 0;
        this.kmVehiculo = 0;
        this.fechaItv = new Date();
        this.fechaRuedas = new Date();
        this.fechaAceite = new Date();
        this.fechaRevision = new Date();
        this.modelo = "";
        this.marca = "";
        this.motor = "";
        this.seguro = "";
        this.color = "";
        this.matricula = "";
        this.imagen = "";
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

    public int getKmVehiculo() {
        return kmVehiculo;
    }

    public void setKmVehiculo(int kmVehiculo) {
        this.kmVehiculo = kmVehiculo;
    }

    public Date getFechaItv() {
        return fechaItv;
    }

    public void setFechaItv(Date fechaItv) {
        this.fechaItv = fechaItv;
    }

    public Date getFechaRuedas() {
        return fechaRuedas;
    }

    public void setFechaRuedas(Date fechaRuedas) {
        this.fechaRuedas = fechaRuedas;
    }

    public Date getFechaAceite() {
        return fechaAceite;
    }

    public void setFechaAceite(Date fechaAceite) {
        this.fechaAceite = fechaAceite;
    }

    public Date getFechaRevision() {
        return fechaRevision;
    }

    public void setFechaRevision(Date fechaRevision) {
        this.fechaRevision = fechaRevision;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getMotor() {
        return motor;
    }

    public void setMotor(String motor) {
        this.motor = motor;
    }

    public String getSeguro() {
        return seguro;
    }

    public void setSeguro(String seguro) {
        this.seguro = seguro;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public static String dateToString(Date raw){
        String formatted = "";
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        formatted = formatter.format(raw);
        return formatted;
    }

    public static String dateToString2(Date raw){
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
        }catch (ParseException e){
            Log.e("error","parse error");
        }
        return raw;
    }

    @Override
    public String toString() {
        return "Vehiculo{" +
                "idVehiculo=" + idVehiculo +
                ", idUsuario=" + idUsuario +
                ", kmVehiculo=" + kmVehiculo +
                ", fechaItv=" + fechaItv +
                ", fechaRuedas=" + fechaRuedas +
                ", fechaAceite=" + fechaAceite +
                ", fechaRevision=" + fechaRevision +
                ", modelo='" + modelo + '\'' +
                ", marca='" + marca + '\'' +
                ", motor='" + motor + '\'' +
                ", seguro='" + seguro + '\'' +
                ", color='" + color + '\'' +
                ", matricula='" + matricula + '\'' +
                '}';
    }

    public boolean equals(@Nullable Vehiculo vehiculo) {
        return idVehiculo==vehiculo.idVehiculo;
    }
}
