package com.example.adigyran.nevatest;

import java.util.Date;

/**
 * Created by adigyran on 31.05.2016.
 */
public class GPSPathpoint
{
    private int id;
    private Date pointdatetime;
    private double PLongitude;
    private double PLatitude;
    //private String title = "";
    //private String subtitle = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getPointdatetime() {
        return pointdatetime;
    }

    public void setPointdatetime(Date pointdatetime) {
        this.pointdatetime = pointdatetime;
    }

    public double getPLongitude() {
        return PLongitude;
    }

    public void setPLongitude(double PLongitude) {
        this.PLongitude = PLongitude;
    }

    public double getPLatitude() {
        return PLatitude;
    }

    public void setPLatitude(double PLatitude) {
        this.PLatitude = PLatitude;
    }

    public GPSPathpoint() {

    }


}