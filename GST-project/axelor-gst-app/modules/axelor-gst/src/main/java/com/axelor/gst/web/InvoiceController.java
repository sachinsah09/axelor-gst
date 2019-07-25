package com.axelor.gst.web;

import java.io.File;

import com.axelor.app.AppSettings;
import com.axelor.db.JpaSupport;
import com.axelor.gst.db.Address;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.Contact;
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
			String invoiceSequenceNumber = service.setInvoiceSequence(invoice);
			response.setValue("invoiceSeq", invoiceSequenceNumber);
		} catch (Exception e) {
			response.setError("Model not Registered");
		}
	}

	public void setInvoicePartyPrimaryContact(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);
		try {
			if (invoice.getParty() == null) {
				response.setValue("partyContact", null);
			} else {
				Contact setInvoicePartyPrimaryContact = service.setInvoicePartyPrimaryContact(invoice);
				response.setValue("partyContact", setInvoicePartyPrimaryContact);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void setInvoicePartyAddress(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);
		try {
			if (invoice.getParty() == null) {
				response.setValue("invoiceAddress", null);
			} else {
				Address setInvoicePartyAddress = service.setInvoicePartyAddress(invoice);
				response.setValue("invoiceAddress", setInvoicePartyAddress);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void setInvoiceDefaultCompany(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);
		try {
			Company setInvoiceDefaultCompany = service.setInvoiceDefaultCompany(invoice);
			response.setValue("company", setInvoiceDefaultCompany);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void setInvoiceShippingAddress(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);
		try {
			if (invoice.getParty() == null) {
				response.setValue("shippingAddress", null);
			} else {
				Address setInvoiceShippingAddress = service.setInvoiceShippingAddress(invoice);
				response.setValue("shippingAddress", setInvoiceShippingAddress);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void invoiceCalculateFieldValue(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);
		try {
			invoice = service.invoiceCalculateFieldValue(invoice);
			response.setValue("netAmount", invoice.getNetAmount());
			response.setValue("netIgst", invoice.getNetIgst());
			response.setValue("netSgst", invoice.getNetSgst());
			response.setValue("netCgst", invoice.getNetCgst());
			response.setValue("grossAmount", invoice.getGrossAmount());
		} catch (Exception e) {
			System.out.println(e);

		}
	}

	public void setPath(ActionRequest request, ActionResponse response) {
		String attachmentPath = AppSettings.get().getPath("file.upload.dir", "");
		attachmentPath = attachmentPath.endsWith(File.separator) ? attachmentPath : attachmentPath + File.separator;
		request.getContext().put("AttachmentPath", attachmentPath);
	}

	public void checkStatus(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);
		System.out.println(invoice.getStatus());
		if (invoice.getStatus().equals("1")) {
			response.setFlash("Draft Invoice Saved");
		} else if (invoice.getStatus().equals("2")) {
			response.setFlash("Validated Invoice Saved");
		} else if (invoice.getStatus().equals("3")) {
			response.setFlash("Paid Invoice Saved");
		} else {
			response.setFlash("Cancelled Invoice Saved");
		}
	}

}
