package com.axelor.apps.events.services;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.mail.MessagingException;

import com.axelor.apps.events.db.Event;
import com.axelor.apps.events.db.EventRegistration;
import com.axelor.apps.events.db.repo.EventRegistrationRepository;
import com.axelor.apps.events.db.repo.EventRepository;
import com.axelor.apps.events.exception.IExceptionMessage;
import com.axelor.apps.message.db.Message;
import com.axelor.apps.message.db.Template;
import com.axelor.apps.message.service.MessageService;
import com.axelor.apps.message.service.TemplateMessageServiceImpl;
import com.axelor.db.Model;
import com.axelor.exception.AxelorException;
import com.axelor.exception.db.repo.TraceBackRepository;
import com.axelor.inject.Beans;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class EventsTemplateMessageService extends TemplateMessageServiceImpl {
	@Inject
	EventRegistrationRepository eventRegistrationRepo;

	@Inject
	public EventsTemplateMessageService(MessageService messageService) {
		super(messageService);
		// TODO Auto-generated constructor stub
	}

	@Override
	@Transactional
	public Message generateAndSendMessage(Model model, Template template) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, AxelorException, IOException, MessagingException {
		Event event = Beans.get(EventRepository.class).find(model.getId());
		List<String> emailsList = event.getEventRegistrationList().stream().filter(r -> r.getEmailSent() == false && r.getEmail()!=null)
				.map(l -> l.getEmail()).collect(Collectors.toList());
		
		if(emailsList.isEmpty())		
		  throw new AxelorException(TraceBackRepository.CATEGORY_NO_VALUE,IExceptionMessage.EMPTY_EMAILS);
			
		template.setSubject("Congratulations, You have been registered successfully for the Event!");
		template.setToRecipients(emailsList.toString().replace("[", "").replace("]", ""));
		Message message = super.generateAndSendMessage(model, template);
		List<EventRegistration> registrations = null;
		if(message!=null)
		{
		 registrations = event.getEventRegistrationList().stream().filter(l->l.getEmail()!=null).collect(Collectors.toList());
		for(EventRegistration l:registrations)
		{
			l.setEmailSent(true);
			eventRegistrationRepo.save(l);
		}
		}
		
		return message;
	}
	
}
