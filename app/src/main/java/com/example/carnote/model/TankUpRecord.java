package com.example.carnote.model;

import java.io.Serializable;
import java.util.Date;

public class TankUpRecord implements Serializable {


    // data wykonaina tankowania
    private Date tankUpDate;

    // przebieg auta w momencie tankowania
    private Integer miallage;

    // dolane litry
    private Integer liters;

    // zapłacona kwota
    private Integer cost;

    private String colliedCarName;


    public TankUpRecord(Date tankUpDate, Integer miallage, Integer liters, Integer cost, String colliedCarName) {
        this.tankUpDate = tankUpDate;
        this.miallage = miallage;
        this.liters = liters;
        this.cost = cost;
        this.colliedCarName = colliedCarName;
    }

    public Date getTankUpDate() {
        return tankUpDate;
    }

    public Integer getMiallage() {
        return miallage;
    }

    public Integer getLiters() {
        return liters;
    }

    public Integer getCost() {
        return cost;
    }

    public String getColliedCarName() {return colliedCarName;}

    public void setTankUpDate(Date tankUpDate) {
        this.tankUpDate = tankUpDate;
    }

    public void setMiallage(Integer miallage) {
        this.miallage = miallage;
    }

    public void setLiters(Integer liters) {
        this.liters = liters;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }
}
