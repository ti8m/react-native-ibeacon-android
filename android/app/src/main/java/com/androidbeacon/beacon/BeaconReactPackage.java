package com.androidbeacon.beacon;

import android.app.Activity;
import com.androidbeacon.MainActivity;
import com.estimote.sdk.BeaconManager;
import com.facebook.react.ReactActivity;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.ArrayList;
import java.util.List;

public class BeaconReactPackage implements ReactPackage {

    private final BeaconManager beaconManager;

    public BeaconReactPackage(Activity mainActivity) {
        beaconManager = new BeaconManager(mainActivity);
    }

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        List<NativeModule> modules = new ArrayList<>();
        modules.add(new RNBeacon(reactContext, this.beaconManager));
        return modules;
    }

    @Override
    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return new ArrayList<>();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactApplicationContext) {
        return new ArrayList<>();
    }
}