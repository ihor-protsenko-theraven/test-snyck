var map = null;

function initMap(latitude, longitude, horizontalAccuracy) {
	initMapByHTMLElementId(latitude, longitude, horizontalAccuracy, "map");
}

function initMapByHTMLElementId(latitude, longitude, horizontalAccuracy,
		elementName) {

	latitude = parseFloat(latitude);
	longitude = parseFloat(longitude);
	horizontalAccuracy = parseFloat(horizontalAccuracy);

	console.log('lat:' + latitude + ', long:' + longitude + ',accu: '
			+ horizontalAccuracy);

	map = null;
	$("#" + elementName).empty();
	markers = [];
	isOut = false;
	polygons = [];
	overlays = [];
	circleCenter = [];
	circleRadius = 0;
	lastUpdateTime = "";
	
	setTimeout(function() { 
	    google.maps.event.trigger(map, 'resize'); 
	    console.log('resize timeout v2'); 
	 
	    map = createGoogleMapWithLatitudeAndLongitudeByHTMLElementId(latitude, 
	        longitude, elementName); 
	    addAccuracyRadius(map, latitude, longitude, horizontalAccuracy); 
	    addMapMarker(map, latitude, longitude); 
	  }, 500); 
}

function createGoogleMapWithLatitudeAndLongitudeByHTMLElementId(latitude,
		longitude, elementName) {

	return new google.maps.Map(document.getElementById(elementName), {
		center : {
			lat : latitude,
			lng : longitude
		},
		zoom : 18
	});

}

function addMapMarker(map, latitude, longitude) {

	var marker = new google.maps.Marker({
		position : {
			lat : latitude,
			lng : longitude
		},
		animation : google.maps.Animation.DROP,
		map : map
	});
	markers.push(marker);
}

function addAccuracyRadius(map, latitude, longitude, horizontalAccuracy) {

	circleCenter = new google.maps.LatLng(latitude, longitude);
	circleRadius = horizontalAccuracy;

	var circle = new google.maps.Circle({
		center : circleCenter,
		radius : circleRadius,
		strokeColor : $.app.map.accuracy.stylingStrokeColor,
		strokeWeight : $.app.map.accuracy.stylingStrokeWidth,
		fillColor : $.app.map.accuracy.stylingFillColor,
		fillOpacity : 1
	});

	circle.setMap(map);
	polygons.push(circle);

}

function getInverseGeocodingInfo(latitude, longitude, callback) {

	var geocoder = new google.maps.Geocoder;
	var inverseGeocodingInfo = "";

	var latlng = {
		lat : latitude,
		lng : longitude
	};

	geocoder.geocode({
		'location' : latlng
	}, function(results, status) {
		if (status === 'OK') {
			if (results[0]) {
				inverseGeocodingInfo = results[0].formatted_address;
			} else {
				console.log('No results found');
			}
		} else {
			console.log('Geocoder failed due to: ' + status);
		}

		callback(inverseGeocodingInfo);
	});

}