package org.john.app;

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


import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import au.com.bytecode.opencsv.CSVReader;

import org.john.app.domain.AccreditedCampus;
import org.john.app.domain.AccreditedPostsecondaryInstitution;
import org.john.app.domain.AccreditedProgram;
import org.john.app.domain.GeocoderLocation;
import org.john.app.domain.GeocoderPopulate;

@Repository
public class InstituteRepository {
	static final Logger logger = LoggerFactory.getLogger(InstituteRepository.class);
	static final String COLLECTION_NAME="institutions"; 

	@Autowired
    MongoTemplate mongoTemplate;
	
    /**
     * Create a {@link AccreditedPostsecondaryInstitution} collection if the collection does not already exists
     */
    public void createCollection() {
        if (!mongoTemplate.collectionExists(AccreditedPostsecondaryInstitution.class)) {
            mongoTemplate.createCollection(AccreditedPostsecondaryInstitution.class);
        }
    }

    /**
     * Drops the {@link AccreditedPostsecondaryInstitution} collection if the collection does already exists
     */
    public void dropCollection() {
        if (mongoTemplate.collectionExists(AccreditedPostsecondaryInstitution.class)) {
            mongoTemplate.dropCollection(AccreditedPostsecondaryInstitution.class);
        }
    }
    
	/**
	 * parseMe() parse a csv file from the US department of education. An institution can
	 * and often does span multiple rows in the file. After analyzing at the data, I am 
	 * making the following assumptions. 
	 *  1) Any given institution is defined in consecutive rows. 
	 *  2) It is assumed that the first row defines institution and a program. Subsequent rows
	 *  with the same instituteId define additional programs    
	 * 
	 * @param csvFile
	 * @return
	 */
	
	public int parseMe(final String csvFile) {
		int rc = 0; 
		CSVReader reader = null;
        String[] curRec = null;
        boolean moreToRead=true;
        try {
            reader = new CSVReader(new FileReader(csvFile));
            curRec = reader.readNext(); // ignore header  
           // int count = -1;
            AccreditedPostsecondaryInstitution institute =null;
            while (moreToRead) {
            //	count++; 
            	curRec = reader.readNext();
                if (curRec == null ) { // || count > 10
                	moreToRead = false; 
                } else if (!curRec[0].equals("")) {
                	// Institution ID
                	Integer intValue = null;
                	int index = 0 ;
                	if (!curRec[index].equals("")) {
                		intValue = this.convertToInteger(curRec[index]);
                		//institute = this.getInstitute(intValue);
                		if (institute == null) {
                			institute = new AccreditedPostsecondaryInstitution(intValue);
                		} else if (intValue.intValue() == institute.getId().intValue()) {
                			// This line in the csv file defines a new program for the existing 
                			// institution. 
                		} else {
                			// save previous institution to the repository
                       		try { 
                    			mongoTemplate.insert(institute,COLLECTION_NAME);
                    		} catch (DataAccessException e) {
                    			System.err.println("unexpected data access error (insert): "  + e.getMessage());
                    			System.err.println("\t ignoring and continuing. ");
                    		} catch (Exception e) {
                    			System.err.println("unexpected error (insert): "  + e.getMessage());
                    			System.err.println("\t ignoring and continuing. ");
                    		}
                       		// create a new institution for this row. 
                       		institute = new AccreditedPostsecondaryInstitution(intValue);
                		}
                	}
                	index++; 
                	
                	// Institution Name
                	institute.setName(curRec[index]); 
                	index++; 
                	
                	// Institution Address
                	institute.setAddress(GeocoderPopulate.TYPE_STREET, curRec[index]);
                	index++;
                	
                	// Institution City
                	institute.setCity(curRec[index]);
                	index++;
                	
                	// Institution State
                	institute.setUsState(curRec[index]);
                	index++;
                	
                	// Institution ZIP
                	institute.setZipCode(curRec[index]);
                	index++;
                	
                	// Institution Phone 
                	//  - ignored for now.
                	index++; 
                	
                	// Institution OPEID 
                	if (!curRec[index].equals("")) {
                		institute.setOPEID(curRec[index]);
                	}
                	index++;
                	
                	// Institution IPEDS UnitID
                	if(!curRec[index].equals("")) {
                		intValue = this.convertToInteger(curRec[index]);
                		institute.setIPEDS_UnitID(intValue);
                	}
                	index++;
                	
                	// Institution web address
                	institute.setWebsite(curRec[index]);
                	index++; 
                	
                	// The data dictionary specific from the department of education defines campuses.
                	// In the database, it is used rarely.  
                	
                	// Campus ID - ignored
                	AccreditedCampus campus = null; 
                	index++;
             
                	// Campus Name
                	if(!curRec[index].equals("") ) {
                		campus = institute.createCampus(curRec[index]);
                	}
                	index++;   
                	
                	// Campus Address
                	if(!curRec[index].equals("") && campus!=null) {
                		campus.setAddress(GeocoderPopulate.TYPE_STREET, curRec[index]);
                	}
                	index++;
                	
                	// Campus City
                	if(!curRec[index].equals("") && campus!=null) {
                		campus.setCity(curRec[index]);
                	}
                	index++;
                	
                	// Campus State
                	if(!curRec[index].equals("") && campus!=null) {
                		campus.setUsState(curRec[index]);
                	}
                	index++;
                	
                	// Campus Zip
                	if(!curRec[index].equals("") && campus!=null ) {
                		campus.setZipCode(curRec[index]);
                	}
                	index++;
                	
                	// Campus IPEDS UnitID
                	if(!curRec[index].equals("")) {
                		intValue = this.convertToInteger(curRec[index]);
                		campus.setIPEDS_UnitID(intValue);
                	}
                	index++;
                	
                	// Campus Accreditation type 
                	String accType = null; 
                	if(!curRec[index].equals("")) {
                		accType = curRec[index];
                	}
                	index++;
                	
                	String agency = curRec[index];
                	index++; 
                	
                	// Consistently, there is an empty field here.  
                	index++;
                	
                	String programName = curRec[index];
                	AccreditedProgram program = institute.createProgram(programName);
                	program.setAgencyName(agency);
                	program.setAccreditationType(accType);
                	index++;
                	
                	// Data dictionary specifies a Review Date
                	// Doesn't appear in the database
                	// String d = curRec[index];
                	// program.setNextReview(this.dateValue(d));
                	// index++;
                	
                	// Accreditation Status 
                	program.setAccreditationStatus(curRec[index]);
                	index++;
                	
                	program.setAcceditationDatatype(curRec[index]);
                	index++;
                	
                	// Periods this is a range with both  start and end dates
                	program.setDates(curRec[index]);
                	index++; 
                	
                	// Last action
                	if (!curRec[index].equals("")) {
                		program.setLastAction(curRec[index]);
                	}
     
                }
            }           
        } catch (FileNotFoundException e404) {
        	System.err.println("Accreditation_2012_03.csv not found. Exiting.");
        	rc = -1;
        	return rc;
        } catch (IOException ioe) {
        	ioe.printStackTrace();
        }
        return rc; 
	}
	
