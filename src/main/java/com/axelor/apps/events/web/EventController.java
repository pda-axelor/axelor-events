package com.axelor.apps.events.web;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

import com.axelor.apps.events.services.EventRegistrationService;
import com.axelor.i18n.I18n;
import com.axelor.meta.MetaFiles;
import com.axelor.meta.db.MetaFile;
import com.axelor.meta.db.repo.MetaFileRepository;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;
import com.google.common.io.Files;

public class EventController {
 
  @Inject EventRegistrationService registrationService;
  @Inject MetaFileRepository metaFileRepo;
   public void validateFile(ActionRequest request, ActionResponse response)
   {
	   MetaFile metaFile =
		        metaFileRepo.find(
		            Long.valueOf(((Map) request.getContext().get("$file")).get("id").toString()));
		    

/*		    if (Files.getFileExtension(metaFile.getFileName()).equals("csv")) {
		      File tmpFile = File.createTempFile("Import", ".log");

		      if (importDemoDataService.importDemoDataExcel(excelFile, tmpFile)) {
		        response.setFlash(I18n.get(IExceptionMessage.IMPORT_COMPLETED_MESSAGE));
		      } else {
		        response.setFlash(I18n.get(IExceptionMessage.INVALID_DATA_FORMAT_ERROR));
		      }

		      response.setAttr("$logFile", "hidden", false);
		      FileInputStream inStream = new FileInputStream(tmpFile);
		      response.setValue("$logFile", metaFiles.upload(inStream, "Import.log"));

		    } else {
		      response.setError(I18n.get(IExceptionMessage.VALIDATE_FILE_TYPE));
		    }
		  }   */
   }

}
