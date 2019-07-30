package com.axelor.gst.service;

import com.axelor.gst.db.Party;
import com.axelor.inject.Beans;
import com.axelor.meta.db.MetaModel;
import com.axelor.meta.db.repo.MetaModelRepository;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class PartyServiceImp implements PartyService {

	@Inject
	private SequenceService service;
	
	@Transactional
	public String setPartySequence(Party party) {
		String sequenceNumber = "";
		if (party.getPartySeq() == null) {
			MetaModel model = Beans.get(MetaModelRepository.class).findByName("Party");
			sequenceNumber=service.calculateSequenceNumber(model);
		} else {
			sequenceNumber = party.getPartySeq();
		}
		return sequenceNumber;
	}
}
