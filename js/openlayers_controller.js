var map = null;

function initMap(latitude, longitude, horizontalAccuracy) {
	initMapByHTMLElementId(latitude, longitude, horizontalAccuracy, "map");
}

function initMapByHTMLElementId(latitude, longitude, horizontalAccuracy,
		elementName) {

	var latitude = parseFloat(latitude);
	var longitude = parseFloat(longitude);
	var horizontalAccuracy = parseFloat(horizontalAccuracy);

	map = null;
	$("#" + elementName).empty();

	console.log('lat:' + latitude + ', long:' + longitude + ', accuracy: '
			+ horizontalAccuracy);

	setTimeout(function() {
		map = createMapWithLatitudeAndLongitudeByHTMLElementId(latitude,
				longitude, elementName);
		addCenterAndThreshold(map, latitude, longitude, horizontalAccuracy);
	}, 300);
}

function createMapWithLatitudeAndLongitudeByHTMLElementId(latitude, longitude,
		elementName) {

	return new ol.Map({
		layers : [ new ol.layer.Tile({
			source : new ol.source.OSM()
		}) ],
		controls : ol.control.defaults()
				.extend([ new ol.control.FullScreen() ]),
		target : elementName,
		view : new ol.View({
			center : ol.proj.transform([ longitude, latitude ], 'EPSG:4326',
					'EPSG:3857'),
			zoom : 19
		})
	});
}

function addCenterAndThreshold(map, latitude, longitude, accuracy) {

	var circle = new ol.geom.Circle(ol.proj.transform([ longitude, latitude ],
			'EPSG:4326', 'EPSG:3857'), accuracy);

	var center = new ol.geom.Circle(ol.proj.transform([ longitude, latitude ],
			'EPSG:4326', 'EPSG:3857'), 2);

	var circleFeature = new ol.Feature(circle);
	var centerFeature = new ol.Feature(center);

	var circleVectorSource = new ol.source.Vector({
		projection : 'EPSG:4326',
	});
	var centerVectorSource = new ol.source.Vector({
		projection : 'EPSG:4326',
	});

	circleVectorSource.addFeatures([ circleFeature ]);
	centerVectorSource.addFeatures([ centerFeature ]);

	var circleLayer = new ol.layer.Vector({
		source : circleVectorSource,
		style : [ new ol.style.Style({
			stroke : new ol.style.Stroke({
				color : $.app.map.accuracy.stylingStrokeColor,
				width : $.app.map.accuracy.stylingStrokeWidth
			}),
			fill : new ol.style.Fill({
				color : $.app.map.accuracy.stylingFillColor
			})
		}) ]
	});
	var c = '#FF0000';
	var centerLayer = new ol.layer.Vector({
		source : centerVectorSource,
		style : [ new ol.style.Style({
			stroke : new ol.style.Stroke({
				color : c,
				width : 10
			}),
			fill : new ol.style.Fill({
				color : c
			})
		}) ]
	});

	map.addLayer(circleLayer);
	map.addLayer(centerLayer);
}

function getInverseGeocodingInfo(latitude, longitude, callback) {

	// we are using Nominatim service
	var reverseGeocode = 'https://nominatim.openstreetmap.org/reverse?format=json&lat='
			+ latitude + '&lon=' + longitude + '&addressdetails=1';

	// use jQuery to call the API and get the JSON results
	$.getJSON(reverseGeocode, function(data) {
		callback(data.display_name);
	});
}
