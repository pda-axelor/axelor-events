package com.axelor.apps.events.web;

import com.axelor.apps.events.db.Event;
import com.axelor.apps.events.services.EventRegistrationService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class EventController {

  @Inject EventRegistrationService registrationService;

  public void setTotalAmount(ActionRequest request, ActionResponse response) {
      
       
  }
}
