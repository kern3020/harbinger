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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

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
 * Sometimes we already have a great deal of information including
 * the address with respect to an institution. For example, the
 * US department of education provides a csv file with a wealth of 
 * information.  However, we still ned to geo-reference the 
 * institution. 
 * 
 * This class calls the Google's geocoder with the address. This class 
 * extracts the lat/lng. If you want to extract all the information based
 * on a name, see { @link GeocoderPopulate } 
 * 
 * @author John Kern
 *
 */

public class GeocoderLocation extends Geocoder  implements ContentHandler {
	
	// keeping lat/lng as strings to avoid round off errors. 
	private String lat;
	private String lng;
	
	public GeocoderLocation() {
		this.reset();
		
	}

	public void reset() { 
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
		lat=null;
		lng=null;
	}

	public String getLat() {
		return lat;
	}

	public String getLng() {
		return lng;
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (inStatus) {
			String status = new String(ch,start,length);
			if (!status.equals("OK")){
				//throws new Exception("Invalid status");
			}
		}  else if (inLocation && inLat) {
			lat = new String(ch,start,length);
		} else if (inLocation && inLng) {
			lng = new String(ch,start,length);
		}
		
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (qName.equals(TAG_GEOCODE)) {
			inDocument=false; 
		} else if (qName.equals(TAG_STATUS)) {
			this.inStatus = false; 
		} else if (qName.equals(TAG_RESULT)) { 
			this.inResult=false;
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

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processingInstruction(String target, String data)
			throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDocumentLocator(Locator locator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void skippedEntity(String name) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		if (qName.equals(TAG_GEOCODE)) {
			inDocument=true; 
		} else if (qName.equals(TAG_STATUS)) {
			this.inStatus = true; 
		} else if (qName.equals(TAG_RESULT)) {
			inResult=true;
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

	@Override
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		// TODO Auto-generated method stub
		
	}
	
	
	/**
	 * 
	 * @param aPlace - assumed to be an address. For example 
	 * " 
	 */
	public void parseMe(String aPlace) {
		XMLReader reader = null;
		try { 
			String webUrl = GeocoderPopulate.CC_BASE_URL +
					URLEncoder.encode(aPlace,"UTF-8") + GeocoderPopulate.CC_URL_SUFFIX;
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

}
