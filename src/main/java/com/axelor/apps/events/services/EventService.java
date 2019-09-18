package com.axelor.apps.events.services;

import com.axelor.apps.events.db.Event;

public interface EventService {
  public void calculateTotal(Event event);
  public void importData(String dataFileName);
}
