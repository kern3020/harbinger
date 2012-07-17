package org.john.buscando.domain;

/**
 * This class represents the data to be return to the client 
 * from a web services query.
 * 
 * @author John Kern
 */

import org.bson.types.ObjectId;

public class Payload {
	
	private final ObjectId id;
	String name;
	String address;
	String city;
	String state;
	String zip;
	double [] location; 
	String website;
	
	public Payload() {
		super();
		this.id= new ObjectId();
	}


	public Payload(ObjectId id) {
		super();
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Payload other = (Payload) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}


	public String getZip() {
		return zip;
	}


	public void setZip(String zip) {
		this.zip = zip;
	}


	public double[] getLocation() {
		return location;
	}


	public void setLocation(double[] location) {
		this.location = location;
	}


	public String getWebsite() {
		return website;
	}


	public void setWebsite(String website) {
		this.website = website;
	}
	 
}

