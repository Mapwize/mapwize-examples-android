package io.mapwize.mapwizeexamples;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.mapbox.mapboxsdk.Mapbox;

import java.util.List;

import io.mapwize.mapwizesdk.api.ApiCallback;
import io.mapwize.mapwizesdk.api.Place;
import io.mapwize.mapwizesdk.api.Placelist;
import io.mapwize.mapwizesdk.api.Style;
import io.mapwize.mapwizesdk.core.MapwizeConfiguration;
import io.mapwize.mapwizesdk.map.ClickEvent;
import io.mapwize.mapwizesdk.map.MapOptions;
import io.mapwize.mapwizesdk.map.MapwizeMap;
import io.mapwize.mapwizesdk.map.MapwizeView;
import io.mapwize.mapwizesdk.map.Marker;
import io.mapwize.mapwizesdk.map.PreviewCallback;

/**
 * The goal of this Activity is to demonstrate how to override Places' style.
 * To achieve this you will have to use the Style class which you can use with many props (see the SDK documentation).
 * You also have to use the setPlaceStyle method which can accept multiple parameters.
 * Like other examples we will use the SimpleMapActivity as Starting point.
 * Note that you can get a place Id on Mapwize Studio or by making a MapwizeApi call.
 */

public class MapSetPlaceStyleActivity extends AppCompatActivity {

    static final String MAPBOX_API_KEY = "pk.mapwize";
    static final String MAPWIZE_API_KEY = "YOUR_MAPWIZE_API_KEY";
    static final String MAPWIZE_VENUE_ID = "SOME_VENUE_ID";

    //Retrive your places IDs
    static final String MAPWIZE_PLACE_ID = "5ae73a842373e90013ac4f65";
    static final String MAPWIZE_PLACE_ID2 = "56c3454002275a0b00fb00c7";
    static final String MAPWIZE_PLACE_ID3 = "56c3426202275a0b00fb00b9";
    static final String MAPWIZE_PLACE_ID4 = "5de914f545bb62001610a4b8";

    //Style attributes
    static final String PLACE_FILL_COLOR = "#800080";
    static final String PLACE_STROKE_COLOR = "#FFFF00";
    static final Double PLACE_FILL_OPACITY = 0.7;
    static final String PLACE_TITLE = "Some Super Fancy Title";

    MapwizeView mapwizeView;
    MapwizeMap map;
    Style style;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_set_place_style);
        Mapbox.getInstance(this, MAPBOX_API_KEY);
        FrameLayout container = findViewById(R.id.container);
        MapwizeConfiguration conf = new MapwizeConfiguration.Builder(this,
                MAPWIZE_API_KEY)
                .build();

        MapOptions options = new MapOptions.Builder()
                //Really usefull to load your map centered on your desired Venue
                .centerOnVenue(MAPWIZE_VENUE_ID)
                .build();



        mapwizeView = new MapwizeView(getApplicationContext(), conf, options);
        mapwizeView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));
        container.addView(mapwizeView);
        mapwizeView.onCreate(savedInstanceState);


        //Instanciate your style
        style = new Style.Builder()
                .fillColor(PLACE_FILL_COLOR)
                .strokeColor(PLACE_STROKE_COLOR)
                .fillOpacity(PLACE_FILL_OPACITY)
                .title(PLACE_TITLE)
                .build();


        mapwizeView.getMapAsync(mapwizeMap -> {

            map = mapwizeMap;

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