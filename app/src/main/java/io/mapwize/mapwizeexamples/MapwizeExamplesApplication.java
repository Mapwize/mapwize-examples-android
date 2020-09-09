package io.mapwize.mapwizeexamples;

import android.app.Application;

import com.mapbox.mapboxsdk.Mapbox;

import io.mapwize.mapwizesdk.core.MapwizeConfiguration;

/**
 * The MapwizeExamplesApplication class is used to instantiate a global Mapwize Configuration.
 * Global Mapwize Configuration allows you to instantiate one or more maps without having to pass
 * the mapwize configuration object to every instance of MapwizeMap and MapwizeApi.
 * However, even with a Global configuration, you can always use a specific configuration at any time.
 *
 * Every examples will use this the SimpleMapActivity which uses a custom configuration (for demo purpose)
 * We will only use this Global configuration for SimpleMapXMLActivity and MapUIActivity.
 */

public class MapwizeExamplesApplication extends Application {

    // The Mapbox api key (or Mapbox token) has to be provided to load Mapbox
    private final static String MAPBOX_API_KEY = "pk.mapwize";
    // The Mapwize api key has to be provided to communicate with the backend and fetch your data
    private final static String MAPWIZE_API_KEY = "YOUR_MAPWIZE_API_KEY";

    @Override
    public void onCreate() {
        super.onCreate();
        Mapbox.getInstance(this, MAPBOX_API_KEY);
        MapwizeConfiguration config = new MapwizeConfiguration.Builder(this, MAPWIZE_API_KEY)
                .build();
        MapwizeConfiguration.start(config);
    }

}
