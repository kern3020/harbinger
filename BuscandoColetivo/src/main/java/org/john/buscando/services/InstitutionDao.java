package org.john.buscando.services;

import java.util.List;
import org.john.buscando.domain.StateCount;
import org.john.app.domain.AccreditedPostsecondaryInstitution; 

public interface InstitutionDao {
	List<AccreditedPostsecondaryInstitution> findBounded(String latSW, String lngSW, String latNE, String lngNE);
	List<StateCount> countPerState();
}
