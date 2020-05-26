package com.boala.fixcar;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Usuario {
    private int idusuario;
    @SerializedName("telefono")
    private int phoneNumber;
    @SerializedName("nombre")
    private String name;
    private String password;
    @SerializedName("direccion")
    private String adress;
    @SerializedName("localidad")
    private String city;
    private String email;
    @SerializedName("imagen")
    private String image;
    @SerializedName("fecha")
    private Date date;

    public Usuario(int idusuario, int phoneNumber, int estado, String name, String password, String confirma, String adress, String city, String email, String image, Date date) {
        this.idusuario = idusuario;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.password = password;
        this.adress = adress;
        this.city = city;
        this.email = email;
        this.image = image;
        this.date = date;
    }

    public Usuario() {
        this.idusuario = 0;
        this.phoneNumber = 0;
        this.name = "";
        this.password = "";
        this.adress = "";
        this.city = "";
        this.email = "";
        this.image = "";
        this.date = Vehiculo.stringToDate("00/00/0000");
    }

    public int getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(int idusuario) {
        this.idusuario = idusuario;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idusuario=" + idusuario +
                ", telefono=" + phoneNumber +
                ", nombre='" + name + '\'' +
                ", password='" + password + '\'' +
                ", direccion='" + adress + '\'' +
                ", localidad='" + city + '\'' +
                ", email='" + email + '\'' +
                ", imagen='" + image + '\'' +
                ", fecha=" + date +
                '}';
    }
}
