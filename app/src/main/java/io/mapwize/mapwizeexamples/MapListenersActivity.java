package io.mapwize.mapwizeexamples;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.mapbox.mapboxsdk.Mapbox;

import java.util.List;

import io.mapwize.mapwizesdk.api.DirectionMode;
import io.mapwize.mapwizesdk.api.Floor;
import io.mapwize.mapwizesdk.api.Universe;
import io.mapwize.mapwizesdk.api.Venue;
import io.mapwize.mapwizesdk.core.MapwizeConfiguration;
import io.mapwize.mapwizesdk.map.ClickEvent;
import io.mapwize.mapwizesdk.map.FollowUserMode;
import io.mapwize.mapwizesdk.map.MapOptions;
import io.mapwize.mapwizesdk.map.MapwizeMap;
import io.mapwize.mapwizesdk.map.MapwizeView;

public class MapListenersActivity extends AppCompatActivity {

    static final String MAPBOX_API_KEY = "pk.mapwize";
    static final String MAPWIZE_VENUE_ID = "YOUR_VENUE_ID";

    MapwizeView mapwizeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_listeners);
        FrameLayout container = findViewById(R.id.container);

        MapOptions options = new MapOptions.Builder()
                .centerOnVenue(MAPWIZE_VENUE_ID)
                .build();
        mapwizeView = new MapwizeView(getApplicationContext(), options);

        mapwizeView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));
        container.addView(mapwizeView);
        mapwizeView.onCreate(savedInstanceState);
        mapwizeView.getMapAsync(new MapwizeView.OnMapwizeReadyCallback() {
            @Override
            public void onMapwizeReady(MapwizeMap mapwizeMap) {
                addListeners(mapwizeMap);
            }
        });
    }

    private void addListeners(MapwizeMap mapwizeMap) {
        mapwizeMap.addOnClickListener(new MapwizeMap.OnClickListener() {
            @Override
            public void onClickEvent(@NonNull ClickEvent clickEvent) {
                switch (clickEvent.getEventType()) {
                    case ClickEvent.MAP_CLICK:
                        Log.i("MapListenersActivity", "MAP_CLICK_EVENT");
                        break;
                    case ClickEvent.PLACE_CLICK:
                        Log.i("MapListenersActivity", "PLACE_CLICK_EVENT");
                        break;
                    case ClickEvent.VENUE_CLICK:
                        Log.i("MapListenersActivity", "VENUE_CLICK_EVENT");

                }
            }
        });

        mapwizeMap.addOnVenueEnterListener(new MapwizeMap.OnVenueEnterListener() {
            @Override
            public void onVenueEnter(@NonNull Venue venue) {
                Log.i("MapListenersActivity", "OnVenueEnter " + venue.getName());
            }

            @Override
            public void onVenueWillEnter(@NonNull Venue venue) {
                Log.i("MapListenersActivity", "OnVenueWillEnter " + venue.getName());
            }

            /*
            SDK 3.3.0
            @Override
            public void onVenueEnterError(@NonNull Venue venue, @NonNull Throwable throwable) {
                Log.i("MapListenersActivity", "OnVenueEnterError " + venue.getName());
            }
            */
        });

        mapwizeMap.addOnVenueExitListener(new MapwizeMap.OnVenueExitListener() {
            @Override
            public void onVenueExit(@NonNull Venue venue) {
                Log.i("MapListenersActivity", "OnVenueExit " + venue.getName());
            }
        });

        mapwizeMap.addOnUniverseChangeListener(new MapwizeMap.OnUniverseChangeListener() {
            @Override
            public void onUniversesChange(@NonNull List<Universe> universes) {
                Log.i("MapListenersActivity", "OnUniversesChange " + universes);
            }

            @Override
            public void onUniverseWillChange(@NonNull Universe universe) {
                Log.i("MapListenersActivity", "OnUniverseWillChange " + universe.getName());
            }

            @Override
            public void onUniverseChange(@Nullable Universe universe) {
                Log.i("MapListenersActivity", "OnUniverseChange " + universe.getName());
            }

            /*
            SDK 3.3.0
            @Override
            public void onUniverseChangeError(@NonNull Universe universe, @NonNull Throwable throwable) {
                Log.i("MapListenersActivity", "OnUniverseChangeError " + universe.getName());
            }
             */
        });

        mapwizeMap.addOnFloorsChangeListener(new MapwizeMap.OnFloorsChangeListener() {
            @Override
            public void onFloorsChange(@NonNull List<Floor> floors) {
                Log.i("MapListenersActivity", "OnFloorsChange " + floors);
            }
        });

        mapwizeMap.addOnFloorChangeListener(new MapwizeMap.OnFloorChangeListener() {
            @Override
            public void onFloorWillChange(@Nullable Floor floor) {
                Log.i("MapListenersActivity", "OnFloorWillChange " + floor);
            }

            @Override
            public void onFloorChange(@Nullable Floor floor) {
                Log.i("MapListenersActivity", "OnFloorChange " + floor);
            }

            /*
            SDK 3.3.0
            @Override
            public void onFloorChangeError(@Nullable Floor floor, @NonNull Throwable throwable) {
                Log.i("MapListenersActivity", "OnFloorChangeError " + floor);
            }
             */
        });

        mapwizeMap.addOnFollowUserModeChangeListener(new MapwizeMap.OnFollowUserModeChangeListener() {
            @Override
            public void onFollowUserModeChange(@NonNull FollowUserMode followUserMode) {
                Log.i("MapListenersActivity", "OnFollowUserModeChange " + followUserMode);
            }
        });

        mapwizeMap.addOnLanguageChangeListener(new MapwizeMap.OnLanguageChangeListener() {
            @Override
            public void onLanguageChange(@NonNull String language) {
                Log.i("MapListenersActivity", "OnLanguageChange " + language);
            }
        });

        mapwizeMap.addOnDirectionModesChangeListener(new MapwizeMap.OnDirectionModesChangeListener() {
            @Override
            public void onDirectionModesChange(@NonNull List<DirectionMode> directionModes) {
                Log.i("MapListenersActivity", "OnDirectionModesChange " + directionModes);
            }
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
