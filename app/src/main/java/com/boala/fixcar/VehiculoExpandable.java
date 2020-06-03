package com.boala.fixcar;

import java.util.Date;

public class VehiculoExpandable extends Vehiculo {
    /**Clase para poder expandir las tarjetas de vehiculos**/
    private boolean expanded = false;

    public VehiculoExpandable(int idVehiculo, int idUsuario, int kmVehicle, Date itvDate, Date tiresDate, Date oilDate, Date revisionDate, String model, String brand, String engine, Date insuranceDate, String licencePlate, String image, String itv_note, String wheels_note, String oil_note, String review_note, String vehicle_note, boolean expanded) {
        super(idVehiculo, idUsuario, kmVehicle, itvDate, tiresDate, oilDate, revisionDate, model, brand, engine, insuranceDate, licencePlate, image, itv_note, wheels_note, oil_note, review_note, vehicle_note);
        this.expanded = false;
    }


    public VehiculoExpandable(Vehiculo vehiculo){
        super(vehiculo);
        expanded = false;

    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
