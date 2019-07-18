package com.axelor.gst.web;

import com.axelor.db.JpaSupport;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.service.InvoiceService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class InvoiceController extends JpaSupport {

	@Inject
	private InvoiceService service;

	public void setInvoiceSequence(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);
		try {
		String invoiceSequenceNumber= service.setInvoiceSequence(invoice);
		response.setValue("invoiceSeq",invoiceSequenceNumber);
		}
		catch(Exception e) { 
			System.out.println(e);
		}
	}
}
