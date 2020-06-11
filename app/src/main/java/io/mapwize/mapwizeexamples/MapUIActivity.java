package io.mapwize.mapwizeexamples;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import java.util.List;

import io.mapwize.mapwizesdk.api.Floor;
import io.mapwize.mapwizesdk.api.MapwizeObject;
import io.mapwize.mapwizesdk.map.MapOptions;
import io.mapwize.mapwizesdk.map.MapwizeMap;
import io.mapwize.mapwizeui.MapwizeFragment;

public class MapUIActivity extends AppCompatActivity implements MapwizeFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_ui);

        MapOptions opts = new MapOptions.Builder().build();
        MapwizeFragment frag = MapwizeFragment.newInstance(opts);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.container, frag);
        ft.commit();
    }

    @Override
    public void onMenuButtonClick() {

    }

    @Override
    public void onInformationButtonClick(MapwizeObject mapwizeObject) {

    }

    @Override
    public void onFragmentReady(MapwizeMap mapwizeMap) {

    }

    @Override
    public void onFollowUserButtonClickWithoutLocation() {

    }

    @Override
    public boolean shouldDisplayInformationButton(MapwizeObject mapwizeObject) {
        return false;
    }

    @Override
    public boolean shouldDisplayFloorController(List<Floor> floors) {
        return false;
    }
}
