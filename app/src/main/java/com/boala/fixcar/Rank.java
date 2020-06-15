package com.boala.fixcar;

import com.google.gson.annotations.SerializedName;

public class Rank {
    @SerializedName("idranking")
    private int idranking;
    @SerializedName("ranking")
    private float ranking;
    @SerializedName("id_users")
    private int id_users;
    @SerializedName("id_workshops")
    private int id_workshops;

    public Rank(int idranking, float ranking, int id_users, int id_workshops) {
        this.idranking = idranking;
        this.ranking = ranking;
        this.id_users = id_users;
        this.id_workshops = id_workshops;
    }

    public int getIdranking() {
        return idranking;
    }

    public void setIdranking(int idranking) {
        this.idranking = idranking;
    }

    public float getRanking() {
        return ranking;
    }

    public void setRanking(float ranking) {
        this.ranking = ranking;
    }

    public int getId_users() {
        return id_users;
    }

    public void setId_users(int id_users) {
        this.id_users = id_users;
    }

    public int getId_workshops() {
        return id_workshops;
    }

    public void setId_workshops(int id_workshops) {
        this.id_workshops = id_workshops;
    }
}
