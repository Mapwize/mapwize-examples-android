package io.mapwize.mapwizeexamples;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void createSimpleMap(View v) {
        Intent intent = new Intent(this, SimpleMapActivity.class);
        startActivity(intent);
    }

    public void createSimpleMapXML(View v) {
        Intent intent = new Intent(this, SimpleMapXMLActivity.class);
        startActivity(intent);
    }

    public void mapListenersImplementation(View v) {
        Intent intent = new Intent(this, MapListenersActivity.class);
        startActivity(intent);
    }

    public void mapUIIntegration(View v) {
        Intent intent = new Intent(this, MapUIActivity.class);
        startActivity(intent);
    }

    public void showDirection(View v) {
        Intent intent = new Intent(this, ShowDirectionActivity.class);
        startActivity(intent);
    }

    public void addMarkerToMap(View v) {
        Intent intent = new Intent(this, MapAddMarkerActivity.class);
        startActivity(intent);
    }
}
