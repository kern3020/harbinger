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

import java.util.LinkedList;
import java.util.List;

/**
 * This class represents an organization which may have multiple location.  
 *  
 * @author John Kern
 * 
 */

public class Organization {

	String name; 
	String url;
	List<Places> campus;
	
	
	/*
	 * default constructors are important for marshalling objects to and
	 * re-constituting them form repository. 
	 */
	public Organization() {
		super();
	}

	public Organization(String name) {
		super();
		this.name = name;
		this.campus = new LinkedList<Places>(); 
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public void add(Places aCampus){
		campus.add(aCampus);
	}
	
	@Override
	public String toString() {
		return "CommunityCollege [name=" + name + ", url=" + url + ", campus="
				+ campus + "]";
	}
	
	// just for testing
	private int size() {
		return campus.size();
	}
	
	double  getFirstLat() {
		double rc = 0.0;
		if (size() > 0 ) {
			rc = campus.get(0).getLat();
		}
		return rc; 
	}
	
	double  getFirstLng() {
		double rc  = 0.0;
		if (size() > 0 ) {
			rc = campus.get(0).getLng();
		}
		return rc; 
	}
}
