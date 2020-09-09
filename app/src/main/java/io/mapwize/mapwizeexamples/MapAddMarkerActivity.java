package io.mapwize.mapwizeexamples;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.mapbox.mapboxsdk.Mapbox;

import io.mapwize.mapwizesdk.api.Venue;
import io.mapwize.mapwizesdk.core.MapwizeConfiguration;
import io.mapwize.mapwizesdk.map.ClickEvent;
import io.mapwize.mapwizesdk.map.MapOptions;
import io.mapwize.mapwizesdk.map.MapwizeMap;
import io.mapwize.mapwizesdk.map.MapwizeView;

public class MapAddMarker extends AppCompatActivity {

    static final String MAPBOX_API_KEY = "pk.mapwize";
    static final String MAPWIZE_API_KEY = "a0b142dea96e9b630855199c8c32c993";
    static final String MAPWIZE_VENUE_ID = "56c2ea3402275a0b00fb00ac";

    MapwizeView mapwizeView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_add_marker);
        Mapbox.getInstance(this, MAPBOX_API_KEY);
        FrameLayout container = findViewById(R.id.container);
        MapwizeConfiguration conf = new MapwizeConfiguration.Builder(this,
                MAPWIZE_API_KEY)
                .build();
        MapOptions options = new MapOptions.Builder()
                .build();
        mapwizeView = new MapwizeView(getApplicationContext(), conf, options);

        mapwizeView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));
        container.addView(mapwizeView);
        mapwizeView.onCreate(savedInstanceState);
        mapwizeView.getMapAsync((mapwizeMap) -> {

            // Mapbox and Mapwize are fully loaded.

            mapwizeMap.addOnClickListener(event -> {
                // Check if we are not outside a Venue
                if (mapwizeMap.getVenue() != null) {
                    if (event.getEventType() == ClickEvent.VENUE_CLICK) {
                        mapwizeMap.centerOnVenue(event.getVenuePreview(), 300);
                        mapwizeMap.addMarker(event.getLatLngFloor());
                    }
                    else if (event.getEventType() == ClickEvent.PLACE_CLICK) {
                        mapwizeMap.addMarker(event.getLatLngFloor());
                        mapwizeMap.centerOnPlace(event.getPlacePreview(),400);
                        mapwizeMap.addPromotedPlace(event.getPlacePreview());
                    }
                }
                else {
                    Log.i("MapAddMarkerActivity", "outside a venue");
                }
            });
        });
    }
}