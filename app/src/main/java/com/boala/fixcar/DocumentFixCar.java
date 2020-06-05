package com.boala.fixcar;

import com.google.gson.annotations.SerializedName;

public class DocumentFixCar {
    @SerializedName("type_document")
    private String type_document;
    @SerializedName("notes")
    private String notes;
    @SerializedName("idUser")
    private int idUser;
    @SerializedName("idVehicle")
    private int idVehicle;
    @SerializedName("iddocument")
    private int iddocuments;
    @SerializedName("documents")
    private String documents;

    public DocumentFixCar(String type_document, String notes, int idUser, int idVehicle, int iddocuments, String documents) {
        this.type_document = type_document;
        this.notes = notes;
        this.idUser = idUser;
        this.idVehicle = idVehicle;
        this.iddocuments = iddocuments;
        this.documents = documents;
    }

    public DocumentFixCar(DocumentFixCar doc) {
        this.type_document = doc.type_document;
        this.notes = doc.notes;
        this.idUser = doc.idUser;
        this.idVehicle = doc.idVehicle;
        this.iddocuments = doc.iddocuments;
        this.documents = doc.documents;
    }

    public String getType_document() {
        return type_document;
    }

    public void setType_document(String type_document) {
        this.type_document = type_document;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdVehicle() {
        return idVehicle;
    }

    public void setIdVehicle(int idVehicle) {
        this.idVehicle = idVehicle;
    }

    public int getIddocuments() {
        return iddocuments;
    }

    public void setIddocuments(int iddocuments) {
        this.iddocuments = iddocuments;
    }

    public String getDocuments() {
        return documents;
    }

    public void setDocuments(String documents) {
        this.documents = documents;
    }
}
