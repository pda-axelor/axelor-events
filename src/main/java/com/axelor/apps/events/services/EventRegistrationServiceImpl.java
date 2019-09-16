package com.axelor.apps.events.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.axelor.apps.events.db.Discount;
import com.axelor.apps.events.db.Event;
import com.axelor.apps.events.db.EventRegistration;

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
			if (registrationDate.isAfter(discountStartDate) || registrationDate.isEqual(discountStartDate))
				discountAmount = discountAmount.add(l.getDiscountAmount());
		}

		return event.getEventFees().subtract(discountAmount);
	}
}
