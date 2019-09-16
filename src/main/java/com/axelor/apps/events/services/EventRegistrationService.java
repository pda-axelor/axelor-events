package com.axelor.apps.events.services;

import com.axelor.apps.events.db.Event;
import com.axelor.apps.events.db.EventRegistration;
import java.math.BigDecimal;

public interface EventRegistrationService {

  public BigDecimal calculateAmount(Event event, EventRegistration eventRegistration);
}
