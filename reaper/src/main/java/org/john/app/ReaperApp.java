package org.john.app;

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
        instituteRepository.parseMe(ACCREDITED_SCHOOLS_CSV);
        //instituteRepository.dump();
    }
}
