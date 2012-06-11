package org.john.app.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document 
public class AccreditedCampus extends Places {

	@SuppressWarnings("unused")
	@Id
	private String campusId;

	// Database Specific Identification Number for Institution
	Integer id;
	
	// A unique identification number for institutions that participate 
	// in the Integrated Postsecondary Education Data System Survey. 
	Integer IPEDS_UnitID;
	
	public AccreditedCampus(){
		super();
	}
	
	public AccreditedCampus(Integer aId) {
		super(Places.PlaceType.CAMPUS);
		this.id = aId; 
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
