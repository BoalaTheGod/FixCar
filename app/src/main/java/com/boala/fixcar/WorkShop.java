package com.boala.fixcar;

import com.google.gson.annotations.SerializedName;

public class WorkShop {
    @SerializedName("idworkshop")
    private int idtaller;
    @SerializedName("name")
    private String name;
    private String cif;
    @SerializedName("adress")
    private String adress;
    @SerializedName("workshop_type")
    private String type;
    @SerializedName("state")
    private String state;
    private String email;
    @SerializedName("phone")
    private String phone;
    @SerializedName("description")
    private String description;
    @SerializedName("location")
    private String location;

    public WorkShop(int idtaller, String name, String cif, String adress, String type, String state, String email, String phone, String description, String location) {
        this.idtaller = idtaller;
        this.name = name;
        this.cif = cif;
        this.adress = adress;
        this.type = type;
        this.state = state;
        this.email = email;
        this.phone = phone;
        this.description = description;
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getIdtaller() {
        return idtaller;
    }

    public void setIdtaller(int idtaller) {
        this.idtaller = idtaller;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
