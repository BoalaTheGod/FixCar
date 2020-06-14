package com.boala.fixcar;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Commentary {
    @SerializedName("idcommentary")
    private String idcomentary;
    @SerializedName("commentary")
    private String comentary;
    @SerializedName("idusers")
    private Integer iduser;
    @SerializedName("idworkshops")
    private Integer idworkshops;
    @SerializedName("response")
    private Integer response;
    @SerializedName("create_date")
    private Date create_date;
    @SerializedName("update_date")
    private Date update_date;

    public Commentary(String idcomentary, String comentary, Integer iduser, Integer idworkshops, Integer response, Date create_date, Date update_date) {
        this.idcomentary = idcomentary;
        this.comentary = comentary;
        this.iduser = iduser;
        this.idworkshops = idworkshops;
        this.response = response;
        this.create_date = create_date;
        this.update_date = update_date;
    }

    public String getIdcomentary() {
        return idcomentary;
    }

    public void setIdcomentary(String idcomentary) {
        this.idcomentary = idcomentary;
    }

    public String getComentary() {
        return comentary;
    }

    public void setComentary(String comentary) {
        this.comentary = comentary;
    }

    public Integer getIduser() {
        return iduser;
    }

    public void setIduser(Integer iduser) {
        this.iduser = iduser;
    }

    public Integer getIdworkshops() {
        return idworkshops;
    }

    public void setIdworkshops(Integer idworkshops) {
        this.idworkshops = idworkshops;
    }

    public Integer getResponse() {
        return response;
    }

    public void setResponse(Integer response) {
        this.response = response;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public Date getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(Date update_date) {
        this.update_date = update_date;
    }
}
