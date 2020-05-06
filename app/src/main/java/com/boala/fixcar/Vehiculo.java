package com.boala.fixcar;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Vehiculo {
    private int idVehiculo, idUsuario, kmVehiculo;
    private Date fechaItv, fechaRuedas, fechaAceite, fechaRevision;
    private String modelo, marca, motor, seguro, color, matricula;


    public Vehiculo(int kmVehiculo, Date fechaItv, Date fechaRuedas, Date fechaAceite, Date fechaRevision, String modelo, String marca, String motor, String seguro, String color, String matricula) {
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

    public static String dateToString(Date raw){
        String formatted = "";
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
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

}
