# react-native-ibeacon-android

iBeacon support for React Native Android. I have developed this API as similar as possible to that of frostney: https://github.com/frostney/react-native-ibeacon

## Support
I’ve tested this module with Estimote beacons and it works. I don’t know if it works with other beacons, too. Beacons don't work in simulator. 

## Installation
The installation is a little bit complicated but we can do this step by step:

1.In AndroidManifest.xml add this:

``` 
...
<!-- Required to scan for and connect to Estimote Beacons via Bluetooth. -->
<uses-permission android:name="android.permission.BLUETOOTH"/>
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>

<!-- Required for BLE scanning on Android 6.0 and above. -->
<uses-permission-sdk-23 android:name="android.permission.ACCESS_COARSE_LOCATION"/>

<!-- Required to access Estimote Cloud. -->
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>

... 

```

2.In your `android/app/build.gradle` you have to add this dependency:
	

```
...
compile 'com.estimote:sdk:0.10.1@aar' // <----- add this
...
````
3.In your MainActivity you have to add the BeaconReactPackage:

```
...
@Override
protected List<ReactPackage> getPackages() {
    return Arrays.<ReactPackage>asList(
        new MainReactPackage(), new BeaconReactPackage(this) // <- Add this 
    );
}
...
```
4.In your app create a new package called „beacon“. There you have to add the classes `BeaconReactPackage.java` and `RNBeacon.java` from this github-repository. Please check the package in `BeaconReactPackage.java` and `RNBeacon.java`.

Now you can start building your react-native-app. You have to import „NativeModules“.

```
import React, {
	...
	NativeModules
}
var Beacons = NativeModules.RNBeacon;
...
```
You also have to set a region with an identifier and a uuid otherwise it won’t work searching for beacons.

```
var region = {
    identifier: 'Estimotes',
    uuid: 'B9407F30-F5F8-466E-AFF9-25556B57FE6D'
};
```
## Usage
You are able to use (for now) the following methods:


### Beacons.startBeaconSearch
```
...
var region = {
    identifier: 'Estimotes',
    uuid: 'B9407F30-F5F8-466E-AFF9-25556B57FE6D'
};
Beacons.startBeaconSearch(region);
...
```

Here you start searching for beacons. This methods calls the ranginglistener and also the statuslistener, which returns an event called „beaconsDidRange“. With this event you can get the beacon data in your react-native-app.

### Beacons.startMonitoringForRegion
```
...
var region = {
    identifier: 'Estimotes',
    uuid: 'B9407F30-F5F8-466E-AFF9-25556B57FE6D'
};
Beacons.startMonitoringForRegion(region);
...
```

This method starts the monitoring of beacons in your defined region.

### Beacons.stopMonitoringForRegion
```
...
var region = {
    identifier: 'Estimotes',
    uuid: 'B9407F30-F5F8-466E-AFF9-25556B57FE6D'
};
Beacons.stopMonitoringForRegion(region);
...
```

This method stops the monitoring of beacons in your defined region.

### Beacons.stopRangingForRegion
```
...
var region = {
    identifier: 'Estimotes',
    uuid: 'B9407F30-F5F8-466E-AFF9-25556B57FE6D'
};
Beacons.stopRangingForRegion(region);
...
```
This method stops the ranging of beacons in your defined region.

## Example-App
To use the app in this repository, you have to clone this repository. Then you are able to run the application on your (real) device.
