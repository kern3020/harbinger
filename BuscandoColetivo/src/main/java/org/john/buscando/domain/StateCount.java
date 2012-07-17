package org.john.buscando.domain;

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

