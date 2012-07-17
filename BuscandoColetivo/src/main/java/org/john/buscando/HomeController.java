package org.john.buscando;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String theMap(HttpServletRequest req, Model model) {
		logger.info("home page ");
		String baseURL = req.getScheme() + "://" +
				req.getServerName() + ":" +
				req.getServerPort() + 
				req.getContextPath();
		
		model.addAttribute("baseURL", baseURL );
		
		return "home";
	}
	
}
