package com.example.carnote.model;

/*
    model danych auta
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AutoData implements Serializable {

    // model auta np. golf
    private String model;

    // marka auta np. VolksWagen
    private String make;

    // color auta
    private String color;


    // kolekcja tankowań
    private List<TankUpRecord> tankUpRecord;



    public AutoData(String model, String make, String color) {
        this.model = model;
        this.make = make;
        this.color = color;
        tankUpRecord = new ArrayList<>();
    }

    public String getModel() {
        return model;
    }

    public String getMake() {
        return make;
    }

    public String getColor() {
        return color;
    }

    public List<TankUpRecord> getTankUpRecord() {
        return tankUpRecord;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setTankUpRecord(List<TankUpRecord> tankUpRecord) {
        this.tankUpRecord = tankUpRecord;
    }

    @Override
    public String toString() {
        return make + " " + model + " " + color;
    }
}
