package org.john.buscando;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;

import org.john.buscando.domain.StateCount;
import org.john.buscando.services.InstitutionRepository;
import org.john.app.domain.GeocoderLocation;

/**
 * This is an web end point.  The browser calls it to resolve
 * ajax calls. 
 * 
 * @author John Kern
 */

@Controller
public class MapController {
	private static final Logger logger = LoggerFactory.getLogger(MapController.class);
	@Autowired
	private InstitutionRepository repo; 
	
	/**
	 * Show institutions on the map. 
	 */
	@RequestMapping(value ="/latSW/{latSW}/lngSW/{lngSW}/latNE/{latNE}/lngNE/{lngNE}/mapEm.json", method = RequestMethod.GET)
	public String getInstitutions(
			@PathVariable String latSW,
			@PathVariable String lngSW,
			@PathVariable String latNE,
			@PathVariable String lngNE,
			Model model) {
		logger.info("return institutions based on extent.");
		try {
			model.addAttribute("institutions", repo.findBounded(latSW, lngSW, latNE, lngNE));
		} catch (DataAccessException e ){
			e.printStackTrace();
		}
		return "json"; 
	}

	
	/**
	 * Given a zip or city return the lat/lng. 
	 * 
	 * @param CityOrZip
	 * @param model
	 * @return json
	 */
	
	@RequestMapping(value ="/CityOrZip/{CityOrZip}/centroid.json", 
			method = RequestMethod.GET)
	public String getCentriod(
			@PathVariable String CityOrZip,
			Model model) {
		logger.info("return location based on city or zip");
		GeocoderLocation geo = new GeocoderLocation();
		geo.parseMe(CityOrZip);
		model.addAttribute("location", geo);
		return "json"; 
	}
	
	/**
	 * Return number of institutions per state with state controid.  
	 * 
	 * @return json 
	 */
	
	@RequestMapping(value="/countsPerState.json",
			method= RequestMethod.GET)
	public String getCountsPerState(Model model) {
		logger.info("return number of institutions per state");
		try {
			List<StateCount> s = repo.countPerState();
			model.addAttribute("countsPerState", s);
		} catch (DataAccessException e ){
			e.printStackTrace();
		}
		return "json"; 
	}
	
}
