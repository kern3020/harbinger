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

import java.util.List;
import java.util.LinkedList;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.URLConnection;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;



/**
 * 
 * Given an organization with a name, this class calls Googles geocoder 
 * and populates everything (e.g., street address, state, zip, etc). 
 * It was designed to georeference educational institutions. 
 * 
 * Sometimes we already have the address but still need to georeference
 * the institution. In this case, use {@link GeocoderLocation }
 * 
 * @author John Kern
 *
 */



public class GeocoderPopulate extends Geocoder implements ContentHandler {	

	private Places campus; 
	private String shortName; 
	private Organization mOrg; 
	
	public GeocoderPopulate() {

		this.initializeDoc();
		this.initializeCampus();
	}

	private void initializeDoc() { 
		inDocument=false;
		inStatus=false; 

	}
	
	private void initializeCampus() {
		inResult=false;
		inAddressComponent=false;
		inShortName=false; 
		inType=false; 
		inLocation=false;
		inLat=false;
		inLng=false;
		campus=null;
		shortName=null;
	}
	
	//@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (inStatus) {
			String status = new String(ch,start,length);
			if (!status.equals("OK")){
				//throws new Exception("Invalid status");
			}
		} else if (inAddressComponent && inShortName) {
			this.shortName = new String(ch,start,length);
		} else if (inAddressComponent && inType) { 
			String t = new String(ch,start,length);
			if (t.equals(TYPE_CITY)) {
				campus.setCity(this.shortName); 
			} else if (t.equals(TYPE_COUNTY)) {
				campus.setCounty(this.shortName);
			} else if (t.equals(TYPE_ROUTE)) {
				campus.setAddress(TYPE_ROUTE,this.shortName); 
			} else if (t.equals(TYPE_STREET)) {
				campus.setAddress(TYPE_STREET,this.shortName); 
			} else if(t.equals(TYPE_STATE)) {
				campus.setUsState(this.shortName);
			} else if (t.equals(TYPE_ZIPCODE)) {
				campus.setZipCode(this.shortName);
			} else if (t.equals(TYPE_NAME)) {
				campus.setName(this.shortName);
			}
			
		} else if (inLocation && inLat) {
			campus.setLat(new String(ch,start,length));
		} else if (inLocation && inLng) {
			campus.setLng(new String(ch,start,length));
		}
	}

	//@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		
	}

	//@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (qName.equals(TAG_GEOCODE)) {
			inDocument=false; 
		} else if (qName.equals(TAG_STATUS)) {
			this.inStatus = false; 
		} else if (qName.equals(TAG_RESULT)) { 
			this.inResult=false;
			mOrg.add(campus);
		} else if (qName.equals(TAG_ADDRESS_COMPONENT)) {
			inAddressComponent=false;
		} else if (qName.equals(TAG_SHORT_NAME) ) {
			inShortName=false;
		} else if (qName.equals(TAG_TYPE) ) {
			inType=false;
		} else if (qName.equals(TAG_LOCATION)) {
			inLocation=false;
		} else if (qName.equals(TAG_LAT)) {
			inLat=false;
		} else if (qName.equals(TAG_LNG)) {
			inLng=false;
		}  
	}

	// TODO: why does the project break if I use the Override annotation.
	// it should be defined in the ContentHandler interface. 
	//@Override
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		if (qName.equals(TAG_GEOCODE)) {
			inDocument=true; 
		} else if (qName.equals(TAG_STATUS)) {
			this.inStatus = true; 
		} else if (qName.equals(TAG_RESULT)) {
			inResult=true;
			this.campus = new Places(Places.PlaceType.CAMPUS); 
		} else if (qName.equals(TAG_ADDRESS_COMPONENT)) {
			inAddressComponent=true;
		} else if (qName.equals(TAG_SHORT_NAME) ) {
			inShortName=true;
		} else if (qName.equals(TAG_TYPE) ) {
			inType=true;
		} else if (qName.equals(TAG_LOCATION)) {
			inLocation=true;
		} else if (qName.equals(TAG_LAT)) {
			inLat=true;
		} else if (qName.equals(TAG_LNG)) {
			inLng=true;
		}  
	}
	
	//@Override
	public void endPrefixMapping(String prefix) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	//@Override
	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		
	}

	//@Override
	public void processingInstruction(String target, String data)
			throws SAXException {
		// TODO Auto-generated method stub
		
	}

	//@Override
	public void setDocumentLocator(Locator locator) {
		// TODO Auto-generated method stub
		
	}

	//@Override
	public void skippedEntity(String name) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	//@Override
	public void startDocument() throws SAXException {
		
	}


	//@Override
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		// TODO Auto-generated method stub
		
	}

	
	public void parseMe(Organization aOrg) {
		mOrg = aOrg; 
		XMLReader reader = null;
		try { 
			String webUrl = GeocoderPopulate.CC_BASE_URL +
					URLEncoder.encode(mOrg.getName(),"UTF-8") + GeocoderPopulate.CC_URL_SUFFIX;
			URL url = new URL(webUrl);
			URLConnection connect = url.openConnection();
			InputStream in = connect.getInputStream(); 
	    	SAXParserFactory spf = SAXParserFactory.newInstance();

	    	SAXParser parser = spf.newSAXParser();
	    	reader = parser.getXMLReader();

	    	reader.setContentHandler(this);	    	    	
	    	reader.parse(new InputSource(in));
	    
	    } catch (ParserConfigurationException pce) {
	    	pce.printStackTrace();
	    } catch (SAXParseException spe) {
	    	spe.printStackTrace();
	    } catch (SAXException se) {
	    	se.printStackTrace();
		} catch (MalformedURLException male) {
			male.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	final public static void main(String args[]) {
		GeocoderPopulate geo = new GeocoderPopulate();
		List<Organization> ccList = new LinkedList<Organization>(); 
		ccList.add(new Organization("Alabama Southern Community College"));
		ccList.add(new Organization("Bevill State Community College"));
		ccList.add(new Organization("South Florida Community College"));
		for (Organization cc : ccList ){
			geo.parseMe(cc);
		}
		for (Organization cc : ccList ){
			System.out.println(cc);
		}
	}
}
