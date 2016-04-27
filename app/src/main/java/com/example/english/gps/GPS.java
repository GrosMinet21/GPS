package com.example.english.gps;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.jjoe64.graphview.series.DataPoint;


public class GPS{
    DataPoint[] data;
    int ite;
    long t = 0;
    Location l2;


    public GPS(){
        data = new DataPoint[30];
        for(int i = 0; i<30; i++)
            data[i] = new DataPoint(i, 0);
        ite = 0;
    }

    public void addLocation(){
        if(ite == 29)
            ite = 0;
        DataPoint loc = new DataPoint(t, l2.getSpeed()*3600/1000);
        data[ite] = loc;
        ite++;
    }

    public DataPoint[] getData(){return data;}
    public double getAverageSpeed(){
        double o = 0;
        double speed = 0;
        for(int i = 0; i<30; i++){
            if(data[i].getY()!=0){
                o++;
                speed += data[i].getY();
            }
        }
        return speed /o;
    }
}
