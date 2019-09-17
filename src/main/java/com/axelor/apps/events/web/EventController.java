package com.axelor.apps.events.web;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axelor.apps.events.services.EventRegistrationService;
import com.axelor.apps.events.services.EventsTemplateMessageService;
import com.axelor.apps.message.db.Message;
import com.axelor.apps.message.db.Template;
import com.axelor.apps.message.db.repo.TemplateRepository;
import com.axelor.db.Model;
import com.axelor.exception.AxelorException;
import com.axelor.meta.db.MetaFile;
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
	MetaFileRepository metaFileRepo;
	@Inject
	private EventsTemplateMessageService eventTemplateMessageService;
	@Inject
	private TemplateRepository templateRepo;

	public void validateFile(ActionRequest request, ActionResponse response) {
		System.out.println(request.getContext().entrySet());
		MetaFile metaFile = metaFileRepo
				.find(Long.valueOf(((Map) request.getContext().get("$file")).get("id").toString()));

		/*
		 * if (Files.getFileExtension(metaFile.getFileName()).equals("csv")) { File
		 * tmpFile = File.createTempFile("Import", ".log");
		 *
		 * if (importDemoDataService.importDemoDataExcel(excelFile, tmpFile)) {
		 * response.setFlash(I18n.get(IExceptionMessage.IMPORT_COMPLETED_MESSAGE)); }
		 * else {
		 * response.setFlash(I18n.get(IExceptionMessage.INVALID_DATA_FORMAT_ERROR)); }
		 *
		 * response.setAttr("$logFile", "hidden", false); FileInputStream inStream = new
		 * FileInputStream(tmpFile); response.setValue("$logFile",
		 * metaFiles.upload(inStream, "Import.log"));
		 *
		 * } else { response.setError(I18n.get(IExceptionMessage.VALIDATE_FILE_TYPE)); }
		 * }
		 */

	}

	public void sendEmails(ActionRequest request, ActionResponse response) {
		Model model = request.getContext().asType(Model.class);
		Template template = templateRepo.all().filter("self.metaModel.fullName = ?1 AND self.isSystem != true", request.getModel())
				.fetchOne();

		try {
			if (template != null) {
				Message message = eventTemplateMessageService.generateMessage(model, template);
                response.setView(ActionView.define("Message")
                        .model(Message.class.getName())
                        .add("form", "message-form")   
                        .context("_showRecord", String.valueOf(message.getId()))
                        .map());
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
