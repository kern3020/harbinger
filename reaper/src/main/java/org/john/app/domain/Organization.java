package org.john.app.domain;

import java.util.LinkedList;
import java.util.List;



/**
 * This class represents an organization which  may have multiple location.  
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
	String getFirstZip() {
		String rc  = null;
		if (size() > 0 ) {
			rc = campus.get(0).getZipCode();
		}
		return rc; 
	}
}
