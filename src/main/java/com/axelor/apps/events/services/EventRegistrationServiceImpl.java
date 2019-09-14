package com.axelor.apps.events.services;

import java.math.BigDecimal;
import java.util.List;

import com.axelor.apps.events.db.Discount;
import com.axelor.apps.events.db.Event;
import com.axelor.apps.events.db.EventRegistration;


public class EventRegistrationServiceImpl implements EventRegistrationService {

	@Override
	public BigDecimal calculateAmount(Event event, EventRegistration eventRegistration) {
		List<Discount> list = event.getDiscount();
		BigDecimal discountAmount=BigDecimal.ZERO;
		for (Discount l:list)
		{
			discountAmount = discountAmount.add(l.getDiscountAmount());
		}
		
		return event.getEventFees().subtract(discountAmount);
	}





}
