package com.axelor.apps.events.web;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.LinkedHashMap;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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

	public void validateFile(ActionRequest request, ActionResponse response) throws IOException {

		LinkedHashMap metaFile = (LinkedHashMap) request.getContext().get("file");				
		if (metaFile == null) {
			response.setError("Please select a File First");
			response.setReload(true);
		} else if (!(metaFile.get("fileType").toString().equals("text/csv"))) {
			response.setError("A CSV File must be selected");
			response.setReload(true);
		} else {
			eventService.importData((String)metaFile.get("file_path"));
			response.setReload(true);
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
			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AxelorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
