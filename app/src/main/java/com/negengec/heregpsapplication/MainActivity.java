package com.negengec.heregpsapplication;

import android.content.Context;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    protected int centerSwitcher = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final WebView wv = (WebView) findViewById(R.id.my_webview);
        WebSettings wvSettings = wv.getSettings();

        wvSettings.setLoadWithOverviewMode(true);
        wvSettings.setJavaScriptEnabled(true);
        wvSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        wvSettings.setAllowContentAccess(true);

        wvSettings.setUseWideViewPort(true);
        wvSettings.setLoadWithOverviewMode(true);

        wv.setPadding(0, 0, 0, 0);
        wv.setMinimumWidth(getDimension(true));
        wv.setMinimumHeight(getDimension(false));

        wv.loadUrl(getString(R.string.test_html));

        final TextView tvLongitude = (TextView) findViewById(R.id.longitude);
        final TextView tvLatitude = (TextView) findViewById(R.id.latitude);

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListenerOnCreate = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                // print the gps location on screen
                tvLongitude.setText("Longitude:" + location.getLongitude());
                tvLatitude.setText(" Latitude:" + location.getLatitude());

                // clear previous location marker and add the new location and send the center gps switcher
                wv.loadUrl("javascript:map.removeObject(liveLocation)");
                wv.loadUrl("javascript:callChangedLocation({lat:" + location.getLatitude() + ", lng:" + location.getLongitude() + "}," + String.valueOf(centerSwitcher) + ")");
                Log.v("centerSwitcherState:", String.valueOf(centerSwitcher));

                // set center switcher back to off
                if (centerSwitcher != 0) {
                    centerSwitcher = 0;
                }
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

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListenerOnCreate);


    }

    private int getDimension(Boolean widthSelect) {
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        if (widthSelect) {
            return width;
        } else {
            return height;
        }
    }

    public void onButtonClick(View v) {
        // set center switcher to center on gps location
        centerSwitcher = 1;
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
}
