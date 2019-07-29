package com.axelor.gst.service;

import com.axelor.gst.db.Party;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.inject.Beans;
import com.axelor.meta.db.MetaModel;
import com.axelor.meta.db.repo.MetaModelRepository;
import com.google.inject.persist.Transactional;

public class PartyServiceImp implements PartyService {

	@Transactional
	public String setPartySequence(Party party) {

		String sequenceNumber = "";
		if (party.getPartySeq() == null) {

			int addPaddingZero = 0;

			// method 1 to find model id
//			long modelId;
//			modelId = JPA.all(MetaModel.class).filter("self.name = Party").fetchOne().getId();

			// method 2 to find model id
			MetaModel model = Beans.get(MetaModelRepository.class).findByName("Party");
			long modelId = model.getId();
			long seqId = Beans.get(SequenceRepository.class).all().filter("self.model = " + modelId).fetchOne().getId();
			Sequence sequence = Beans.get(SequenceRepository.class).find(seqId);
			String prefix = sequence.getPrefix();
			String suffix = sequence.getSuffix();
			int padding = sequence.getPadding();
			int nextNumber = Integer.parseInt(sequence.getNextNumber());

			if (suffix == null) {
				suffix = "";
			}
			sequenceNumber = prefix;

			for (int i = 1; i < padding; i++) {
				sequenceNumber = sequenceNumber + addPaddingZero;
			}
			sequenceNumber = sequenceNumber + nextNumber + suffix;

			nextNumber++;
			String setNextNumber = "" + nextNumber;
			sequence.setNextNumber(setNextNumber);
			 Beans.get(SequenceRepository.class).save(sequence);
		} else {
			sequenceNumber = party.getPartySeq();
		}
		return sequenceNumber;
	}
}
