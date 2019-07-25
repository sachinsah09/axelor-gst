package com.axelor.gst.web;

import com.axelor.gst.db.Sequence;
import com.axelor.gst.service.SequenceService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class SequenceController {

	@Inject
	private SequenceService service;

	public void calculateSequenceNumber(ActionRequest request, ActionResponse response) {
		Sequence sequence = request.getContext().asType(Sequence.class);
		try {
		 service.calculateSequenceNumber(sequence);
		}
		catch(Exception e) { 
			response.setError("Error while fetching ID");
		}
	}

}
