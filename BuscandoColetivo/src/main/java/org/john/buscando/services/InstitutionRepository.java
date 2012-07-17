package org.john.buscando.services;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.john.app.domain.AccreditedPostsecondaryInstitution;
import org.john.app.domain.StateAndCentroid;
import org.john.buscando.domain.StateCount;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.Box;
import org.springframework.data.mongodb.core.geo.Point;
import org.springframework.data.mongodb.core.mapreduce.GroupByResults;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

public class InstitutionRepository implements InstitutionDao {

    private MongoTemplate mongoTemplate;
    
	public InstitutionRepository(MongoTemplate mongoTemplate) {
		super();
		this.mongoTemplate = mongoTemplate;
	}
	
	public InstitutionRepository() {
		super();
	}

	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
	public List<AccreditedPostsecondaryInstitution> findBounded(
			String latSW, String lngSW, 
			String latNE, String lngNE) {
//		Criteria c = where("location").within(
//					new Box(
//							new Point(Double.valueOf(latNE), 
//									Double.valueOf(lngNE)),
//							new Point(Double.valueOf(latSW),
//									Double.valueOf(lngSW))
//							));
//		System.out.println("criteria: " + c.getCriteriaObject().toString());
		List<AccreditedPostsecondaryInstitution> poi = mongoTemplate.find(
			query(where("location").within(
					new Box(
						new Point(Double.valueOf(latNE), 
								Double.valueOf(lngNE)),
						new Point(Double.valueOf(latSW),
								Double.valueOf(lngSW))
						))).limit(300),
					AccreditedPostsecondaryInstitution.class);
		return poi;
	}
	
	/**
	 * With the following API call (ie, countPerState()), we would like
	 * to join the centroid and institutions collections to get both the 
	 * count and the centroid for each state.  Mongo doesn't support joins.
	 * We will implement the moral equivalent of one here. 
	 */
	
	class Count {
		String us_state; 
		int count;
		
		public String getUs_state() {
			return us_state;
		}
		
		public void setUs_state(String us_state) {
			this.us_state = us_state;
		}
		
		public int getCount() {
			return count;
		}
		
		public void setCount(int count) {
			this.count = count;
		}
	}
	
	public List<StateCount> countPerState() {
		Map<String,StateCount> map = new HashMap<String,StateCount>();
		GroupByResults<Count> results = null;
		try { 
			// Aggregate the number of institutions per state. 
			 results= 
				mongoTemplate.group("institutions", 
                        GroupBy.key("us_state").initialDocument("{ count: 0 }").reduceFunction("function(doc, prev) { prev.count += 1 }"), 
                        Count.class);
		} catch (Exception e ) {
			e.printStackTrace();
		}
		Iterator<Count> it = results.iterator();
		while (it.hasNext()) {
			Count c = it.next();
			StateCount s = new StateCount();
			s.setAbbrev(c.getUs_state());
			s.setCount(c.getCount());
			map.put(s.getAbbrev(), s);
		}
		 
		// retrieve centroids 
		List<StateAndCentroid> poi = mongoTemplate.findAll(StateAndCentroid.class,"centroid");
		Iterator<StateAndCentroid> cit = poi.iterator();
		while (cit.hasNext()) {
			StateAndCentroid sac = cit.next();
			StateCount s = map.get(sac.getAbbreviation());
			// add assert 
			if (s != null ) {
				double d = sac.getLat();
				s.setLat(d);
				d = sac.getLng();
				s.setLng(d);
			}
		}
		
				
 		return new ArrayList<StateCount>(map.values()); 
	}
}
