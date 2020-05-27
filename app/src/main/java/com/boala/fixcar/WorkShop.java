package com.boala.fixcar;

import com.google.gson.annotations.SerializedName;

public class WorkShop {
    private int idtaller;
    @SerializedName("nombre")
    private String name;
    private String cif;
    @SerializedName("direccion")
    private String adress;
    @SerializedName("tipo_taller")
    private String type;
    @SerializedName("estado")
    private String state;
    private String email;

    public WorkShop(int idtaller, String name, String cif, String adress, String type, String state, String email) {
        this.idtaller = idtaller;
        this.name = name;
        this.cif = cif;
        this.adress = adress;
        this.type = type;
        this.state = state;
        this.email = email;
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
}
