package com.axelor.gst.web;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.axelor.app.AppSettings;
import com.axelor.gst.db.Address;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Product;
import com.axelor.gst.db.repo.ProductRepository;
import com.axelor.gst.service.InvoiceService;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class InvoiceController {

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

	public void setProductItem(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);
		String idList = (String) request.getContext().get("idList");
		System.out.println(idList);
		InvoiceLine invoiceLine = new InvoiceLine();
		List<InvoiceLine> invoiceItemList = new ArrayList<InvoiceLine>();		
		String[] items = idList.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "").split(",");
		long[] results = new long[items.length];
		for (int i = 0; i < items.length; i++) {
			try {
				results[i] = Integer.parseInt(items[i]);
				System.out.println(results[i]);
				Product product = Beans.get(ProductRepository.class).find(results[i]);
				System.out.println(product.getName());
				invoiceLine.setItem( "[" + product.getCode() + "] " + product.getName());
				invoiceLine.setPrice(product.getSalesPrice());
				invoiceLine.setHsbn(product.getHsbn());
				invoiceLine.setProduct(product);
				invoiceItemList.add(invoiceLine);
			} catch (NumberFormatException nfe) {
				response.setError("unable to fetch Ids");
			}	
		}
		invoice.setInvoiceItemsList(invoiceItemList);
		response.setValues(invoice);
	}
}
