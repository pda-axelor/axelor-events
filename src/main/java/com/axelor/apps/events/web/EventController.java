package com.axelor.apps.events.web;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;

import javax.mail.MessagingException;

import com.axelor.apps.events.db.Event;
import com.axelor.apps.events.services.EventRegistrationService;
import com.axelor.apps.events.services.EventService;
import com.axelor.apps.events.services.EventsTemplateMessageService;
import com.axelor.apps.message.db.Message;
import com.axelor.apps.message.db.Template;
import com.axelor.apps.message.db.repo.TemplateRepository;
import com.axelor.db.Model;
import com.axelor.exception.AxelorException;
import com.axelor.meta.db.repo.MetaFileRepository;
import com.axelor.meta.schema.actions.ActionView;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class EventController {

	@Inject
	EventRegistrationService registrationService;
	@Inject
	EventService eventService;
	@Inject
	MetaFileRepository metaFileRepo;
	@Inject
	private EventsTemplateMessageService eventTemplateMessageService;
	@Inject
	private TemplateRepository templateRepo;

	public void setEventRegistration(ActionRequest request, ActionResponse response) {

		Event eventContext = request.getContext().asType(Event.class);
		if (eventContext.getCapacity() <= eventContext.getTotalEntry()) {
			response.setFlash("Sorry, Registrations are Full for this Event");
			response.setReload(true);

		} else {
			response.setReload(
					eventService.calculateTotal(eventContext.getId(), eventContext.getEventRegistrationList()));
		}

	}

	public void validateFile(ActionRequest request, ActionResponse response) throws Exception {
		long eventId = Long.valueOf((Integer) request.getContext().get("_id"));
		@SuppressWarnings("rawtypes")
		LinkedHashMap metaFile = (LinkedHashMap) request.getContext().get("file");
		if (metaFile == null) {
			response.setError("Please select a File First");
			response.setReload(true);
		} else if (!(metaFile.get("fileType").toString().equals("text/csv"))) {
			response.setError("A CSV File must be selected");
			response.setReload(true);
		} else {
			try {
				eventService.importData((String) metaFile.get("file_path"), eventId);
				response.setReload(true);
			} catch (IndexOutOfBoundsException e) {
				response.setFlash("Please specify a config file first");
			}
		}
	}

	public void sendEmails(ActionRequest request, ActionResponse response) throws MessagingException {
		Model model = request.getContext().asType(Model.class);
		Template template = templateRepo.all()
				.filter("self.metaModel.fullName = ?1 AND self.isSystem != true", request.getModel()).fetchOne();

		try {
			if (template != null) {
				Message message = eventTemplateMessageService.generateAndSendMessage(model, template);
				response.setView(ActionView.define("Message").model(Message.class.getName()).add("form", "message-form")
						.context("_showRecord", String.valueOf(message.getId())).map());
			} else {
				response.setFlash("No Template Found for Event");
			}
		} catch (ClassNotFoundException e) {
			response.setAlert(e.getMessage());
		} catch (InstantiationException e) {
			response.setAlert(e.getMessage());
		} catch (IllegalAccessException e) {
			response.setAlert(e.getMessage());
		} catch (AxelorException e) {
			response.setAlert(e.getMessage());
		} catch (IOException e) {
			response.setAlert(e.getMessage());
		}
	}
}