	public  void geocodeMe() {
		GeocoderLocation geo = new GeocoderLocation(); 
        List<AccreditedPostsecondaryInstitution> results = null;
		results = mongoTemplate.find(query(where("location").exists(false)).limit(500), 
				AccreditedPostsecondaryInstitution.class);

		Iterator<AccreditedPostsecondaryInstitution> iterator = results.iterator();
		while(iterator.hasNext() && !geo.limitExceeded() ) {
			AccreditedPostsecondaryInstitution institute = iterator.next();
			String geocodeDis = 
				institute.getAddress() + " " + 
				institute.getCity() + " " +
				institute.getUs_state();
			geo.reset();
			geo.parseMe(geocodeDis);

			
			// geocode institute. 
			try { 
				String latStr = geo.getLat(); 
				if (latStr != null) {
					institute.setLat(latStr);
				} else {
					System.err.println("error: null lat for "  + geocodeDis);
	    			System.err.println("\t ignoring and continuing. ");
				}
				String lngStr = geo.getLng();
				if (lngStr != null) {
					institute.setLng(lngStr);
				} else {
					System.err.println("error: null lng for "  + geocodeDis);
	    			System.err.println("\t ignoring and continuing. ");
				}
				
			} catch (Exception e ){
				e.printStackTrace();
			}
			
			
			try {
				// save modified version 
				mongoTemplate.save(institute,COLLECTION_NAME);
			} catch (DataAccessException e) {
    			System.err.println("unexpected data access error (save): "  + e.getMessage());
    			System.err.println("\t ignoring and continuing. ");
    		} catch (Exception e) {
    			System.err.println("unexpected error (save): "  + e.getMessage());
    			System.err.println("\t ignoring and continuing. ");
    		}
		}
	}

	private Integer convertToInteger(String aStr) {
		Integer intValue = null;
		// excel triple quotes must die. 
		intValue = Integer.valueOf(aStr.replace("\"", ""));
		return intValue;
	}
    
}
