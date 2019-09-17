package com.axelor.apps.events.modules;

import com.axelor.app.AxelorModule;
import com.axelor.apps.events.services.EventRegistrationService;
import com.axelor.apps.events.services.EventRegistrationServiceImpl;
import com.axelor.apps.events.services.EventService;
import com.axelor.apps.events.services.EventServiceImpl;
import com.axelor.apps.events.services.EventsTemplateMessageService;
import com.axelor.apps.message.service.TemplateMessageServiceImpl;

public class EventsModule extends AxelorModule {
  @Override
  protected void configure() {

    bind(EventService.class).to(EventServiceImpl.class);
    bind(EventRegistrationService.class).to(EventRegistrationServiceImpl.class);
    bind(TemplateMessageServiceImpl.class).to(EventsTemplateMessageService.class);
  }
}
