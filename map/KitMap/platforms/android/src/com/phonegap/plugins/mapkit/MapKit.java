package com.phonegap.plugins.mapkit;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.LOG;
import org.json.JSONArray;
import org.json.JSONException;

import android.widget.RelativeLayout;

public class MapKit extends CordovaPlugin {

    private String TAG = "MapKitPlugin";
    
    private Map_base mapImp = null;
 
    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        RelativeLayout main = new RelativeLayout(cordova.getActivity());
        
        if (hasHere()) {
        	mapImp = new Map_Here();
        }else { 
        	mapImp = new Map_Google();
        }
        
        mapImp.initialize(this,main);
       
    }
    
    public boolean execute(String action, JSONArray args,CallbackContext callbackContext) throws JSONException {
        
    	mapImp.setCallBack(callbackContext);
    	
        if (action.compareTo("showMap") == 0) {
        	mapImp.showMap(args.getJSONObject(0));
        } else if (action.compareTo("hideMap") == 0) {
        	mapImp.hideMap();
        } else if (action.compareTo("addMapPins") == 0) {
        	mapImp.addMapPins(args.getJSONArray(0));
        } else if (action.compareTo("clearMapPins") == 0) {
        	mapImp.clearMapPins();
        } else if( action.compareTo("changeMapType") == 0 ) {
        	mapImp.changeMapType(args.getJSONObject(0));	
        }
        LOG.d(TAG, action);

        return true;
    }
    
    private static final String HERE_LIBRARY = "com.here.android";

    private boolean hasHere() {
        String[] systemSharedLibraryNames = cordova.getActivity().getPackageManager().getSystemSharedLibraryNames();
        
        for (String library : systemSharedLibraryNames) {
            if (HERE_LIBRARY.equals(library)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void onPause(boolean multitasking) {
        LOG.d(TAG, "MapKitPlugin::onPause()");
        mapImp.onPause();
        super.onPause(multitasking);
    }

    @Override
    public void onResume(boolean multitasking) {
        LOG.d(TAG, "MapKitPlugin::onResume()");
        mapImp.onResume();
        super.onResume(multitasking);
    }

    @Override
    public void onDestroy() {
        LOG.d(TAG, "MapKitPlugin::onDestroy()");
        mapImp.onDestroy();
        super.onDestroy();
    }
}
