package com.axelor.apps.events.services;

import com.axelor.app.AppSettings;
import com.axelor.apps.events.db.Event;
import com.axelor.apps.events.db.EventRegistration;
import com.axelor.apps.events.db.repo.EventRepository;
import com.axelor.data.Importer;
import com.axelor.data.csv.CSVImporter;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

public class EventServiceImpl implements EventService {

  @Inject EventRepository eventRepo;

  @Override
  @Transactional
  public void calculateTotal(Event event) {
    List<EventRegistration> list = event.getEventRegistrationList();
    int entry = list.size();
    BigDecimal totalAmount = BigDecimal.ZERO;
    for (EventRegistration l : list) {
      totalAmount = totalAmount.add(l.getAmount());
    }
    event.setTotalEntry(entry);
    event.setAmountCollected(totalAmount);
    event.setTotalDiscount(
        event.getEventFees().multiply(BigDecimal.valueOf(entry)).subtract(totalAmount));
    eventRepo.save(event);
  }

@Override
public void importData(String dataFileName) {
	String uploadDir = AppSettings.get().get("file.upload.dir");
	File dataFile = new File(uploadDir + "/" + dataFileName);
	File configFile = new File(
			"/home/axelor/Projects/ABS/Project ABS/abs-webapp/modules/abs/axelor-events/src/main/resources/demo/events-config.xml");
	Importer importer = new CSVImporter(configFile.getAbsolutePath(), dataFile.getParent());
	importer.run();
	dataFile.delete();
	
}
}
