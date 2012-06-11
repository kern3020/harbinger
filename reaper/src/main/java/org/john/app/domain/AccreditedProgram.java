package org.john.app.domain;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class AccreditedProgram {
	
	@SuppressWarnings("unused")
	@Id
	private String programId;
	
	String programName;
	String agencyName; // who endorsed the program. 
	Date nextReview; 
	Date initialDate;  // example of format: "05/24/1968 - Current"
	Date endDate; 
	AccreditationStatus accreditationStatus; 
	AccreditationDataType accreditationDataType;
	ProgramAction lastAction; 
	
	enum AccreditationStatus {PreAccredited, Accredited};
	enum AccreditationDataType {Actual, Estimated}; 
	enum ProgramAction {
		Accredited, // an institution or program which was pre–accredited and was granted full accredited status.
		DeniedFullAccreditation, // an institution or program which was pre–accredited and was not granted full accredited status
		Resigned, // an institution or program which has voluntarily withdrew its recognition as an accredited institution or program.
		Expired, //  an institution or program which has allowed its recognition as an accredited institution or program to expire
		ResignedUnderShowCause, // an institution or program which has voluntarily withdrew its recognition as an 
						// accredited institution or program while under a Show Cause status.
		Terminated, // an institution or program that an agency no longer accredits.
		ClosedbyInstitution, // a program which has been closed by the institution. 
		ClosedbyAgency // an institution or program type that an agency is no longer recognized to accredit.
	};
	
	public AccreditedProgram(String programName) {
		super();
		this.programName = programName;
	}
	
	public AccreditedProgram(){
		super();
	}
	
	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		if (this.programName == null ) {
			this.programName = programName;
		}
	}

	public String getAgencyName() {
		return agencyName;
	}

	public void setAgencyName(String agencyName) {
		if (this.agencyName == null) {
			this.agencyName = agencyName;
		}
	}

	public Date getNextReview() {
		return nextReview;
	}

	public void setNextReview(String nextReviewStr) {
		if (this.nextReview == null ) {
			this.nextReview = dateValue(nextReviewStr);
		}
	}
	
	/*
	 * Dates in the data are often in this format: 05/07/1987 - Current
	 */
	public void setDates (String d) {
		if (!d.equals("")) {
			if (isDates(d)) {
				String[] dates = d.split("-"); 
				this.setInitialDate(dates[0]);
				this.setEndDate(dates[1]);
			} else {
				System.err.println("Unexpected value for date: "+d);
				this.searchAndSet(d);
			}
		}
	}
	public boolean isDates(String d) {
		return d.contains("-");
	}
	
	public Date getInitialDate() {
		return initialDate;
	}

	public void setInitialDate(String initialDateStr) {
		if (this.initialDate == null) {
			this.initialDate = dateValue(initialDateStr);
		}
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDateStr) {
		if (this.endDate == null ) {
			this.endDate =  dateValue(endDateStr);
		}
	}
	
	private Date dateValue(String aDateStr) {
		Date d = null; 
		if (aDateStr.toLowerCase().contains("current")) {
			return null;
		} else {
			try {
				DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
				d = df.parse(aDateStr);
			} catch (ParseException pe) {
				//pe.printStackTrace();
				System.err.println("Date Parse Exception: value, " + d );
				if(!this.searchAndSet(aDateStr)) {
					d =null;
				}
			}
		}
		return d; 
	}

	public AccreditationStatus getAccreditationStatus() {
		return accreditationStatus;
	}

	public void setAccreditationStatus(String accreditationStatusStr) {
		if (this.accreditationStatus == null ) {
			if (accreditationStatusStr.equals("Accredited")){
				this.accreditationStatus = AccreditationStatus.Accredited;
			} else if (accreditationStatusStr.equals("Pre-Accredited")){
				this.accreditationStatus = AccreditationStatus.PreAccredited;
			}
		}
	}

	/*
	 * This method checks to see if a string represents an accreditation status.  
	 */
	public boolean isAcceditationStatus(String aStr) {
		boolean rc = false;
		try { 
			@SuppressWarnings("unused")
			AccreditationStatus tmp = AccreditationStatus.valueOf(aStr);
			rc = true; 
		}catch (IllegalArgumentException iae) {
			rc = false;
		}
		return rc; 
	}
	
	public AccreditationDataType getAccreditationDataType() {
		return this.accreditationDataType;
	}
	
	public void setAcceditationDatatype(String aAcceditationDatatypeStr) {
		if (this.accreditationDataType == null ) {
			try { 
				this.accreditationDataType =
					AccreditationDataType.valueOf(aAcceditationDatatypeStr);
			}catch (IllegalArgumentException iae) {
				System.err.println( aAcceditationDatatypeStr  + " data type not found.");
				if (!this.searchAndSet(aAcceditationDatatypeStr)) {
					this.accreditationDataType = null;
				}
			}
		}
	}
	
	/*
	 * This method checks to see if a string represents an action.  
	 */
	public boolean isAcceditationDatatype(String aStr) {
		boolean rc = false;
		try { 
			@SuppressWarnings("unused")
			AccreditationDataType tmp = AccreditationDataType.valueOf(aStr);
			rc = true; 
		}catch (IllegalArgumentException iae) {
			rc = false;
		}
		return rc; 
	}

	
	public ProgramAction getLastAction() {
		return lastAction;
	}

	public void setLastAction(String lastActionStr) {
		String cmp = lastActionStr.replaceAll("\\s+", "");
		try { 
			this.lastAction = ProgramAction.valueOf(cmp);
		}catch (IllegalArgumentException iae) {
			System.err.println(lastActionStr + " action not found.");
			if (!this.searchAndSet(lastActionStr)) {
				this.lastAction = null;
			}
		}
	}
	
	/*
	 * This method checks to see if a string represents an action.  
	 */
	public boolean isAction(String aStr) {
		boolean rc = false;
		try { 
			@SuppressWarnings("unused")
			ProgramAction tmp = ProgramAction.valueOf(aStr);
			rc = true; 
		}catch (IllegalArgumentException iae) {
			rc = false;
		}
		return rc; 
	}
	
	/*
	 * Found a value in a column which doesn't match. Sometimes they columns are off.
	 * See if it is a well-known value. If so, set it. 
	 */
	private boolean searchAndSet(String aStr) {
		boolean rc = true;
		if (isAction(aStr)) {
			System.err.println("\t" + aStr+ " is action");
			setLastAction(aStr);
		} else if (this.isAcceditationStatus(aStr)){
			System.err.println("\t" + aStr+ " is acceditation status");
			this.setAccreditationStatus(aStr);
		} else if(this.isAcceditationDatatype(aStr)) {
			System.err.println("\t" + aStr+ " is a data type.");
			this.setAcceditationDatatype(aStr); 
		} else if (this.isDates(aStr)) {
			System.err.println("\t" + aStr+ " is a date range");
			this.setDates(aStr);
		} else {
			System.err.println("Do not recognize the format: " + aStr );
			System.err.println("\tIgnoring and continuing.");
			rc = false; 
		}
		return rc; 
	}
}
