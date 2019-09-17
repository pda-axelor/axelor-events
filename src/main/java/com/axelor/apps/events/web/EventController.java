package com.axelor.apps.events.web;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axelor.app.AppSettings;
import com.axelor.apps.events.services.EventRegistrationService;
import com.axelor.apps.events.services.EventsTemplateMessageService;
import com.axelor.apps.message.db.Message;
import com.axelor.apps.message.db.Template;
import com.axelor.apps.message.db.repo.TemplateRepository;
import com.axelor.apps.tool.file.CsvTool;
import com.axelor.db.Model;
import com.axelor.exception.AxelorException;
import com.axelor.meta.db.MetaFile;
import com.axelor.meta.db.repo.MetaFileRepository;
import com.axelor.meta.schema.actions.ActionView;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

public class EventController {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Inject
	EventRegistrationService registrationService;
	@Inject
	MetaFileRepository metaFileRepo;
	@Inject
	private EventsTemplateMessageService eventTemplateMessageService;
	@Inject
	private TemplateRepository templateRepo;

	public void validateFile(ActionRequest request, ActionResponse response) throws IOException {
		
	/*	ObjectMapper mapper = new ObjectMapper();	    
		MetaFile metaFile = mapper.convertValue(request.getContext().get("file"), MetaFile.class);	
        File data = new File(metaFile.getFilePath());
		System.out.println(metaFile.getFilePath());
		
		Importer importer = new CSVImporter();

    */
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
