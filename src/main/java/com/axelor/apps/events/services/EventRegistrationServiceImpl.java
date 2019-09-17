package com.axelor.apps.events.services;

import com.axelor.apps.events.db.Discount;
import com.axelor.apps.events.db.Event;
import com.axelor.apps.events.db.EventRegistration;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class EventRegistrationServiceImpl implements EventRegistrationService {

  @Override
  public BigDecimal calculateAmount(Event event, EventRegistration eventRegistration) {
    List<Discount> list = event.getDiscount();
    BigDecimal discountAmount = BigDecimal.ZERO;
    LocalDate closeDate = event.getRegistrationClose();
    LocalDate registrationDate = eventRegistration.getRegistrationDate().toLocalDate();
    LocalDate discountStartDate = null;
    for (Discount l : list) {
      discountStartDate = closeDate.minusDays(l.getBeforeDays());
      if (registrationDate.isBefore(discountStartDate)
          || registrationDate.isEqual(discountStartDate))
        discountAmount = discountAmount.max(l.getDiscountAmount());
    }

    return event.getEventFees().subtract(discountAmount);
  }
}
