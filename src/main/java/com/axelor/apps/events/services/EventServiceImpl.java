package com.axelor.apps.events.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import com.axelor.app.AppSettings;
import com.axelor.apps.events.db.Event;
import com.axelor.apps.events.db.EventRegistration;
import com.axelor.apps.events.db.repo.EventRegistrationRepository;
import com.axelor.apps.events.db.repo.EventRepository;
import com.axelor.data.Importer;
import com.axelor.data.csv.CSVImporter;
import com.axelor.exception.AxelorException;
import com.axelor.exception.db.repo.TraceBackRepository;
import com.axelor.inject.Beans;
import com.axelor.meta.MetaScanner;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class EventServiceImpl implements EventService {

	@Inject
	EventRepository eventRepo;

	@Inject
	EventRegistrationRepository eventRegistrationRepo;

	@Override
	@Transactional
	public boolean calculateTotal(Long eventId, List<EventRegistration> registrations) {

		BigDecimal totalAmount = BigDecimal.ZERO;
		boolean flag = false;
		Event event = Beans.get(EventRepository.class).find(eventId);
		if (registrations != null) {
			Optional<EventRegistration> temp = registrations.stream().filter(l -> l.getId() == null).findFirst();
			if (!temp.isPresent()) {
				flag = true;
			} else {
				EventRegistration r1 = temp.get();
				EventRegistration registration = new EventRegistration();
				registration.setEvent(event);
				registration.setName(r1.getName());
				registration.setEmailSent(false);
				registration.setRegistrationDate(r1.getRegistrationDate());
				registration.setAmount(r1.getAmount());
				eventRegistrationRepo.save(registration);
			}
		}

		if (flag == false) {
			List<EventRegistration> list = event.getEventRegistrationList();
			int entry = list.size();

			for (EventRegistration l : list) {
				totalAmount = totalAmount.add(l.getAmount());
			}
			event.setTotalEntry(entry);
			event.setAmountCollected(totalAmount);
			event.setTotalDiscount(event.getEventFees().multiply(BigDecimal.valueOf(entry)).subtract(totalAmount));
			eventRepo.save(event);
			return true;
		}
		return false;
	}

	@Override
	public void importData(String dataFileName, long id) throws Exception {

		String uploadDir = AppSettings.get().get("file.upload.dir");
		File dataFile = new File(uploadDir + "/" + dataFileName);

		File configFile = this.getConfigFile();
		Importer importer = new CSVImporter(configFile.getAbsolutePath(), dataFile.getParent());
		importer.run();
		configFile.delete();
		dataFile.delete();
		this.setRegistrations(id);
	}

	public File getConfigFile() throws Exception {
		List<URL> list = MetaScanner.findAll("events-config.xml");
		File configFile = new File("events-config.xml");
		byte[] b = new byte[1000];

		list.get(0).openStream().read(b);
		FileOutputStream outputStream = new FileOutputStream(configFile);
		outputStream.write(b);
		outputStream.close();

		return configFile;

	}

	@Transactional
	public void setRegistrations(long id) {
		Event event = Beans.get(EventRepository.class).find(id);

		List<EventRegistration> registrations = Beans.get(EventRegistrationRepository.class).all()
				.filter("event is null").fetch();
		System.out.println(registrations);
		for (EventRegistration l : registrations) {
			l.setEvent(event);
			eventRegistrationRepo.save(l);
		}
	}
}
