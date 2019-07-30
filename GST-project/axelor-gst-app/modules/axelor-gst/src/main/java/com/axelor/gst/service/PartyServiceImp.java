package com.axelor.gst.service;

import com.axelor.gst.db.Party;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.inject.Beans;
import com.axelor.meta.db.MetaModel;
import com.axelor.meta.db.repo.MetaModelRepository;
import com.google.inject.persist.Transactional;

public class PartyServiceImp extends SequenceServiceImp implements PartyService {

	@Transactional
	public String setPartySequence(Party party) {

		String sequenceNumber = "";
		if (party.getPartySeq() == null) {
			// method 1 to find model id
//			long modelId;
//			modelId = JPA.all(MetaModel.class).filter("self.name = Party").fetchOne().getId();

			// method 2 to find model id
			MetaModel model = Beans.get(MetaModelRepository.class).findByName("Party");
			long modelId = model.getId();
			Sequence sequence = Beans.get(SequenceRepository.class).all().filter("self.model = ?" , modelId).fetchOne();
			sequenceNumber=calculateSequenceNumber(sequence);
		} else {
			sequenceNumber = party.getPartySeq();
		}
		return sequenceNumber;
	}
}
