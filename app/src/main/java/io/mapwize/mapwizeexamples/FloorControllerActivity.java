package io.mapwize.mapwizeexamples;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;

import com.mapbox.mapboxsdk.Mapbox;

import java.util.ArrayList;
import java.util.List;

import io.mapwize.mapwizesdk.api.Floor;
import io.mapwize.mapwizesdk.core.MapwizeConfiguration;
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
    static final String MAPWIZE_API_KEY = "a0b142dea96e9b630855199c8c32c993";
    static final String MAPWIZE_VENUE_ID = "56c2ea3402275a0b00fb00ac";

    private RecyclerView recyclerView;
    private FloorAdapter mAdapter;

    MapwizeView mapwizeView;
    MapwizeMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor_controller);
        Mapbox.getInstance(this, MAPBOX_API_KEY);
        FrameLayout container = findViewById(R.id.container);
        recyclerView = findViewById(R.id.floorController);

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

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new FloorAdapter();
        recyclerView.setAdapter(mAdapter);


        mapwizeView.getMapAsync(mapwizeMap -> {

            map = mapwizeMap;

             mapwizeMap.addOnFloorsChangeListener(floors -> {
                 mAdapter.setFloors(floors);
                 map.addOnFloorChangeListener(new MapwizeMap.OnFloorChangeListener() {
                     @Override
                     public void onFloorWillChange(@Nullable Floor floor) {
                         Log.i("FloorControllerActivity", "Loading....");
                     }

                     @Override
                     public void onFloorChange(@Nullable Floor floor) {
                         runOnUiThread(() -> {
                            mAdapter.setSelectedFloor(floor);
                         });
                     }
                 });
            });
        });
    }

    public static class FloorAdapter extends RecyclerView.Adapter<FloorAdapter.FloorViewHolder> {
        private List<Floor> floors = new ArrayList<>();
        private Floor selectedFloor;
        private AdapterView.OnItemClickListener mlistener;

        // Provide a suitable constructor (depends on the kind of dataset)
        public FloorAdapter() {
        }

        public void setFloors(List<Floor> floors){
            this.floors = floors;
            notifyDataSetChanged();
        }

        public void setSelectedFloor(Floor floor){
            this.selectedFloor = floor;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public FloorAdapter.FloorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.floor_button_view, parent, false);
            return new FloorViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FloorAdapter.FloorViewHolder holder, int position) {
            holder.setFloor(floors.get(position));

            if(holder.floor.equals(selectedFloor)){
                holder.floorButton.setBackgroundColor(Color.parseColor("#4B0082"));
            }else {
                holder.floorButton.setBackgroundColor(Color.parseColor("#9932CC"));
            }
        }

        @Override
        public int getItemCount() {
            return this.floors.size();
        }



        public class FloorViewHolder extends RecyclerView.ViewHolder {
            private Button floorButton;
            private Floor floor;
            public FloorViewHolder(View view) {
                super(view);
                view.getContext();
                floorButton = view.findViewById(R.id.floorButton);
            }

            public void setFloor(Floor floor) {
                this.floor = floor;
                floorButton.setText(floor.getName());
                floorButton.setOnClickListener(v -> {
                    int adapterPostition = getAdapterPosition();
                    if( mlistener != null && adapterPostition != RecyclerView.NO_POSITION){
                        Floor nextFloor = floors.get(adapterPostition);

                    }
                });
            }
        }
    }
}

