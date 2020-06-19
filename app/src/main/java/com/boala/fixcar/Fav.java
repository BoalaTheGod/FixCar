package com.boala.fixcar;

import com.google.gson.annotations.SerializedName;

public class Fav {
    private int idfavorites;
    @SerializedName("idsuser")
    private int iduser;
    @SerializedName("idsworkshop")
    private int idworkshop;

    public Fav(int idfavorites, int iduser, int idworkshop) {
        this.idfavorites = idfavorites;
        this.iduser = iduser;
        this.idworkshop = idworkshop;
    }

    public int getIdfavorites() {
        return idfavorites;
    }

    public void setIdfavorites(int idfavorites) {
        this.idfavorites = idfavorites;
    }

    public int getIduser() {
        return iduser;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }

    public int getIdworkshop() {
        return idworkshop;
    }

    public void setIdworkshop(int idworkshop) {
        this.idworkshop = idworkshop;
    }
}
