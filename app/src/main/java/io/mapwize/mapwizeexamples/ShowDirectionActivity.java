package io.mapwize.mapwizeexamples;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.mapbox.mapboxsdk.Mapbox;

import java.util.List;

import io.mapwize.mapwizesdk.api.ApiCallback;
import io.mapwize.mapwizesdk.api.Direction;
import io.mapwize.mapwizesdk.api.DirectionMode;
import io.mapwize.mapwizesdk.api.Place;
import io.mapwize.mapwizesdk.core.MapwizeConfiguration;
import io.mapwize.mapwizesdk.map.ClickEvent;
import io.mapwize.mapwizesdk.map.MapOptions;
import io.mapwize.mapwizesdk.map.MapwizeMap;
import io.mapwize.mapwizesdk.map.MapwizeView;

public class ShowDirectionActivity extends AppCompatActivity {

    static final String MAPBOX_API_KEY = "pk.mapwize";
    static final String MAPWIZE_API_KEY = "YOUR_MAPWIZE_API_KEY";
    static final String MAPWIZE_VENUE_ID = "YOU_VENUE_ID";
    static final String FROM_DIRECTION_POINT_ID = "YOUR_STARTING_POINT";
    static final String TO_DIRECTION_POINT_ID = "YOUR_DESTINATION";

    MapwizeView mapwizeView;
    MapwizeMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_direction);
        Mapbox.getInstance(this, MAPBOX_API_KEY);
        FrameLayout container = findViewById(R.id.container);

        MapwizeConfiguration conf = new MapwizeConfiguration.Builder(this,
                MAPWIZE_API_KEY)
                .build();

        MapOptions options = new MapOptions.Builder()
                .centerOnVenue(MAPWIZE_VENUE_ID)
                .build();

        mapwizeView = new MapwizeView(getApplicationContext(), conf, options);

        mapwizeView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));
        container.addView(mapwizeView);
        mapwizeView.onCreate(savedInstanceState);
        mapwizeView.getMapAsync(new MapwizeView.OnMapwizeReadyCallback() {
            @Override
            public void onMapwizeReady(MapwizeMap mapwizeMap) {
                map = mapwizeMap;
                findViewById(R.id.startDirectionButton).setVisibility(View.VISIBLE);
            }
        });
    }

    public void startDirection(View v) {
        if (map.getVenue() == null) {
           showError("Your are not in a venue, please enter a venue before starting a direction");
            return;
        }
        List<DirectionMode> availableDirectionModes = map.getDirectionModes();
        if (availableDirectionModes.isEmpty()) {
            showError("No direction modes are available. Check your configuration on Mapwize Studio");
            return;
        }
        map.getMapwizeApi().getPlace(FROM_DIRECTION_POINT_ID, new ApiCallback<Place>() {
            @Override
            public void onSuccess(@NonNull Place from) {
                map.getMapwizeApi().getPlace(TO_DIRECTION_POINT_ID, new ApiCallback<Place>() {
                    @Override
                    public void onSuccess(@NonNull Place to) {
                        map.getMapwizeApi().getDirection(from, to, availableDirectionModes.get(0), new ApiCallback<Direction>() {
                            @Override
                            public void onSuccess(@NonNull Direction direction) {
                                new Handler(Looper.getMainLooper()).post(() -> {
                                    map.setDirection(direction);
                                });
                            }

                            @Override
                            public void onFailure(@NonNull Throwable throwable) {
                                showError(
                                        "No direction has been found between "
                                                + FROM_DIRECTION_POINT_ID
                                                + " and "
                                                + TO_DIRECTION_POINT_ID
                                );
                            }
                        });
                    }

                    @Override
                    public void onFailure(@NonNull Throwable throwable) {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            showError("TO_DIRECTION_POINT_ID not found. Make sure the ID is correctly set");
                        });
                    }
                });
            }

            @Override
            public void onFailure(@NonNull Throwable throwable) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    showError("FROM_DIRECTION_POINT_ID not found. Make sure the ID is correctly set");
                });
            }
        });
    }

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
