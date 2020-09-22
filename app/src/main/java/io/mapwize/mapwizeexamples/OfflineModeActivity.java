package io.mapwize.mapwizeexamples;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.mapboxsdk.Mapbox;

import io.mapwize.mapwizesdk.api.ApiCallback;
import io.mapwize.mapwizesdk.api.MapwizeApi;
import io.mapwize.mapwizesdk.api.MapwizeApiFactory;
import io.mapwize.mapwizesdk.api.OfflineException;
import io.mapwize.mapwizesdk.api.OfflineManager;
import io.mapwize.mapwizesdk.api.OfflineRegion;
import io.mapwize.mapwizesdk.api.Universe;
import io.mapwize.mapwizesdk.api.Venue;
import io.mapwize.mapwizesdk.core.MapwizeConfiguration;
import io.mapwize.mapwizesdk.map.MapOptions;
import io.mapwize.mapwizesdk.map.MapwizeMap;
import io.mapwize.mapwizesdk.map.MapwizeView;

public class OfflineModeActivity extends AppCompatActivity {

    static final String MAPBOX_API_KEY = "pk.mapwize";
    static final String MAPWIZE_API_KEY = "";
    static final String MAPWIZE_VENUE_ID = "56c2ea3402275a0b00fb00ac";
    static final String MAPWIZE_UNIVERSE_ID = "57ec94f8098881c02bdc5e9f";

    static final String TAG = "OfflineModeActivity";

    MapwizeApi api;
    OfflineManager offlineManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_mode);
        Mapbox.getInstance(this, MAPBOX_API_KEY);
        FrameLayout container = findViewById(R.id.container);

        MapwizeConfiguration conf = new MapwizeConfiguration.Builder(this,
                MAPWIZE_API_KEY)
                .build();

        MapOptions options = new MapOptions.Builder()
                .build();

        MapwizeView mapwizeView = new MapwizeView(getApplicationContext(), conf, options);

        mapwizeView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));
        container.addView(mapwizeView);
        mapwizeView.onCreate(savedInstanceState);

        // first you need to retrieve the correct Universe and Venue from Mapwize Api
        api = MapwizeApiFactory.getApi(conf);
        offlineManager = new OfflineManager(conf);


            api.getVenue(MAPWIZE_VENUE_ID, new ApiCallback<Venue>() {
                @Override
                public void onSuccess(@NonNull Venue venue) {
                    api.getUniverse(MAPWIZE_UNIVERSE_ID, new ApiCallback<Universe>() {
                        @Override
                        public void onSuccess(@NonNull Universe universe) {
                            offlineManager.downloadData(venue, universe, new OfflineManager.DownloadTaskListener() {
                                @Override
                                public void onSuccess(OfflineRegion offlineRegion) {
                                    Log.i(TAG, "Data successfully retrieved");

                                    mapwizeView.getMapAsync(mapwizeMap -> {

                                        mapwizeMap.setUniverse(offlineRegion.getUniverse());
                                        mapwizeMap.centerOnVenue(offlineRegion.getVenue(), 1);
                                    });
                                }

                                @Override
                                public void onProgress(OfflineRegion offlineRegion, int i) {

                                    Log.i(TAG, "onProgress: Loading.....");
                                    while( i < 100) {

                                        ProgressBar progressBar = (ProgressBar) findViewById(R.id.pBar);
                                        progressBar.setVisibility(View.VISIBLE);
                                        container.addView(progressBar);
                                        TextView percentageView = findViewById(R.id.percentageView);

                                        percentageView.setText("progression: "+ i + "%");
                                        percentageView.setVisibility(View.VISIBLE);
                                        container.addView(percentageView);

                                    }

                                    findViewById(R.id.pBar).setVisibility(View.GONE);
                                    findViewById(R.id.percentageView).setVisibility(View.GONE);
                                }

                                @Override
                                public void onFailure(OfflineRegion offlineRegion, OfflineException e) {
                                    showError(
                                            "Something went wrong, check your network status"
                                    );
                                }
                            });
                        }

                        @Override
                        public void onFailure(@NonNull Throwable throwable) {
                            runOnUiThread(() -> {
                                showError(
                                        "Wrong Universe"
                                );
                            });
                        }
                    });
                }

                @Override
                public void onFailure(@NonNull Throwable throwable) {
                    runOnUiThread(() -> {
                        showError(
                                "Wrong Venue Id"
                        );
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
}