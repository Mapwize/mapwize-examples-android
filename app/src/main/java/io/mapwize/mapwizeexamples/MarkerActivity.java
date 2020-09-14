package io.mapwize.mapwizeexamples;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import io.mapwize.mapwizesdk.api.ApiCallback;
import io.mapwize.mapwizesdk.api.Placelist;
import io.mapwize.mapwizesdk.core.MapwizeConfiguration;
import io.mapwize.mapwizesdk.map.ClickEvent;
import io.mapwize.mapwizesdk.map.MapOptions;
import io.mapwize.mapwizesdk.map.MapwizeMap;
import io.mapwize.mapwizesdk.map.MapwizeView;

/**
 * This activity is about showing how you can place markers to different locations in your Venue.
 * We show also show you a fews methods to remove all markers by clicking on a button, or to remove a single marker by clicking on it.
 * We also illustrate how you can add custom markers to a placelist and display them all at once by clicking on a button.
 */

public class MarkerActivity extends AppCompatActivity {

    static final String MAPBOX_API_KEY = "pk.mapwize";
    static final String MAPWIZE_API_KEY = "a0b142dea96e9b630855199c8c32c993";
    static final String MAPWIZE_VENUE_ID = "56c2ea3402275a0b00fb00ac";
    static final String MAPWIZE_PLACELIST_ID = "5728a351a3a26c0b0027d5cf";

    MapwizeView mapwizeView;
    MapwizeMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_marker);
        Mapbox.getInstance(this, MAPBOX_API_KEY);
        FrameLayout container = findViewById(R.id.container);
        MapwizeConfiguration conf = new MapwizeConfiguration.Builder(this,
                MAPWIZE_API_KEY)
                .build();

        MapOptions options = new MapOptions.Builder()
                //Really usefull to load your map center on your desired Venue
                .centerOnVenue(MAPWIZE_VENUE_ID)
                .build();
        mapwizeView = new MapwizeView(getApplicationContext(), conf, options);

        Bitmap customIcon = BitmapUtils.getBitmapFromDrawable(getApplicationContext().getDrawable(R.drawable.ic_baseline_location_on_24));

        mapwizeView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));
        container.addView(mapwizeView);
        mapwizeView.onCreate(savedInstanceState);
        mapwizeView.getMapAsync(mapwizeMap -> {

            map = mapwizeMap;
            // add your custom icon to your map
            map.addImageToMap("customIcon1",customIcon);

            findViewById(R.id.removeMarkersButton).setVisibility(View.VISIBLE);
            findViewById(R.id.addCustomMarkersButton).setVisibility(View.VISIBLE);

            // Mapbox and Mapwize are fully loaded
            map.addOnClickListener(clickEvent ->  {
                switch(clickEvent.getEventType()){
                    case ClickEvent.VENUE_CLICK:
                        map.centerOnCoordinate(clickEvent.getLatLngFloor(),16,200);
                        break;
                    case ClickEvent.PLACE_CLICK:
                        // Add your marker on a place
                        map.addMarker(clickEvent.getPlacePreview());
                        break;
                    default:
                        // If not a place add a marker by coordinates
                        map.addMarker(clickEvent.getLatLngFloor());
                        break;
                }
            });
            //Add a marker listener to remove it
            map.addOnMarkerClickListener(marker -> map.removeMarker(marker));
        });
    }

    // Remove all the markers on the map
    public void removeMarkers(View v){
        map.removeMarkers();
    }

    // method a add custom marker to a specific Placelist
    public void addCustomMarkers(View v) {
        map.getMapwizeApi().getPlacelist(MAPWIZE_PLACELIST_ID, new ApiCallback<Placelist>(){

            @Override
            public void onSuccess(@NonNull Placelist placelist) {
                map.addMarker(placelist,"customIcon1", list -> {
                });
            }

            @Override
            public void onFailure(@NonNull Throwable throwable) {
                runOnUiThread(() -> {
                    showError(
                            "No Placelist found for Placelist number : "
                                    + MAPWIZE_PLACELIST_ID
                                    + ", be sure you entered the correct PLACELIST_ID "
                    );
                });
            }
        });
    }

    // Usefull to display a message in a Toast
    private void showError(String message) {
        Toast.makeText(
                getApplicationContext(),
                message,
                Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapwizeView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapwizeView.onResume();
    }

    @Override
    public void onPause() {
        mapwizeView.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        mapwizeView.onStop();
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);
        mapwizeView.onSaveInstanceState(saveInstanceState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapwizeView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        mapwizeView.onDestroy();
        super.onDestroy();
    }
}