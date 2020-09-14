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


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.mapwize.mapwizesdk.api.ApiCallback;
import io.mapwize.mapwizesdk.api.ApiFilter;
import io.mapwize.mapwizesdk.api.Place;
import io.mapwize.mapwizesdk.api.Style;
import io.mapwize.mapwizesdk.api.Venue;
import io.mapwize.mapwizesdk.core.MapwizeConfiguration;
import io.mapwize.mapwizesdk.map.ClickEvent;
import io.mapwize.mapwizesdk.map.MapOptions;
import io.mapwize.mapwizesdk.map.MapwizeMap;
import io.mapwize.mapwizesdk.map.MapwizeView;
import io.mapwize.mapwizesdk.map.PreviewCallback;

/**
 * The goal of this Activity is to demonstrate how to override Places' style.
 * To achieve this you will have to use the Style class which you can use with many props (see the SDK documentation).
 * You also have to use the setPlaceStyle method which can accept multiple parameters.
 * Like other examples we will use the SimpleMapActivity as Starting point.
 * Note that you can get a place Id on Mapwize Studio or by making a MapwizeApi call.
 */

public class PlaceStyleActivity extends AppCompatActivity {

    static final String MAPBOX_API_KEY = "pk.mapwize";
    static final String MAPWIZE_API_KEY = "a0b142dea96e9b630855199c8c32c993";
    static final String MAPWIZE_VENUE_ID = "56c2ea3402275a0b00fb00ac";


    //Style attributes
    static final String PLACE_FILL_COLOR = "#800080";
    static final String PLACE_STROKE_COLOR = "#FFFF00";
    static final Double PLACE_FILL_OPACITY = 0.7;
    static final Double PLACE_STROKE_OPACITY = 1.0;
    static final String PLACE_TITLE = "Some Super Fancy Place Title";

    MapwizeView mapwizeView;
    MapwizeMap map;
    Style style;
    ApiFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_place_style);
        Mapbox.getInstance(this, MAPBOX_API_KEY);
        FrameLayout container = findViewById(R.id.container);
        MapwizeConfiguration conf = new MapwizeConfiguration.Builder(this,
                MAPWIZE_API_KEY)
                .build();

        MapOptions options = new MapOptions.Builder()
                //Really usefull to load your map centered on your desired Venue
                .centerOnVenue(MAPWIZE_VENUE_ID)
                .build();

        Bitmap customIcon = BitmapUtils.getBitmapFromDrawable(getApplicationContext().getDrawable(R.drawable.ic_baseline_add_comment_24));

        mapwizeView = new MapwizeView(getApplicationContext(), conf, options);
        mapwizeView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));
        container.addView(mapwizeView);
        mapwizeView.onCreate(savedInstanceState);

        mapwizeView.getMapAsync(mapwizeMap -> {

            map = mapwizeMap;

            map.addImageToMap("customIcon1",customIcon);

            findViewById(R.id.removePlaceStyle).setVisibility(View.VISIBLE);
            findViewById(R.id.addMultipleRandomPlaceStyle).setVisibility(View.VISIBLE);

            style = new Style.Builder()
                    .fillColor(PLACE_FILL_COLOR)
                    .strokeColor(PLACE_STROKE_COLOR)
                    .fillOpacity(PLACE_FILL_OPACITY)
                    .strokeOpacity(PLACE_STROKE_OPACITY)
                    .icon("customIcon1")
                    .title(PLACE_TITLE)
                    .build();

            // Mapbox and Mapwize are fully loaded
            map.addOnClickListener(clickEvent ->  {
                // Let us change style with a simple click on a place
                if (clickEvent.getEventType() == ClickEvent.PLACE_CLICK) {
                    //retrieve the full Object from Preview
                    clickEvent.getPlacePreview().getFullObjectAsync(new PreviewCallback<Place>() {
                        @Override
                        public void getObjectAsync(Place place) {
                            map.setPlaceStyle(place,style);
                        }

                        @Override
                        public void error(Throwable throwable) {
                            showError(
                                    "An error occured retrieving the place"
                            );
                        }
                    });
                }
            });

            map.addOnVenueEnterListener(new MapwizeMap.OnVenueEnterListener() {
                @Override
                public void onVenueEnter(@NonNull Venue venue) {
                    findViewById(R.id.removePlaceStyle).setClickable(true);
                    findViewById(R.id.addMultipleRandomPlaceStyle).setClickable(true);
                }

                @Override
                public void onVenueWillEnter(@NonNull Venue venue) {}
            });

            map.addOnVenueExitListener(new MapwizeMap.OnVenueExitListener() {
                @Override
                public void onVenueExit(@NonNull Venue venue) {
                    findViewById(R.id.removePlaceStyle).setClickable(false);
                    findViewById(R.id.addMultipleRandomPlaceStyle).setClickable(false);
                }
            });

        });
    }

    public void removePlaceStyle(View v){
        map.removePlaceStyles();
    }

    public void addRandomCustomPlaceStyle(View v){
        Map<Place, Style> styleByPlace = new HashMap<>();
        String MAPWIZE_UNIVERSE_ID = map.getUniverse().getId();

        filter = new ApiFilter.Builder()
                .venueId(MAPWIZE_VENUE_ID)
                .universeId(MAPWIZE_UNIVERSE_ID)
                .build();

        map.getMapwizeApi().getPlaces(filter, new ApiCallback<List<Place>>() {
            @Override
            public void onSuccess(@NonNull List<Place> places) {
                runOnUiThread(() -> {
                    for (Place place : places) {
                        styleByPlace.put(place, generateCustomStyle());
                    }
                    map.setPlaceStyles(styleByPlace);
                });
            }

            @Override
            public void onFailure(@NonNull Throwable throwable) {
                runOnUiThread(() -> {
                    showError(
                            "Something horrible happened"
                    );
                });
            }
        });
    }

    public Style generateCustomStyle(){
        Random obj = new Random();
        int rand_num = obj.nextInt(0xffffff + 1);

        // format it as hexadecimal string and print
        String colorCode = String.format("#%06x", rand_num);

        Double opacity = Math.random();

        Style randomStyle = new Style.Builder()
                .fillColor(colorCode)
                .fillOpacity(opacity)
                .strokeColor(colorCode)
                .strokeOpacity(opacity)
                .build();

        return randomStyle;
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