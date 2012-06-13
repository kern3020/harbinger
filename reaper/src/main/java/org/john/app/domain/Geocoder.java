package org.john.app.domain;

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

abstract public class Geocoder {
	final public static String CC_BASE_URL="http://maps.googleapis.com/maps/api/geocode/xml?address=";
	final public static String CC_URL_SUFFIX="&sensor=false";
	
	// tags I'm looking for 
	protected final static String TAG_GEOCODE="GeocodeResponse";
	protected final static String TAG_STATUS="status";
	protected final static String TAG_RESULT="result";
	protected final static String TAG_ADDRESS_COMPONENT="address_component";
	protected final static String TAG_SHORT_NAME="short_name";
	protected final static String TAG_TYPE="type";
	
	protected final static String TAG_LOCATION="location";
	protected final static String TAG_LAT="lat"; 
	protected final static String TAG_LNG="lng";
	
	// geo types 
	protected final static String TYPE_NAME="point_of_interest";
	public final static String TYPE_STREET="street_number"; // used by campus 
	public final static String TYPE_ROUTE="route"; // used by campus
	protected final static String TYPE_CITY="locality";
	protected final static String TYPE_COUNTY="administrative_area_level_2";
	protected final static String TYPE_STATE="administrative_area_level_1";
	protected final static String TYPE_ZIPCODE="postal_code";
	
	//state 
	protected boolean inDocument; // did we get a geocode response ? 
	protected boolean inStatus; // confirm status.
	protected boolean inResult; 
	protected boolean inAddressComponent; 
	protected boolean inShortName;
	protected boolean inType; 
	protected boolean inLocation; 
	protected boolean inLat;
	protected boolean inLng; 
}
