package com.axelor.apps.events.modules;

import com.axelor.app.AxelorModule;
import com.axelor.apps.events.db.repo.EventRegistrationRepository;
import com.axelor.apps.events.services.EventRegistrationService;
import com.axelor.apps.events.services.EventRegistrationServiceImpl;
import com.axelor.apps.events.services.EventService;
import com.axelor.apps.events.services.EventServiceImpl;
import com.axelor.apps.events.services.ExtendedEventRegistrationRepository;

public class EventsModule extends AxelorModule {
  @Override
  protected void configure() {

	bind(EventService.class).to(EventServiceImpl.class);
    bind(EventRegistrationService.class).to(EventRegistrationServiceImpl.class);
    bind(EventRegistrationRepository.class).to(ExtendedEventRegistrationRepository.class);
  }
}
