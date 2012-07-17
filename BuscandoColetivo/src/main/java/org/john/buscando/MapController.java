package org.john.buscando;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;

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
	 * @return
	 */
	
	@RequestMapping(value ="/CityOrZip/{CityOrZip}/centroid.json", method = RequestMethod.GET)
	public String getCentriod(
			@PathVariable String CityOrZip,
			Model model) {
		logger.info("return location based on city or zip");
		GeocoderLocation geo = new GeocoderLocation();
		geo.parseMe(CityOrZip);
		model.addAttribute("location", geo);
		return "json"; 
	}
}
