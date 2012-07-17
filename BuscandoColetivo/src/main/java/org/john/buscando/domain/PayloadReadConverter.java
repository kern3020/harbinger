package org.john.buscando.domain;

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
