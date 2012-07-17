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



/*
 * This is a generic place object which is designed to be inserted into 
 * mongo as a document. 
 */

public class Places {
	String name; 
	String address; // street_number + route
	String city;  // locality 
	String county; // administrative_area_level_2
	String us_state;  // administrative_area_level_1, shortname
	String zipCode; // postal_code
	double[] location; 
	String website; 
	PlaceType type;

	public enum PlaceType {CAMPUS,LIBRARY, UNIVERSITY, UNSPECIFIED};

	private final static int LAT=0;
	private final static int LNG=1;
	
	public Places(PlaceType pt) {
		super();
		this.type = pt;
	}
	public Places() {
		super();
	}
	
	// It is tempting to represent the url as a {{URL}}. 
	// I do not see the benefit of storing it in Mongo that way.
	// If you change your mind, remember there is no default contructor. 
	// Spring data is planning to create a default converter. If they
	// haven't added it, you'll need to do so. 
	// see https://jira.springsource.org/browse/DATAMONGO-461
	// and https://jira.springsource.org/browse/DATAMONGO-462
	public String getWebsite() {
		return website;
	}
	
	public void setWebsite(String website)  {
		this.website = website;
		if (!website.isEmpty() && 
				!website.toLowerCase().contains("http")) {
			this.website = "http://" + website;
		}

	}
	
	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public double getLat() {
		return this.location[LAT];
	}

	public void setLat(String lat) {
		if (this.location == null) {
			this.location = new double[2];
		}
		this.location[LAT] = Double.valueOf(lat);
	}

	public double getLng() {
		return this.location[LNG];
	}

	public void setLng(String lng) {
		if (this.location == null) {
			this.location = new double[2];
		}
		this.location[LNG] = Double.valueOf(lng);
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAddress() {
		return address;
	}
	
	// address is composed of both street_number + route
	public void setAddress(String type, String address) {
		if (this.address == null) {
			this.address = address; 
		}
	}
	public String getUs_state() {
		return us_state;
	}
	public void setUsState(String us_state) {
		this.us_state = us_state;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode.replace("\"", "");
	}

	@Override
	public String toString() {
		String rc = null; 
		switch(this.type) {
		case CAMPUS:
			rc="Campus";
			break;
		case LIBRARY: 
			rc="Library";
			break;
		case UNIVERSITY:
			rc="university";
			break;
		default:	
			break;
		}
		return rc + " [name=" + name + ", address=" + address + ", city="
				+ city + ", county=" + county + ", us_state=" + us_state
				+ ", zipCode=" + zipCode + "]";
	}
	
}
