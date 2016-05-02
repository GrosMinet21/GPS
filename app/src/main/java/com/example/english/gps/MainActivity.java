package com.example.english.gps;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
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
    long time = 0;
    TimerTask task;
    TextView sp;
    TextView curr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                    t.scheduleAtFixedRate(task, 0, 10000);
                    button.setText("Stop Tracking");
                }
                else {
                    task.cancel();
                    gps = new GPS();
                    button.setText("Start Tracking");
                }
            }
        });
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener ls = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                gps.l2 = location; //i can't get the location, it doesn't work i returns null and i really don't understand why.
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        try{
            lm.requestLocationUpdates(lm.GPS_PROVIDER,1,0,ls);
        } catch(SecurityException e) {
            Log.e("GPS", e.getMessage());
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
                graph.removeAllSeries();
                graph.addSeries(series);
                double speed = gps.getAverageSpeed();
                sp.setText("Average Speed : "+speed);
                double currSpeed = gps.l2.getSpeed();
                curr.setText("Current Speed : "+ currSpeed);
            }
        });
    }
}
