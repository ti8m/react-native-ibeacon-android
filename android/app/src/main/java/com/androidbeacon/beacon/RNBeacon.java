package com.androidbeacon.beacon;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.support.annotation.Nullable;
import android.util.Log;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;
import com.estimote.sdk.repackaged.gson_v2_3_1.com.google.gson.Gson;
import com.facebook.react.ReactActivity;
import com.facebook.react.bridge.*;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.*;


public class RNBeacon extends ReactContextBaseJavaModule {
    private static final String BEACON = "BEACON";
    private ReactContext reactContext;
    private BluetoothAdapter bluetoothAdapter;
    private boolean isConnected = false;
    private BeaconManager beaconManager;

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    public RNBeacon(ReactApplicationContext reactContext, BeaconManager beaconManager) {
        super(reactContext);
        this.reactContext = reactContext;
        this.beaconManager = beaconManager;
    }

    @ReactMethod
    public void startBeaconSearch(ReadableMap map) {
        final Region region = convertMapToBeaconRegion(map);
        //set the foreground scan period
        beaconManager.setForegroundScanPeriod(5000, 5000);

        if (!isConnected) {
            //connect to BeaconService
            connectToBeaconService();
        }

        //start native ranging
        beaconManager.startRanging(region);

        //set the RangingListener
        setRangingListener();

        //set the StatusListener
        setScanStatusListener();

    }

    @ReactMethod
    public void startMonitoringForRegion(ReadableMap map) {
        final Region region = convertMapToBeaconRegion(map);

        if (!isConnected) {
            //connect to BeaconService
            connectToBeaconService();
        }

        //start native monitoring
        beaconManager.startMonitoring(region);
    }

    @ReactMethod
    public void stopRangingForRegion(ReadableMap map) {
        final Region region = convertMapToBeaconRegion(map);
        //stop native ranging
        beaconManager.stopRanging(region);

    }

    @ReactMethod
    public void stopMonitoringForRegion(ReadableMap map) {
        final Region region = convertMapToBeaconRegion(map);
        //stop native monitoring
        beaconManager.stopMonitoring(region);

    }

    private void connectToBeaconService() {
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                Log.v(BEACON, "service is ready");
                isConnected = true;
            }
        });
    }

    private Region convertMapToBeaconRegion(ReadableMap map) {
        if (!map.hasKey("minor")) {
            if (!map.hasKey("major")) {
                return createBeaconRegion(
                        map.getString("identifier"),
                        map.getString("uuid"),
                        null,
                        null);
            } else {
                return createBeaconRegion(
                        map.getString("identifier"),
                        map.getString("uuid"),
                        map.getInt("major"),
                        null);
            }
        } else {
            return createBeaconRegion(
                    map.getString("identifier"),
                    map.getString("uuid"),
                    map.getInt("major"),
                    map.getInt("minor"));
        }
    }

    private Region createBeaconRegion(String identifier, String proximityUUID, Integer major, Integer minor) {
        return new Region(identifier, UUID.fromString(proximityUUID), major, minor);
    }

    private void setRangingListener() {
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, final List<Beacon> beacons) {
                // Note that results are not delivered on UI thread.
                WritableMap params = Arguments.createMap();
                Log.v(BEACON, "Found amount: " + beacons.size());
                params.putString("beacons", new Gson().toJson(beacons));
                sendEvent(reactContext, "beaconsDidRange", params);
            }
        });
    }

    private void sendEvent(ReactContext reactContext,
                           String eventName,
                           @Nullable WritableMap params) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    private void setScanStatusListener() {
        beaconManager.setScanStatusListener(new BeaconManager.ScanStatusListener() {
            @Override
            public void onScanStart() {
                Log.v(BEACON, "start scan");
            }

            @Override
            public void onScanStop() {
                Log.v(BEACON, "stop scan");
            }
        });
    }

    private void disconnectBeaconManager() {
        beaconManager.disconnect();
    }
}

