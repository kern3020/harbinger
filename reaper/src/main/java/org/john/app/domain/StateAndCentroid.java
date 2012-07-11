package org.john.app.domain;

/**
 * For the harbinger project, I want to display the a count of institutes 
 * per state when the map is zoomed out.  For this I need the centroid for 
 * each state. I found this list on the web. 
 * http://www.maptechnavigation.com/support/forums/messages.cfm?threadid=1101&CFID=2901702&CFTOKEN=69902104
 * This class represents a state and its centroid.
 * 
 * @author John Kern
 */

public class StateAndCentroid {

	String name;
	String abbreviation;
	String fips; //FIPS State Numeric Code
	double[] location; 
	private final static int LAT=0;
	private final static int LNG=1;
	
	public StateAndCentroid() {
		super();
		this.location = new double[2];
	}

	public String getFips() {
		return fips;
	}

	public void setFips(String fips) {
		this.fips = fips;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAbbreviation() {
		return abbreviation;
	} 

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public double getLat() {
		return location[LAT];
	}

	public void setLat(double lat) {
		this.location[LAT] = lat;
	}

	public double getLng() {
		return location[LNG];
	}

	public void setLng(double lng) {
		this.location[LNG] = lng;
	}

	@Override
	public String toString() {
		return "StateAndCentroid [name=" + name + ", abbreviation="
				+ abbreviation + ", fips=" + fips + ", lat=" + location[LAT] + ", lng="
				+ location[LNG] + "]";
	}

}
