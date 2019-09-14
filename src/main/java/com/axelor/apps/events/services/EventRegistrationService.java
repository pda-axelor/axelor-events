package com.axelor.apps.events.services;

import java.math.BigDecimal;

import com.axelor.apps.events.db.Event;
import com.axelor.apps.events.db.EventRegistration;

public interface EventRegistrationService {

	public BigDecimal calculateAmount(Event event,EventRegistration eventRegistration);
 
}
