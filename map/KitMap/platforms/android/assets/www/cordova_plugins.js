cordova.define('cordova/plugin_list', function(require, exports, module) {
module.exports = [
    {
        "file": "plugins/com.phonegap.plugins.mapkit/www/MapKit.js",
        "id": "com.phonegap.plugins.mapkit.mapkit",
        "clobbers": [
            "plugin.mapKit"
        ]
    }
];
module.exports.metadata = 
// TOP OF METADATA
{
    "com.phonegap.plugins.mapkit": "0.9.3"
}
// BOTTOM OF METADATA
});