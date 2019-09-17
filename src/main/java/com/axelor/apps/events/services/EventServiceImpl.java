package com.axelor.apps.events.services;

import com.axelor.apps.events.db.Event;
import com.axelor.apps.events.db.EventRegistration;
import com.axelor.apps.events.db.repo.EventRepository;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import java.math.BigDecimal;
import java.util.List;

public class EventServiceImpl implements EventService {

  @Inject EventRepository eventRepo;

  @Override
  @Transactional
  public void calculateTotal(Event event) {
    List<EventRegistration> list = event.getEventRegistrationList();
    int entry = list.size();
    BigDecimal totalAmount = BigDecimal.ZERO;
    for (EventRegistration l : list) {
      totalAmount = totalAmount.add(l.getAmount());
    }
    event.setTotalEntry(entry);
    event.setAmountCollected(totalAmount);
    event.setTotalDiscount(
        event.getEventFees().multiply(BigDecimal.valueOf(entry)).subtract(totalAmount));
    eventRepo.save(event);
  }
}
