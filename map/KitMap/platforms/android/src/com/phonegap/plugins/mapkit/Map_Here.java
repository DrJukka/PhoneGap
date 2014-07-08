package com.phonegap.plugins.mapkit;


import org.apache.cordova.LOG;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;


import com.nokia.android.gms.maps.CameraUpdateFactory;
import com.nokia.android.gms.maps.GoogleMapOptions;
import com.nokia.android.gms.maps.MapView;
import com.nokia.android.gms.maps.MapsInitializer;
import com.nokia.android.gms.maps.model.LatLng;
import com.nokia.android.gms.maps.model.MarkerOptions;
import com.nokia.android.gms.maps.model.BitmapDescriptor;
import com.nokia.android.gms.maps.model.BitmapDescriptorFactory;

public class Map_Here  extends Map_base {

    private String TAG = "HERE Plugin";
    protected MapView mapView;
    
    public void showMap(final JSONObject options) {
        try {
        	plugin.cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    double latitude = 0, longitude = 0;
                    int height = 460;
                    boolean atBottom = false;
                    try {
                        height = options.getInt("height");
                        latitude = options.getDouble("lat");
                        longitude = options.getDouble("lon");
                        atBottom = options.getBoolean("atBottom");
                    } catch (JSONException e) {
                        LOG.e(TAG, "Error reading options");
                    }

                    
                    mapView = new MapView(plugin.cordova.getActivity(),new GoogleMapOptions());
                    root = (ViewGroup) plugin.webView.getParent();
                    root.removeView(plugin.webView);
                    main.addView(plugin.webView);

                    plugin.cordova.getActivity().setContentView(main);

                    MapsInitializer.initialize(plugin.cordova.getActivity());

                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, height);
                    if (atBottom) {
                    	params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
                    } else {
                    	params.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);
                    }
                    params.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);

                    mapView.setLayoutParams(params);
                    mapView.onCreate(null);
                    mapView.onResume(); // FIXME: I wish there was a better way
                                            // than this...
                    main.addView(mapView);

                    // Moving the map to lot, lon
                    mapView.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));
                    cCtx.success();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            cCtx.error("MapKitPlugin::showMap(): An exception occured");
        }
    }

    public void hideMap() {
        try {
        	plugin.cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mapView != null) {
                        mapView.onDestroy();
                        main.removeView(plugin.webView);
                        main.removeView(mapView);
                        root.addView(plugin.webView);
                        plugin.cordova.getActivity().setContentView(root);
                        mapView = null;
                        cCtx.success();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            cCtx.error("MapKitPlugin::hideMap(): An exception occured");
        }
    }

    public void addMapPins(final JSONArray pins) {
        try {
        	plugin.cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mapView != null) {
                        try {
                            for (int i = 0, j = pins.length(); i < j; i++) {
                                double latitude = 0, longitude = 0;
                                JSONObject options = pins.getJSONObject(i);
                                latitude = options.getDouble("lat");
                                longitude = options.getDouble("lon");

                                MarkerOptions mOptions = new MarkerOptions();

                                mOptions.position(new LatLng(latitude,
                                                             longitude));
                                if(options.has("title")) {
                                    mOptions.title(options.getString("title"));
                                }
                                if(options.has("snippet")) {
                                    mOptions.snippet(options.getString("snippet"));
                                }
                                if(options.has("icon")) {
                                    BitmapDescriptor bDesc = getBitmapDescriptor(options);
                                    if(bDesc != null) {
                                      mOptions.icon(bDesc);
                                    }
                                }

                                // adding Marker
                                // This is to prevent non existing asset resources to crash the app
                                try {
                                    mapView.getMap().addMarker(mOptions);
                                } catch(NullPointerException e) {
                                    LOG.e(TAG, "An error occurred when adding the marker. Check if icon exists");
                                }
                            }
                            cCtx.success();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            LOG.e(TAG, "An error occurred while reading pins");
                            cCtx.error("An error occurred while reading pins");
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            cCtx.error("MapKitPlugin::addMapPins(): An exception occured");
        }
    }

    public void clearMapPins() {
        try {
        	plugin.cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mapView != null) {
                        mapView.getMap().clear();
                        cCtx.success();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            cCtx.error("MapKitPlugin::clearMapPins(): An exception occured");
        }
    }

    public void changeMapType(final JSONObject options) {
        try{
        	plugin.cordova.getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if( mapView != null ) {
                        int mapType = 0;
                        try {
                            mapType = options.getInt("mapType");
                        } catch (JSONException e) {
                            LOG.e(TAG, "Error reading options");
                        }

                        //Don't want to set the map type if it's the same
                        if(mapView.getMap().getMapType() != mapType) {
                            mapView.getMap().setMapType(mapType);
                        }
                    }

                    cCtx.success();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            cCtx.error("MapKitPlugin::changeMapType(): An exception occured ");
        }
    }
    
    private BitmapDescriptor getBitmapDescriptor( final JSONObject iconOption ) {
        try {
            Object o = iconOption.get("icon");
            String type = null, resource = null;
            if( o.getClass().getName().equals("org.json.JSONObject" ) ) {
                JSONObject icon = (JSONObject)o;
                if(icon.has("type") && icon.has("resource")) {
                    type = icon.getString("type");
                    resource = icon.getString("resource");
                    if(type.equals("asset")) {
                        return BitmapDescriptorFactory.fromAsset(resource);
                    }
                }
            } else {
                //this is a simple change in the icon's color
                return BitmapDescriptorFactory.defaultMarker(Float.parseFloat(o.toString()));
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }
    public void onPause() {
        if (mapView != null) {
            mapView.onPause();
        }
    }

    public void onResume() {
        if (mapView != null) {
            mapView.onResume();
        }
    }

    public void onDestroy() {
        if (mapView != null) {
            mapView.onDestroy();
        }
    }
}
