package com.axelor.gst.web;

import java.math.BigDecimal;
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
			response.setError("Please Select Party");
		}
	}

	public void setProductName(ActionRequest request, ActionResponse response) {
		InvoiceLine invoiceLine = request.getContext().asType(InvoiceLine.class);
		try {
			String productName = service.setProductName(invoiceLine);
			response.setValue("item", productName);
		} catch (Exception e) {
			response.setError("cannot fetch address");
		}
	}

	public void setGstRate(ActionRequest request, ActionResponse response) {
		InvoiceLine invoiceLine = request.getContext().asType(InvoiceLine.class);
		try {
			BigDecimal gstRate = service.setGstRate(invoiceLine);
			response.setValue("gstRate", gstRate);
		} catch (Exception e) {
			response.setError("cannot fetch GST rate");
		}
	}

	public void setPrice(ActionRequest request, ActionResponse response) {
		InvoiceLine invoiceLine = request.getContext().asType(InvoiceLine.class);
		try {
			BigDecimal price = service.setPrice(invoiceLine);
			response.setValue("price", price);
		} catch (Exception e) {
			response.setError("cannot fetch price");
		}
	}

	public void setHsbn(ActionRequest request, ActionResponse response) {
		InvoiceLine invoiceLine = request.getContext().asType(InvoiceLine.class);
		try {
			String hsbn = service.setHsbn(invoiceLine);
			response.setValue("hsbn", hsbn);
		} catch (Exception e) {
			response.setError("cannot fetch hsbn");
		}
	}

}