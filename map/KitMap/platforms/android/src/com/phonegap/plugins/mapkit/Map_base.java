package com.phonegap.plugins.mapkit;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONObject;


import android.view.ViewGroup;
import android.widget.RelativeLayout;


public abstract class Map_base  {

    protected ViewGroup root; // original Cordova layout
    protected RelativeLayout main; // new layout to support map
    
    protected CallbackContext cCtx;
    protected CordovaPlugin plugin;
    
    public void initialize(CordovaPlugin cordovaplugin, RelativeLayout mainLayout) {
    	plugin = cordovaplugin;
    	main = mainLayout;
    }
    
    public void setCallBack(CallbackContext callbackContext) {
    	cCtx = callbackContext;
    } 
    
    abstract void showMap(final JSONObject options);
    abstract void hideMap();
    abstract void addMapPins(final JSONArray pins);
    abstract void clearMapPins() ;
    abstract void changeMapType(final JSONObject options);

    
    abstract void onPause();
    abstract void onResume();
    abstract void onDestroy();
}
