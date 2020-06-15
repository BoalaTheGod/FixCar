package com.boala.fixcar;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

public class Commentary {
    @SerializedName("idcommentary")
    private int idcomentary;
    @SerializedName("commentary")
    private String comentary;
    @SerializedName("idusers")
    private int iduser;
    @SerializedName("idworkshops")
    private int idworkshops;
    @SerializedName("response")
    private int response;
    @SerializedName("create_date")
    private Date create_date;
    @SerializedName("update_date")
    private Date update_date;
    private ArrayList<Commentary> replyList;
    private Rank rank;


    public Commentary(int idcomentary, String comentary, int iduser, int idworkshops, int response, Date create_date, Date update_date, ArrayList<Commentary> replyList, Rank rank) {
        this.idcomentary = idcomentary;
        this.comentary = comentary;
        this.iduser = iduser;
        this.idworkshops = idworkshops;
        this.response = response;
        this.create_date = create_date;
        this.update_date = update_date;
        this.replyList = replyList;
        this.rank = rank;
    }


    public void addReply(Commentary commentary){
        replyList.add(commentary);
    }

    public ArrayList<Commentary> getReplyList() {
        return replyList;
    }

    public void setReplyList(ArrayList<Commentary> replyList) {
        this.replyList = replyList;
    }

    public int getIdcomentary() {
        return idcomentary;
    }

    public void setIdcomentary(int idcomentary) {
        this.idcomentary = idcomentary;
    }

    public String getComentary() {
        return comentary;
    }

    public void setComentary(String comentary) {
        this.comentary = comentary;
    }

    public int getIduser() {
        return iduser;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }

    public int getIdworkshops() {
        return idworkshops;
    }

    public void setIdworkshops(int idworkshops) {
        this.idworkshops = idworkshops;
    }

    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
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

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }
}
