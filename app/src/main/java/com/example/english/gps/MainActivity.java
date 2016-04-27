package com.example.english.gps;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {
    GraphView graph;
    GPS gps;
    boolean active = false;
    Timer t;
    long time;
    TimerTask task;
    TextView sp;
    TextView curr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.graph = graph;
        gps = new GPS();
        final Button button = (Button) findViewById(R.id.button);
        sp = (TextView) findViewById(R.id.sp);
        curr = (TextView) findViewById(R.id.curr);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                active = !active;
                if (active) {
                    t = new Timer();
                     task = new TimerTask() {
                        @Override
                        public void run() {
                            printGraph();
                        }
                    };
                    t.scheduleAtFixedRate(task, 0, 1000);
                    button.setText("Stop Tracking");
                }
                else {
                    gps = new GPS();
                    button.setText("Start Tracking");
                    task.cancel();
                }
            }
        });

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        try {
            gps.l2 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }catch(SecurityException e){

        }
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                gps.l2 = location;
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 0, locationListener);
        }catch(Exception e){

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void printGraph(){
        final GraphView graph = (GraphView) findViewById(R.id.graph);

        gps.t = time;
        time +=10;
        gps.addLocation();
        final LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(gps.getData());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                graph.addSeries(series);
                double speed = gps.getAverageSpeed();
                sp.setText("Average Speed : "+speed);
                double currSpeed = gps.l2.getSpeed()*3600/1000;
                curr.setText("Current Speed : "+ currSpeed);
            }
        });
    }
}
