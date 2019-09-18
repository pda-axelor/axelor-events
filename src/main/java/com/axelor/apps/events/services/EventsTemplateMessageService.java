package com.axelor.apps.events.services;

import com.axelor.apps.events.db.Event;
import com.axelor.apps.events.db.EventRegistration;
import com.axelor.apps.events.db.repo.EventRepository;
import com.axelor.apps.message.db.Message;
import com.axelor.apps.message.db.Template;
import com.axelor.apps.message.service.MessageService;
import com.axelor.apps.message.service.TemplateMessageServiceImpl;
import com.axelor.db.Model;
import com.axelor.exception.AxelorException;
import com.axelor.inject.Beans;
import com.google.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.mail.MessagingException;

public class EventsTemplateMessageService extends TemplateMessageServiceImpl {
	@Inject
	public EventsTemplateMessageService(MessageService messageService) {
		super(messageService);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Message generateAndSendMessage(Model model, Template template) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, AxelorException, IOException, MessagingException {
		Event event = Beans.get(EventRepository.class).find(model.getId());
		List<String> emailsList = event.getEventRegistrationList().stream().filter(r -> r.getEmailSent() == false)
				.map(l -> l.getEmail()).collect(Collectors.toList());
		template.setSubject("Congratulations, You have been registered successfully for the Event!");
		template.setToRecipients(emailsList.toString().replace("[", "").replace("]", ""));
		return super.generateAndSendMessage(model, template);
	}
}
