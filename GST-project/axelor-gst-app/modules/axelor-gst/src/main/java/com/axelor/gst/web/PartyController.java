package com.axelor.gst.web;

import com.axelor.gst.db.Party;
import com.axelor.gst.service.SequenceService;
import com.axelor.inject.Beans;
import com.axelor.meta.db.MetaModel;
import com.axelor.meta.db.repo.MetaModelRepository;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class PartyController {

	@Inject
	private SequenceService sequenceService;

	public void setPartySequence(ActionRequest request, ActionResponse response) {
		Party party = request.getContext().asType(Party.class);
		try {
			String sequenceNumber = null;
			if (party.getPartySeq() == null) {
				MetaModel model = Beans.get(MetaModelRepository.class).findByName("Party");
				sequenceNumber = sequenceService.calculateSequenceNumber(model);
			} else {
				sequenceNumber = party.getPartySeq();
			}
			response.setValue("partySeq", sequenceNumber);
		} catch (Exception e) {
			response.setError("Model not Registered");
		}
	}
}
