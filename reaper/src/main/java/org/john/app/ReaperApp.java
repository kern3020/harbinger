package org.john.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Harvests information from Dept of Ed 
 */
public class ReaperApp 
{
	static final Logger logger = LoggerFactory.getLogger(ReaperApp.class);
	final static String ACCREDITED_SCHOOLS_CSV="/home/jkern/workspace/harvester/data/Accreditation/Accreditation_2012_03.csv";
	
    public static void main( String[] args )
    {
    	ConfigurableApplicationContext context =null;
        logger.info("Initializing Reaper App" );
        try { 
        	context = new ClassPathXmlApplicationContext("META-INF/spring/applicationContext.xml");
        } catch (Exception e ) {
        	e.printStackTrace();
        }
        InstituteRepository instituteRepository = context.getBean(InstituteRepository.class);
        
        instituteRepository.dropCollection();
        
        instituteRepository.createCollection();
        //instituteRepository.insert("MSU", 48823);
        // instituteRepository.insert("UofM", 48103);
        instituteRepository.parseMe(ACCREDITED_SCHOOLS_CSV);
        instituteRepository.dump();
    }
}
