package com.boala.fixcar;

import java.util.Date;

public class VehiculoExpandable extends Vehiculo {
    /**Clase para poder expandir las tarjetas de vehiculos**/
    private boolean expanded = false;
    public VehiculoExpandable(int kmVehiculo, Date fechaItv, Date fechaRuedas, Date fechaAceite, Date fechaRevision, String modelo, String marca, String motor, String seguro, String color, String matricula, String imagen) {
        super(kmVehiculo, fechaItv, fechaRuedas, fechaAceite, fechaRevision, modelo, marca, motor, seguro, color, matricula, imagen);
        expanded = false;
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
