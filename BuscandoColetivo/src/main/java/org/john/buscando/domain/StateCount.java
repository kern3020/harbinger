package org.john.buscando.domain;

import java.util.Arrays;

/**
 * Used to store results of a group by call. Use these results
 * when the map is zoomed out and we want a state by state 
 * count of institutions.
 * 
 * @author John Kern
 *
 */

public class StateCount {
	String abbrev; 
	double [] location ;
	int count;

	private final static int LAT=0;
	private final static int LNG=1;
	
	
	
	public StateCount() {
		super();
		location = new double[2];
	}

	public String getAbbrev() {
		return abbrev;
	}

	public void setAbbrev(String abbrev) {
		this.abbrev = abbrev;
	}

	public void setLat(double aLat) {
		this.location[LAT] = aLat;
	}

	public void setLng(double aLng) {
		this.location[LNG] = aLng;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "StateCount [abbrev=" + abbrev + ", location="
				+ Arrays.toString(location) + ", count=" + count + "]";
	} 
}

