package com.boala.fixcar;

public class WorkshopTypes {
    private int mechanics;
    private int repairs;
    private int electricity;
    private int bodywork;
    private int review;
    private int creditcard;

    public WorkshopTypes(int mechanics, int repairs, int electricity, int bodywork, int review, int creditcard) {
        this.mechanics = mechanics;
        this.repairs = repairs;
        this.electricity = electricity;
        this.bodywork = bodywork;
        this.review = review;
        this.creditcard = creditcard;
    }

    public int getMechanics() {
        return mechanics;
    }

    public void setMechanics(int mechanics) {
        this.mechanics = mechanics;
    }

    public int getRepairs() {
        return repairs;
    }

    public void setRepairs(int repairs) {
        this.repairs = repairs;
    }

    public int getElectricity() {
        return electricity;
    }

    public void setElectricity(int electricity) {
        this.electricity = electricity;
    }

    public int getBodywork() {
        return bodywork;
    }

    public void setBodywork(int bodywork) {
        this.bodywork = bodywork;
    }

    public int getReview() {
        return review;
    }

    public void setReview(int review) {
        this.review = review;
    }

    public int getCreditcard() {
        return creditcard;
    }

    public void setCreditcard(int creditcard) {
        this.creditcard = creditcard;
    }
    public String toString(){
        String result = "";
        if (repairs == 1){
            result+="Reparaciones,";
        }
        if (mechanics == 1){
            result+="Mecánica,";
        }
        if (electricity == 1){
            result+="Electricidad,";
        }
        if (bodywork == 1){
            result+="Chapa y pintura,";
        }
        if (review == 1){
            result+="Revisiones,";
        }
        if (creditcard == 1){
            result+="Pago con tarjeta,";
        }
        if (result.length()>0) {
            String coma = String.valueOf(result.charAt(result.length() - 1));
            if (coma.equals(",")) {
                result = result.substring(0, result.length() - 1);
            }
        }
        return result;
    }
}
