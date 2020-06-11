# Mapwize Examples Android

This repository contains an Android application with multiple examples using the Mapwize SDK and the Mapwize UI libraries.
We will try to keep it updated as much as possible but it is possible that, sometimes, new SDK have been released without update of this repository.

## How to use

In order to run the application, you need to get a Mapwize api key from Mapwize Studio. If you don't have an account yet, sign up for free at [www.mapwize.io](https://www.mapwize.io).
Once you get this api key, you can use it in the SimpleMapActivity and in the MapwizeExamplesApplication class. The MapwizeExamplesApplication class creates a Global MapwizeConfiguration that will be used in every others activity except the SimpleMapActivity that shows how to create a map using a specific configuration.

## Examples

### SimpleMapActivity

Shows how to create a map programmatically.

### SimpleMapXMLActivity

Shows how to create a map through XML file.

### MapListenersActivity

Shows every listeners that you can add to the map and Log every event trigger by the map when you use it.

### MapUIActivity

Show how to integrate the MapwizeUI Fragment in your application
