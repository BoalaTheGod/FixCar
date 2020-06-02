package com.boala.fixcar;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Usuario {
    @SerializedName("iduser")
    private int idusuario;
    @SerializedName("phone")
    private String phoneNumber;
    @SerializedName("name")
    private String name;
    @SerializedName("pass")
    private String password;
    @SerializedName("adress")
    private String adress;
    @SerializedName("location")
    private String city;
    private String email;
    @SerializedName("image")
    private String image;
    @SerializedName("birth_date")
    private Date date;
    @SerializedName("state")
    private int state;

    public Usuario(int idusuario, String phoneNumber, int estado, String name, String password, String confirma, String adress, String city, String email, String image, Date date) {
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
        this.phoneNumber = "";
        this.name = "";
        this.password = "";
        this.adress = "";
        this.city = "";
        this.email = "";
        this.image = "";
        this.date = Vehiculo.stringToDate("00/00/0000");
    }

    public Usuario(Usuario body) {
        this.idusuario = body.idusuario;
        this.phoneNumber = body.phoneNumber;
        this.name = body.name;
        this.password = body.password;
        this.adress = body.adress;
        this.city = body.city;
        this.email = body.email;
        this.image = body.image;
        this.date = body.date;
        this.state = body.state;
    }

    public int getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(int idusuario) {
        this.idusuario = idusuario;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
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
