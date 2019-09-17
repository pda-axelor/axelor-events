package com.axelor.apps.events.web;

import com.axelor.apps.events.db.Event;
import com.axelor.apps.events.db.EventRegistration;
import com.axelor.apps.events.db.repo.EventRepository;
import com.axelor.apps.events.services.EventRegistrationService;
import com.axelor.apps.events.services.EventService;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class EventRegistrationController {

  @Inject EventService eventService;

  @Inject EventRegistrationService eventRegistrationService;

  public void setTotal(ActionRequest request, ActionResponse response) {
    EventRegistration eventRegistration = request.getContext().asType(EventRegistration.class);
    Event event = Beans.get(EventRepository.class).find(eventRegistration.getEvent().getId());
    eventService.calculateTotal(event);
  }

  public void setAmount(ActionRequest request, ActionResponse response) {
    EventRegistration eventRegistration = request.getContext().asType(EventRegistration.class);
    if (eventRegistration.getRegistrationDate() != null) {
      if (eventRegistration.getEvent() != null) {
        Event event = Beans.get(EventRepository.class).find(eventRegistration.getEvent().getId());
        response.setValue(
            "amount", eventRegistrationService.calculateAmount(event, eventRegistration));
      }
    }
  }
}
