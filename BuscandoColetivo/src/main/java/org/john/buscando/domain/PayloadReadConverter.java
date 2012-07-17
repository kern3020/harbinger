package org.john.buscando.domain;

import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.springframework.core.convert.converter.Converter;

/**
 * Spring Data provides converters to allow you to select a subset of each
 * document from your query results.  
 * 
 * Here the website doesn't need to know about which programs offered by
 * the institution. So do not send those extra bits across the wire. 
 * 
 * @author John Kern
 *
 */

public class PayloadReadConverter implements Converter<DBObject, Payload> {
	public Payload convert(DBObject source) {
		Payload p = new Payload((ObjectId) source.get("_id"));
		p.setName((String) source.get("name"));
		p.setAddress((String) source.get("address"));
		p.setCity((String) source.get("city"));
		p.setState((String) source.get("us_state"));
		p.setZip((String) source.get("zipCode"));
		p.setWebsite((String) source.get("website"));
		p.setLocation((double[]) source.get("location"));
		return p;
	}
}
