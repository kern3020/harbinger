package org.john.buscando;

import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.dao.DataAccessException;

import org.john.buscando.services.InstitutionRepository;
import org.john.buscando.domain.StateCount;
import org.john.app.domain.AccreditedPostsecondaryInstitution;

/**
 * The service needs to provide Points of interest to the client. For the first
 * milestone, it will return all point of interest within a box. 
 * 
 * @author John Kern
 *
 */
public class MapQueryTest {

	/**
	 * Test method for {@link org.john.buscando.services.InstitutionRepository#findBounded(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	
	/**
	 * A converter should have been registered via the config file. Query mongo 
	 * and confirm as for results as a Payload.  
	 */
	
/**
 * BUG: currently this optimization fails. If I ask Mongo to return a PayLoad to Spring data, 
 * the null set is returned. This message is left in the mongo.log. 
 * Mon Jul  2 08:20:12 [conn3] command demo.$cmd command: { count: "institutions", query: { us_state: "MA" }, fields: {} } ntoreturn:1 reslen:48 275ms
 * Map it first, then we will optimize the payload. 
 */
	
	@Test
	public void testFindBounded() {
	   	ConfigurableApplicationContext context = null;
        try { 
        	context = new FileSystemXmlApplicationContext("src/main/webapp/WEB-INF/spring/appServlet/persistent-context.xml");
        } catch (Exception e ) {
        	e.printStackTrace();
        	fail("This can't happen.");
        }
        List<AccreditedPostsecondaryInstitution> results = null;
        try { 
        	MongoTemplate mongoTemplate = (MongoTemplate)context.getBean("mongoTemplate");
        	results = mongoTemplate.find(query(where("us_state").is("MA")), AccreditedPostsecondaryInstitution.class);
        } catch (DataAccessException e) {
        	e.printStackTrace();
        }
        
        assertEquals(results.size(),282);
	}
	
	@Test
	public void tryAJoin() { 
	   	ConfigurableApplicationContext context = null;
        try { 
        	context = new FileSystemXmlApplicationContext("src/main/webapp/WEB-INF/spring/appServlet/persistent-context.xml");
        } catch (Exception e ) {
        	e.printStackTrace();
        	fail("This can't happen.");
        }
        List<StateCount> results = null; 
        try {
        	InstitutionRepository repo = (InstitutionRepository)context.getBean("repo");
        	results = repo.countPerState();
        	Iterator<StateCount> it = results.iterator();
        	while (it.hasNext()) {
        		StateCount sc = it.next();
        		System.out.println(sc);
        	}
        } catch (DataAccessException dae ) {
        	dae.printStackTrace();
        }
        assertEquals(results.size(),59);
	}
}
