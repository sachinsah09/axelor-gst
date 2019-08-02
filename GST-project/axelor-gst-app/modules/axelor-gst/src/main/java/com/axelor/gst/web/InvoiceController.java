package com.axelor.gst.web;

import java.io.File;
import com.axelor.app.AppSettings;
import com.axelor.gst.db.Address;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.service.InvoiceService;
import com.axelor.gst.service.SequenceService;
import com.axelor.inject.Beans;
import com.axelor.meta.db.MetaModel;
import com.axelor.meta.db.repo.MetaModelRepository;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class InvoiceController {

	@Inject
	private InvoiceService service;
	@Inject
	private SequenceService sequenceService;

	public void setInvoiceSequence(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);
		try {
			String sequenceNumber = null;
			if (invoice.getInvoiceSeq() == null) {
				MetaModel model = Beans.get(MetaModelRepository.class).findByName("Invoice");
				sequenceNumber = sequenceService.calculateSequenceNumber(model);
			} else {
				sequenceNumber = invoice.getInvoiceSeq();
			}
			response.setValue("invoiceSeq", sequenceNumber);
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
			e.printStackTrace();
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
			e.printStackTrace();
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
			e.printStackTrace();
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
			if ((invoice.getParty().getAddressList()).isEmpty()) {
				response.setNotify("Selected Party has no address");
			}
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
			e.printStackTrace();
		}
	}

	public void setPath(ActionRequest request, ActionResponse response) {
		String attachmentPath = AppSettings.get().getPath("file.upload.dir", "");
		attachmentPath = attachmentPath.endsWith(File.separator) ? attachmentPath : attachmentPath + File.separator;
		request.getContext().put("AttachmentPath", attachmentPath);
	}

	public void checkStatus(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);
		if (invoice.getStatus().equals("draft")) {
			response.setFlash("Draft Invoice Saved");
		} else if (invoice.getStatus().equals("validated")) {
			response.setFlash("Validated Invoice Saved");
		} else if (invoice.getStatus().equals("paid")) {
			response.setFlash("Paid Invoice Saved");
		} else {
			response.setFlash("Cancelled Invoice Saved");
		}
	}

	public void setProductItem(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);
		String idList = (String) request.getContext().get("idList");
		if (idList != null) {
			int partyId = (int) request.getContext().get("partyId");
			Invoice invoiceSetValue = service.setProductItem(invoice, idList, partyId);
			response.setValues(invoiceSetValue);
			if ((invoice.getParty().getAddressList()).isEmpty()) {
				response.setNotify("Selected Party has no address");
			}
		}
	}

	public void calulateValueOnAddressChange(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);
		try {
			Invoice invoiceCalculateValue = service.calulateValueOnAddressChange(invoice);
			response.setValue("invoiceItemsList", invoiceCalculateValue.getInvoiceItemsList());
			response.setValues(invoice);
			if ((invoice.getParty().getAddressList()).isEmpty()) {
				response.setNotify("Selected Party has no address");
			}
			if ((invoice.getCompany().getAddress()) == null) {
				response.setNotify("Selected Party has no address");
			}
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