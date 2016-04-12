'use strict';
import React, {
    AppRegistry,
    Component,
    StyleSheet,
    Text,
    ListView,
    View,
    NativeModules,
    DeviceEventEmitter,
    TouchableHighlight
} from 'react-native';

var Beacons = NativeModules.RNBeacon;

// Define a region which can be identifier + uuid, 
// identifier + uuid + major or identifier + uuid + major + minor
// (minor and major properties are numbers)
var region = {
    identifier: 'Estimotes',
    uuid: 'B9407F30-F5F8-466E-AFF9-25556B57FE6D'
};

// Create our dataSource which will be displayed in the data list
var ds = new ListView.DataSource({ rowHasChanged: (r1, r2) => r1 !== r2 });

// The BeaconList component listens for changes and re-renders the 
// rows (BeaconView components) in that case
var androidbeacon = React.createClass({
    getInitialState() {
        return {
            dataSource: ds.cloneWithRows([]),
        };
    },

    componentWillMount() {
        // Listen for beacon changes
        var subscription = DeviceEventEmitter.addListener(
            'beaconsDidRange',
            (data) => {     
                // Set the dataSource state with the whole beacon data
                // We will be rendering all of it throug <BeaconView />
                this.setState({
                    dataSource: ds.cloneWithRows(JSON.parse(data.beacons)),    
                });
            }
        );
        //start monitoring for beacons in region
        Beacons.startMonitoringForRegion(region);
    },

    renderRow(rowData) {     
        return (
            <View style={styles.row}>
                <Text style={styles.smallText}>UUID: {rowData.proximityUUID}</Text>
                <Text style={styles.smallText}>Major: {rowData.major}</Text>
                <Text style={styles.smallText}>Minor: {rowData.minor}</Text>
                <Text style={styles.smallText}>RSSI: {rowData.rssi}</Text>
            </View>
        )
    },

    render() {
        return (
            <View style={styles.container}>
                <View style={styles.header}>
                    <Text style={styles.headline}>All beacons in the area</Text>
                </View>
                <ListView
                    dataSource={this.state.dataSource}
                    renderRow={this.renderRow}
                />
                <View style={styles.footer}>
                    <TouchableHighlight onPress={this.start.bind(null, this) }>
                        <Text>Search Beacons</Text>
                    </TouchableHighlight>
                    <TouchableHighlight onPress={this.stop.bind(null, this) }>
                        <Text>Stop </Text>
                    </TouchableHighlight>
                </View>

            </View>
        );
    },
    start() {
        Beacons.startBeaconSearch(region);
    },
    stop() {
        Beacons.stopMonitoringForRegion(region);
        Beacons.stopRangingForRegion(region);
    }
});


var styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#F5FCFF',
    },
    header: {
        paddingTop: 50
    },
    headline: {
        fontSize: 20
    },
    row: {
        padding: 8,
        paddingBottom: 16
    },
    smallText: {
        fontSize: 11
    },
    footer: {
        paddingBottom: 50
    }
});

AppRegistry.registerComponent('androidbeacon', () => androidbeacon);
