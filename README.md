# Mapwize Examples Android

This repository contains an Android application with multiple examples using the Mapwize SDK and the Mapwize UI libraries.
We will try to keep it updated as much as possible but it is possible that, sometimes, new SDK have been released without update of this repository.

## How to use

In order to run the application, you need to get a Mapwize api key from Mapwize Studio. If you don't have an account yet, sign up for free at [www.mapwize.io](https://www.mapwize.io).
Once you get this api key, you can use it in the SimpleMapActivity and in the MapwizeExamplesApplication class. The MapwizeExamplesApplication class creates a Global MapwizeConfiguration that will be used only in SimpleMapXMLActivity and MapUIActivity. For the demo purpose we will use SimpleMapActivity as starting point for every other activity as it shows how to create a map using a specific configuration. You can also use it as a standalone to make your own map using our SDK.

## Overview 

[SimpleMapActivity](#simplemapactivity)

[SimpleMapXMLActivity](#simplemapxmlactivity)

[MapListenerActivity](#maplisteneractivity)

[MapUIActivity](#mapuiactivity)

[ShowDirectionActivity](#showdirectionactivity)


## Examples

### SimpleMapActivity

Shows how to create a map programmatically. This is the base example so we will keep it as simple as possible

** activity-simple_map.xml**
```
	<?xml version="1.0" encoding="utf-8"?>
	<androidx.constraintlayout.widget.ConstraintLayout
	    xmlns:android="http://schemas.android.com/apk/res/android"
	    xmlns:app="http://schemas.android.com/apk/res-auto"
	    xmlns:tools="http://schemas.android.com/tools"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    tools:context=".SimpleMapActivity">

	    <FrameLayout
		android:id="@+id/container"
		android:layout_width="match_parent"
		android:layout_height="match_parent"/>

	</androidx.constraintlayout.widget.ConstraintLayout>
```

In order to create a map with specific options, you need at least a Mapwize API key obtained for signin up for free at [www.mapwize.io](https://www.mapwize.io).

Once you get it you can now create your map with the minimum configuration.(see the full code [here](#https://github.com/Mapwize/mapwize-examples-android/blob/dev/app/src/main/java/io/mapwize/mapwizeexamples/SimpleMapActivity.java)

** SimpleMapActivity.java**
```
	public class SimpleMapActivity extends AppCompatActivity {

	    static final String MAPBOX_API_KEY = "pk.mapwize";
	    static final String MAPWIZE_API_KEY = "YOUR_MAPWIZE_API_KEY";
	    
	    MapwizeView mapwizeView;

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_simple_map);
		Mapbox.getInstance(this, MAPBOX_API_KEY);
		FrameLayout container = findViewById(R.id.container);
		
		MapwizeConfiguration conf = new MapwizeConfiguration.Builder(this,
		        MAPWIZE_API_KEY)
		        .build();

		MapOptions options = new MapOptions.Builder()
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
    	}
```
As you can see we need to instanciate a MapwizeConfiguration composed with the `MAPWIZE_API_KEY`. You can also add some options but we will cover this in another activity. 
You can now instanciate your MapwizeView and pass the `MapwizeConfiguration` and the `MapOptions` (if any).
Next you have to add your new Map to the Layout and call for the `getMapAsync()` method. Once the map is fully loaded you can know use our sdk to fit your needs.

### SimpleMapXMLActivity

Shows how to create a map through XML file.

### MapListenersActivity

Shows every listeners that you can add to the map and Log every event trigger by the map when you use it.

### MapUIActivity

Shows how to integrate the MapwizeUI Fragment in your application

### ShowDirectionActivity

Shows how to Search and Display a direction between two places
