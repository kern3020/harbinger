package org.john.app;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import au.com.bytecode.opencsv.CSVReader;

import org.john.app.domain.Institute; 
import org.john.app.domain.AccreditedCampus;
import org.john.app.domain.AccreditedPostsecondaryInstitution;
import org.john.app.domain.AccreditedProgram;
import org.john.app.domain.Geocoder;

@Repository
public class InstituteRepository {
	static final Logger logger = LoggerFactory.getLogger(InstituteRepository.class);

	@Autowired
    MongoTemplate mongoTemplate;

	public void logAllPersons() {
        List<Institute> results = mongoTemplate.findAll(Institute.class);
        logger.info("Total amount of persons: {}", results.size());
        logger.info("Results: {}", results);
    }

	public void insert(String aName, Integer aZip) {
		Institute i = new Institute(aName, aZip);
		mongoTemplate.insert(i);
	}
	
	public void dump() {
		System.out.println("entering dump.");
		int count = 0;
		List<Institute> results = mongoTemplate.findAll(Institute.class);
        Iterator<Institute> instituteIterator = results.iterator();
        while (instituteIterator.hasNext() && count < 5 ) {
            Institute nextInstitute = instituteIterator.next();
            System.out.println(nextInstitute);
            count++;
        }

	}
	
    /**
     * Create a {@link Institute} collection if the collection does not already exists
     */
    public void createCollection() {
        if (!mongoTemplate.collectionExists(Institute.class)) {
            mongoTemplate.createCollection(Institute.class);
        }
    }

    /**
     * Drops the {@link Institute} collection if the collection does already exists
     */
    public void dropCollection() {
        if (mongoTemplate.collectionExists(Institute.class)) {
            mongoTemplate.dropCollection(Institute.class);
        }
    }
	
    // TODO: check mongo for existing institution.  
	public  AccreditedPostsecondaryInstitution getInstitute(Integer aId){		
		AccreditedPostsecondaryInstitution rc = new AccreditedPostsecondaryInstitution(aId);
		return rc;
	}

	
	public int parseMe(final String csvFile) {
		int rc = 0; 
		CSVReader reader = null;
        String[] curRec = null;
        boolean moreToRead=true;
        try {
            reader = new CSVReader(new FileReader(csvFile));
            curRec = reader.readNext(); // ignore header  
            while (moreToRead) {
            	curRec = reader.readNext();
                if (curRec == null) {
                	moreToRead = false; 
                } else if (!curRec[0].equals("")){
                	// Institution ID
                	AccreditedPostsecondaryInstitution institute =null;
                	Integer intValue = null;
                	int index = 0 ;
                	if (!curRec[index].equals("")) {
                		intValue = this.convertToInteger(curRec[index]);
                		institute = this.getInstitute(intValue);
                	}
                	index++; 
                	
                	// Institution Name
                	institute.setName(curRec[index]); 
                	index++; 
                	
                	// Institution Address
                	institute.setAddress(Geocoder.TYPE_STREET, curRec[index]);
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
                		try { 
                			intValue = this.convertToInteger(curRec[index]);
                			institute.setOPEID(intValue);
                		} catch (NumberFormatException nfe) {
                			System.out.println("Number format exception: " + curRec[index] + "\tname: " + curRec[1]);
                			System.out.println("Ignore and continue. ");
                		}
                	}
                	index++;
                	
                	// Institution IPEDS UnitID
                	if(!curRec[index].equals("")) {
                		intValue = this.convertToInteger(curRec[index]);
                		institute.setIPEDS_UnitID(intValue);
                	}
                	index++;
                	
                	// Institution web address
                	try {
                		institute.setWebsite(curRec[index]);
                	} catch (java.net.MalformedURLException me) {
                		System.out.println("bogus URL - " + curRec[index]);
                	}
                	index++; 
                	
                	// The data dictionary specific from the department of education defines campuses.
                	// In the database, it is used rarely.  
                	
                	// Campus ID 
                	AccreditedCampus campus = null; 
                	if(!curRec[index].equals("")) {
                		intValue = this.convertToInteger(curRec[index]);
                		campus = institute.getCampus(intValue);
                	}
                	index++;
             
                	// Campus Name
                	if(!curRec[index].equals("") && campus!=null) {
                		campus.setName(curRec[index]);
                	}
                	index++;   
                	
                	// Campus Address
                	if(!curRec[index].equals("") && campus!=null) {
                		campus.setAddress(Geocoder.TYPE_STREET, curRec[index]);
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
                	if(!curRec[index].equals("")) {
                		institute.setAccreditationType(curRec[index]);
                	}
                	index++;
                	
                	String agency = curRec[index];
                	index++; 
                	
                	// Consistently, there is an empty field here.  
                	index++;
                	
                	String programName = curRec[index];
                	AccreditedProgram program = institute.getProgram(programName);
                	program.setAgencyName(agency);
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
            		
                	mongoTemplate.insert(institute);
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
	

	private Integer convertToInteger(String aStr) {
		Integer intValue = null;
		// excel triple quotes most die. 
		intValue = Integer.valueOf(aStr.replace("\"", ""),16);
		return intValue;
	}
    
}
