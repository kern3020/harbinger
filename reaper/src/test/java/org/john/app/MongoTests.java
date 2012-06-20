package org.john.app;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import org.john.app.domain.AccreditedPostsecondaryInstitution;

/**
 * Learning about Mongo. Using this class to explore using java
 * to query the institute data. I choose to use a test, so 
 * that, it will have a place in the source base and, hopefully, 
 * this will help others learn these techniques too. 
 * 
 * @author John Kern
 */


public class MongoTests {
	
	// db.<collection>.find({"name": /College/,"us_state":"CA","gps":{$exists: true }}, {"programSet.agencyName":1} )
	// the counts is very dependent on the data set. 
	
	@Test
	public void test() {
    	ConfigurableApplicationContext context = null;
        try { 
        	context = new ClassPathXmlApplicationContext("META-INF/spring/applicationContext.xml");
        } catch (Exception e ) {
        	e.printStackTrace();
        	fail("This can't happen.");
        }
        List<AccreditedPostsecondaryInstitution> results = null;
        MongoTemplate mongoTemplate = (MongoTemplate)context.getBean("mongoTemplate");
        results = mongoTemplate.find(query(where("us_state").is("MA")), AccreditedPostsecondaryInstitution.class);
        assertEquals(results.size(),282);
        
        // > db.institutions.find({"name": /College/}).count()
        results = mongoTemplate.find(query(where("name").regex("College")), AccreditedPostsecondaryInstitution.class);
        System.out.println("College count: " + results.size());
        assertEquals(results.size(),3015);
	}
}
