package io.mapwize.mapwizeexamples;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.mapbox.mapboxsdk.Mapbox;

import java.util.ArrayList;
import java.util.List;

import io.mapwize.mapwizesdk.api.Floor;
import io.mapwize.mapwizesdk.core.MapwizeConfiguration;
import io.mapwize.mapwizesdk.map.ClickEvent;
import io.mapwize.mapwizesdk.map.MapOptions;
import io.mapwize.mapwizesdk.map.MapwizeMap;
import io.mapwize.mapwizesdk.map.MapwizeView;

/**
 * Like many other Activities we will use SimpleMapActivity as starting point for this Activity.
 * Fell free to check it if you need to understand the basics
 * The purpose of this next Activity is to demonstrate how to handle many floors in a Venue.
 * To achieve this we will use Android RecyclerView Class in associatiation with our MapwizeApi (check the documentation for more details).
 * For this demonstration we will create an Adapter Class in the same file, but you should create a new file for the Adapter Class
 */

public class FloorControllerActivity extends AppCompatActivity {

    static final String MAPBOX_API_KEY = "pk.mapwize";
    static final String MAPWIZE_API_KEY = "YOUR_MAPWIZE_API_KEY";
    static final String MAPWIZE_VENUE_ID = "56c2ea3402275a0b00fb00ac";
    static final String MAPWIZE_PLACELIST_ID = "5728a351a3a26c0b0027d5cf";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    MapwizeView mapwizeView;
    MapwizeMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor_controller);
        Mapbox.getInstance(this, MAPBOX_API_KEY);
        FrameLayout container = findViewById(R.id.container);
        recyclerView = (RecyclerView) findViewById(R.id.floorController);

        MapwizeConfiguration conf = new MapwizeConfiguration.Builder(this,
                MAPWIZE_API_KEY)
                .build();

        MapOptions options = new MapOptions.Builder()
                //Really usefull to load your map center on your desired Venue
                .centerOnVenue(MAPWIZE_VENUE_ID)
                .build();

        mapwizeView = new MapwizeView(getApplicationContext(), conf, options);

        mapwizeView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));
        container.addView(mapwizeView);
        mapwizeView.onCreate(savedInstanceState);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new FloorAdapter();
        recyclerView.setAdapter(mAdapter);


        mapwizeView.getMapAsync(mapwizeMap -> {

            map = mapwizeMap;
            System.out.println(map.getFloors());

            //setFloors method 
            ((FloorAdapter) mAdapter).setFloors(map.getFloors());

        });
    }

    public static class FloorAdapter extends RecyclerView.Adapter<FloorAdapter.FloorViewHolder> {
        private List<Floor> floors = new ArrayList<>();
        Context context;

        // Provide a suitable constructor (depends on the kind of dataset)
        public FloorAdapter() {
        }

        public void setFloors(List<Floor> floors){
            this.floors = floors;
            notifyDataSetChanged();
        }


        @NonNull
        @Override
        public FloorAdapter.FloorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            this.context = parent.getContext();
            Button button = (Button) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.floor_button_view, parent, false);

            FloorViewHolder vh = new FloorViewHolder(button);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull FloorAdapter.FloorViewHolder holder, int position) {
            holder.floorButton.setText(floors.get(position).getName());

        }

        @Override
        public int getItemCount() {
            return this.floors.size();
        }

        public static class FloorViewHolder extends RecyclerView.ViewHolder {
            public Button floorButton;
            public FloorViewHolder(Button button) {
                super(button);
            }
        }
    }
}

