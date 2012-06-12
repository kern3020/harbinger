package org.john.app.domain;

import org.john.app.domain.GeocoderPopulate;
import org.john.app.domain.Organization;
import org.junit.* ;
import static org.junit.Assert.* ;

/**
 * Unit test for simple Geocoder.
 */
public class GeocoderTest  {

	/**
	 * Given a organization with a name, populate it via the geocoder.
	 * Should expand to 
	 * http://maps.googleapis.com/maps/api/geocode/xml?address=Alabama+Southern+Community+College&sensor=false 
	 */
	@Test 
	public void justANameTest() {
		GeocoderPopulate geo = new GeocoderPopulate();
		Organization org = new Organization("Alabama Southern Community College");
		geo.parseMe(org);
		// expecting 
		// <lat>31.8893560</lat>
		// <lng>-87.7452687</lng>
		assertTrue(org.getFirstLat().equals("31.8893560"));
		assertTrue(org.getFirstLng().equals("-87.7452687"));
	}
	
	/**
	 * given an address, get lat/lng
	 * http://maps.googleapis.com/maps/api/geocode/xml?address=917+Missile+Road+Sheppard+AFB+TX+76311-2263&sensor=false
	 */
	
	@Test  
	public void anAddressTest() {
		GeocoderLocation geo = new GeocoderLocation(); 
		geo.parseMe("917 Missile Road Sheppard AFB TX 76311-2263");
		//<lat>33.9840670</lat>
		//<lng>-98.5119150</lng>
		assertTrue(geo.getLat().equals("33.9840670"));
		assertTrue(geo.getLng().equals("-98.5119150"));
	}
	

}
