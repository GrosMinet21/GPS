package com.example.english.gps;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.jjoe64.graphview.series.DataPoint;


public class GPS implements LocationListener{
    DataPoint[] data;
    int ite;
    long t = 0;
    Location l2;
    Location l;

    public GPS(){
        data = new DataPoint[30];
        for(int i = 0; i<30; i++)
            data[i] = new DataPoint(0, 0);
        ite = 0;
    }

    public void addLocation(Location l){
        if(ite == 29)
            ite = 0;
        l2 = l;
        DataPoint loc = new DataPoint(l2.getSpeed()*3600/1000, t/3600);
        data[ite] = loc;
        ite++;
    }

    public DataPoint[] getData(){return data;}

    public void onLocationChanged(Location location) {
        l = location;
    }

    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    public void onProviderEnabled(String s) {

    }

    public void onProviderDisabled(String s) {

    }
}
