package com.axelor.apps.events.services;

import com.axelor.apps.events.db.Event;
import com.axelor.apps.events.db.EventRegistration;
import com.axelor.apps.events.db.repo.EventRegistrationRepository;
import com.axelor.apps.events.db.repo.EventRepository;
import com.axelor.apps.events.exception.IExceptionMessage;
import com.axelor.inject.Beans;
import java.time.LocalDate;
import javax.persistence.PersistenceException;

public class ExtendedEventRegistrationRepository extends EventRegistrationRepository {

  @Override
  public EventRegistration save(EventRegistration eventRegistration) {
    try {
      Event event = Beans.get(EventRepository.class).find(eventRegistration.getEvent().getId());
      LocalDate today = LocalDate.now();
      System.out.println(
          today.isBefore(event.getRegistrationOpen())
              && today.isAfter(event.getRegistrationOpen()));
      if (event.getCapacity() < event.getTotalEntry()) {
        throw new Exception();
      } else if (today.isBefore(event.getRegistrationOpen())) {
        throw new Exception();
      } else if (today.isAfter(event.getRegistrationClose())) {
        throw new Exception();
      } else {
        return super.save(eventRegistration);
      }
    } catch (Exception e) {
      throw new PersistenceException(IExceptionMessage.REGISTRATION_FULL);
    }
  }
}
