package com.axelor.gst.web;

import com.axelor.db.JpaSupport;
import com.axelor.gst.db.Party;
import com.axelor.gst.service.PartyService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class PartyController extends JpaSupport {

	@Inject
	private PartyService service;

	public void setPartySequence(ActionRequest request, ActionResponse response) {
		Party party = request.getContext().asType(Party.class);
		try {
		String partySequenceNumber=service.setPartySequence(party);
		response.setValue("partySeq",partySequenceNumber);
		}catch(Exception e){
				System.out.println(e);
		}
	}
}
