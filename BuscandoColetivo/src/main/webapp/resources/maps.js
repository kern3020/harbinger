
/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var map = null;

function sleep(ms)
{
        var dt = new Date();
        dt.setTime(dt.getTime() + ms);
        while (new Date().getTime() < dt.getTime());
}

function isEmpty( o ) {
    for ( var p in o ) { 
        if ( o.hasOwnProperty( p ) ) { return false; }
    }
    return true;
}


function setupMap() {
	//var latlng = new google.maps.LatLng( 42.357778, -71.061667);	// boston
	// var latlng = new google.maps.LatLng(39.810556, -98.556111); // USA centroid - Lebanon, Kansas
	var latlng = new google.maps.LatLng( 37.41, -79.1422);
	var options = {
		center: latlng,
		zoom: 8,
		mapTypeId: google.maps.MapTypeId.TERRAIN
	};
	map = new google.maps.Map(document.getElementById("map_canvas"),options);
	// Tempting to use 'bounds_changed' but this will fire continuously
	// while the user is panning. 
	google.maps.event.addListener(map, 'idle', function() {
	        // need to allow time to download the tiles before google maps can
	        // compute the bounds of the viewport. 
	        getInstitutions();
	     });
	// setup event for form
	setupEvent();
}

function getInstitutions () {
	var latlng = map.getBounds(); 
	// baseURL is passed in via jsp.
    var url = baseURL +
            "/latSW/" + latlng.getSouthWest().lat() +
            "/lngSW/" + latlng.getSouthWest().lng() +
            "/latNE/" + latlng.getNorthEast().lat() +
            "/lngNE/" + latlng.getNorthEast().lng() + 
            "/mapEm.json";
    $.get(url, function (data) {
        // (guard) If JSON returns nothing, simply return. 
        if (data.institutions == null) return; 
        // Add markers for each institute in this extent. 
        var lat = 0; 
        var lng = 0;
        for (var i = 0; i < data.institutions.length;i++) {
        	lat = data.institutions[i].lat;
        	lng = data.institutions[i].lng;
            marker = new google.maps.Marker({
                position:  new google.maps.LatLng(lat ,lng),
                map: map ,
                title: data.institutions[i].name
                });
            (function (aName,aMarker){
                google.maps.event.addListener(aMarker, 'click', function () {
                        var infowindow = new google.maps.InfoWindow({
                                content: aName});
                        infowindow.open(map,aMarker);
                });
            })(data.institutions[i].name,marker);
        }
    } );
}

// given a city or zip code, try to geo-reference it.
function setupEvent() { 
	$('#submitButton').click(function(e) {
		e.preventDefault();
		// e.stopPropagation();
		// at first one might be inclined to determine if it is a zip or city.
		// don't. Let the geo-coding API figure it out. 
		var txt=$("#cityOrZip").val(); 
		var url = baseURL + "/CityOrZip/" + encodeURI(txt) + "/centroid.json";
		$.get(url, function (data) {
			// Was the server able to geo-reference the city or zip? 
			if (data.location.lat == null || data.location.lng == null) {
				// inform user we couldn't geo-reference the city or zip? 
				alert("Geo-reference failed.");
			} else { 
				// should this be panTo() ?
				map.setCenter( new google.maps.LatLng( data.location.lat, data.location.lng ) );
			}
			
		},'json'); 
	});
}
