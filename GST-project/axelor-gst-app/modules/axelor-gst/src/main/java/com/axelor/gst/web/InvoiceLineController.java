package com.axelor.gst.web;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.service.InvoiceLineService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class InvoiceLineController {

	@Inject
	private InvoiceLineService service;

	public void calculatedFieldValue(ActionRequest request, ActionResponse response) {

		InvoiceLine invoiceLine = request.getContext().asType(InvoiceLine.class);
		Invoice invoice = request.getContext().getParent().asType(Invoice.class);
		try {
			InvoiceLine invoiceline = service.calculatedFieldValue(invoiceLine, invoice);
			response.setValue("netAmount", invoiceline.getNetAmount());
			response.setValue("igst", invoiceline.getIgst());
			response.setValue("sgst", invoiceline.getSgst());
			response.setValue("cgst", invoiceline.getCgst());
			response.setValue("grossAmount", invoiceline.getGrossAmount());
		} catch (Exception e) {
			if (invoice.getCompany() == null) {
				response.setNotify("Select Company");
			} else if (invoice.getCompany().getAddress() == null) {
				response.setNotify("Selected Company has no Address");
			} else if ((invoice.getCompany().getAddress().getState()) == null) {
				response.setNotify("Selected Company Address has no State");
			} else if (invoice.getParty() == null) {
				response.setNotify("Select Party");
			} else if ((invoice.getParty().getAddressList()).isEmpty()) {
				response.setNotify("Selected Party has no address");
			} else if (invoice.getInvoiceAddress() == null) {
				response.setNotify("Please select Invoice Address");
			} else if ((invoice.getInvoiceAddress().getState()) == null) {
				response.setNotify("Selected Invoice Address has no state");
			} 
		}
	}
}