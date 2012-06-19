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

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 
 * This class represents an Accredited Postsecondary Institute as defined by the 
 * US Department of Education. ReadMe.doc file in the data/Accreditation defines 
 * the fields in the data file. 
 * 
 * N.B.: The data contents redundant information. We use the first value and ignore 
 * subsequence ones. 
 * 
 * @author John Kern
 *
 */

@Document(collection="institutions")
public class AccreditedPostsecondaryInstitution extends Places {
	@SuppressWarnings("unused")
	@Id
	private String instituteId;
	 
	// Database Specific Identification Number for Institution
	Integer id;
	
	// Identification number used by the U.S. Department of Education's Office of 
	// Postsecondary Education (OPE) to identify schools that have Program 
	// Participation Agreements (PPA) so that its students are eligible to 
	// participate in Federal Student Financial Assistance programs under Title 
	// IV regulations. This is a 6-digit number followed by a 2-digit suffix 
	// used to identify branches, additional locations, and other entities 
	// that are part of the eligible institution.
	Integer OPEID;
	
	// A unique identification number for institutions that participate in the 
	// Integrated Postsecondary Education Data System Survey.
	Integer IPEDS_UnitID; 

	// institutions can have multiple campuses. Story in hash to allow for quick existence check 
	// based on Database Id. 
	private Map<Integer, AccreditedCampus> campusMap;
	
	// programs associates the programs name with this accredited program object.  
	private Set<AccreditedProgram> programSet; 
	
	AccreditationType accreditationType; 
	
	enum AccreditationType {INSTITUTIONAL, SPECIALIZED, INTERSHIP_RESIDENCY};
	
	public AccreditedPostsecondaryInstitution(Integer aId) {
		super(Places.PlaceType.UNSPECIFIED);
		this.id = aId;
		this.campusMap = new HashMap<Integer, AccreditedCampus>();
		this.programSet = new HashSet<AccreditedProgram>();
	}
	
	public AccreditedPostsecondaryInstitution(){
		super();
	}
	
	// returns the campus or null if none is defined. 
	public AccreditedCampus getCampus(Integer aId) {
		AccreditedCampus rc = campusMap.get(aId);
		if (rc == null ) {
			rc = new AccreditedCampus(aId);
			campusMap.put(aId, rc);
		}
		return rc; 				
	}
	
	// creates a new program and adds it to a set. 
	// new program is returned. 
	public AccreditedProgram createProgram(String aName) {
		AccreditedProgram rc = new AccreditedProgram(aName);
		this.programSet.add(rc);
		return rc;
	}
	
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer aId) {
		if (this.id == null) {
			this.id = aId;
		}
	}

	public Integer getOPEID() {
		return this.OPEID;
	}

	public void setOPEID(Integer aOPEID) {
		if (this.OPEID == null) {
			this.OPEID = aOPEID;
		}
	}

	public Integer getIPEDS_UnitID() {
		return this.IPEDS_UnitID;
	}

	public void setIPEDS_UnitID(Integer aIPEDS_UnitID) {
		if (this.IPEDS_UnitID == null ) {
			this.IPEDS_UnitID = aIPEDS_UnitID;
		}
	}

	public AccreditationType getAccreditationType() {
		return accreditationType;
	}

	public void setAccreditationType(String accreditationTypeStr) {
		if (this.accreditationType == null) {
			if (accreditationTypeStr.toLowerCase().equals("internship/residency")) {
				this.accreditationType = AccreditationType.INTERSHIP_RESIDENCY;
			} else {
				this.accreditationType = AccreditationType.valueOf(accreditationTypeStr.toUpperCase());
			}
		}
	}
}
