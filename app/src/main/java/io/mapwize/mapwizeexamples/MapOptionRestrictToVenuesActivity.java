package io.mapwize.mapwizeexamples;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.mapbox.mapboxsdk.Mapbox;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import io.mapwize.mapwizesdk.core.MapwizeConfiguration;
import io.mapwize.mapwizesdk.map.MapOptions;
import io.mapwize.mapwizesdk.map.MapwizeView;

public class MapOptionRestrictToVenuesActivity extends AppCompatActivity {

    static final String MAPBOX_API_KEY = "pk.mapwize";
    static final String MAPWIZE_API_KEY = "YOUR_MAPWIZE_API_KEY";
    static final List<String> MAPWIZE_VENUE_IDS = Arrays.asList("SOME_VENUE_ID", "ANOTHER_VENUE_ID");

    MapwizeView mapwizeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_option_restrict_to_venues);
        Mapbox.getInstance(this, MAPBOX_API_KEY);
        FrameLayout container = findViewById(R.id.container);
        MapwizeConfiguration conf = new MapwizeConfiguration.Builder(this,
                MAPWIZE_API_KEY)
                .build();

        MapOptions options = new MapOptions.Builder()
                .restrictContentToVenueIds(MAPWIZE_VENUE_IDS)
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
            /*
                You can now do some stuff like adding places, markers, or anything else
            */
        });
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