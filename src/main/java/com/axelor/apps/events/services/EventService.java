package com.axelor.apps.events.services;

import java.util.List;

import com.axelor.apps.events.db.EventRegistration;

public interface EventService {
	
  public boolean calculateTotal(Long eventId, List<EventRegistration> registrationsList);
  public void importData(String dataFileName,long id);

}
