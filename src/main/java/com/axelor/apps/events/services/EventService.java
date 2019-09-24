package com.axelor.apps.events.services;

import java.util.List;

import com.axelor.apps.events.db.EventRegistration;
import com.axelor.exception.AxelorException;

public interface EventService {

	public boolean calculateTotal(Long eventId, List<EventRegistration> registrationsList) throws AxelorException;

	public void importData(String dataFileName, long id) throws Exception;
}
