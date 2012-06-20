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

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document 
public class AccreditedCampus extends Places {

	@SuppressWarnings("unused")
	@Id
	private String campusId;
	
	// A unique identification number for institutions that participate 
	// in the Integrated Postsecondary Education Data System Survey. 
	Integer IPEDS_UnitID;
	
	public AccreditedCampus(){
		super();
	}
	
	public AccreditedCampus(String aName) {
		super(Places.PlaceType.CAMPUS);
		this.name = aName;
	}

	public Integer getIPEDS_UnitID() {
		return IPEDS_UnitID;
	}

	public void setIPEDS_UnitID(Integer iPEDS_UnitID) {
		if (this.IPEDS_UnitID == null) {
			IPEDS_UnitID = iPEDS_UnitID;
		}
	}
	
}
