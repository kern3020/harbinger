package org.john.app.domain;

import java.net.URL;
import java.net.MalformedURLException;


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
	String lat; 
	String lng; 
	URL website; 
	PlaceType type;

	public enum PlaceType {CAMPUS,LIBRARY, UNIVERSITY, UNSPECIFIED};
	
	public Places(PlaceType pt) {
		super();
		this.type = pt;
	}
	public Places() {
		super();
	}
	
	public URL getWebsite() {
		return website;
	}
	public void setWebsite(String website) throws MalformedURLException {
		if (!website.toLowerCase().contains("http")) {
			website = "http://" + website;
		}
		this.website = new URL(website);
	}
	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
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
		} else if (type.equals(Geocoder.TYPE_STREET)) {
			this.address = address + " " + this.address; 
		} else { 
			this.address += " " + address;
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
				+ ", zipCode=" + zipCode + ", lat=" + lat + ", lng=" + lng
				+ "]";
	}
	
}