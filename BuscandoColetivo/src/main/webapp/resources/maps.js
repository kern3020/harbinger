
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


var map = null; // global map object 
var markers = []; // stores marker objects to allow us to clear them. 
var curZoomLevel = 0; 

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
	// boston
	//var latlng = new google.maps.LatLng( 42.357778, -71.061667);	
	// USA centroid - Lebanon, Kansas
	var latlng = new google.maps.LatLng(39.810556, -98.556111); 
	// Lynchburg, VA 
	// var latlng = new google.maps.LatLng( 37.41, -79.1422);
	
	// A zoom level of 8 provides a comfortable regional view while a 
	// zoom level of 4 works for the continental US. When looking at zoom
	// level 4 or less, aggregate the institutions on a per state 
	// basis. BTW, 12 is good for a city.  
	var options = {
		center: latlng,
		zoom: 4,
		mapTypeId: google.maps.MapTypeId.TERRAIN
	};
	map = new google.maps.Map(document.getElementById("map_canvas"),options);
	// Tempting to use 'bounds_changed' but this will fire continuously
	// while the user is panning. 
	google.maps.event.addListener(map, 'idle', function() {
	        getInstitutions();
	     });
	
	google.maps.event.addListener(map, 'zoom_changed', function() {
        var newZoom = map.getZoom(); 
        if (curZoomLevel <= 4 ) {
        	// we are zoomed out 
        	if ( newZoom > 4 ) { 
        		 clearMarkers();
        		 getInstitutions();
        	}
        } else {
        	// we are zoomed in 
        	if ( newZoom <= 4 ) {
        		 clearMarkers(); 
        		 getInstitutions();
        	}
        }
        curZoomLevel = newZoom; 
     });
	// setup event on form
	setupEvent();
}

function clearMarkers() { 
    for (var i = 0; i < markers.length; i++) {
        markers[i].setMap(null);
    }
    markers = []; 
}

function getInstitutions () {
	var latlng = map.getBounds(); 
	var zl = map.getZoom(); 
	// baseURL is passed in via jsp.
	var url = baseURL;
	if (zl > 4 ) { 
		url +=  "/latSW/" + latlng.getSouthWest().lat() +
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
	            markers.push(marker);
	            (function (aName,aMarker){
	                google.maps.event.addListener(aMarker, 'click', function () {
	                        var infowindow = new google.maps.InfoWindow({
	                                content: aName});
	                        infowindow.open(map,aMarker);
	                });
	            })(data.institutions[i].name,marker);
	        }
	    } );
	} else { 
		// use is zoomed out. Show them the aggregation of institutions per state. 
		url += "/countsPerState.json";
		$.get(url, function (data) {
	        // (guard) If JSON returns nothing, simply return. 
	        if (data.countsPerState == null) return; 
	        // Add markers for each state in this extent. 
	        var lat = 0; 
	        var lng = 0;
	        // {"count":38,"abbrev":"VT","lat":43.86954,"lng":-72.47119}
	        for (var i = 0; i < data.countsPerState.length;i++) {
	        	lat = data.countsPerState[i].lat;
	        	lng = data.countsPerState[i].lng;
	            marker = new google.maps.Marker({
	                position:  new google.maps.LatLng(lat ,lng),
	                map: map ,
	                title: data.countsPerState[i].abbrev 
	                	+ ": " 
	                	+  data.countsPerState[i].count
	                });
	            markers.push(marker);
	            (function (aName,aMarker){
	                google.maps.event.addListener(aMarker, 'click', function () {
	                        var infowindow = new google.maps.InfoWindow({
	                                content: aName});
	                        infowindow.open(map,aMarker);
	                });
	            })(data.countsPerState[i].name,marker);
	        }
	    } );
	}
}

// given a city or zip code, try to geo-reference it.
function setupEvent() { 
	$('#submitButton').click(function(e) {
		e.preventDefault();
		// At first one might be inclined to determine if it is a zip or city.
		// don't. Let the geo-coding API  figure it out. 
		var txt=$("#cityOrZip").val(); 
		var url = baseURL + "/CityOrZip/" + encodeURI(txt) + "/centroid.json";
		$.get(url, function (data) {
			// Was the server able to geo-reference the city or zip? 
			if (data.location.lat == null || data.location.lng == null) {
				// inform user we couldn't geo-reference the city or zip? 
				alert("Geo-reference failed.");
			} else { 
				map.setCenter( new google.maps.LatLng( data.location.lat, data.location.lng ) );
			}
			
		},'json'); 
	});
}
