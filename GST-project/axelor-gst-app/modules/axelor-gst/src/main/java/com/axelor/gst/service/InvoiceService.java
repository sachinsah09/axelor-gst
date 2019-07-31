package com.axelor.gst.service;

import com.axelor.gst.db.Address;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;

public interface InvoiceService {
	public String setInvoiceSequence(Invoice invoice);
	public Contact setInvoicePartyPrimaryContact(Invoice invoice);
	public Address setInvoicePartyAddress(Invoice invoice);
	public Address setInvoiceShippingAddress(Invoice invoice);
	public Invoice invoiceCalculateFieldValue(Invoice invoice);
	public Invoice setProductItem(Invoice invoice,String idList,String partyName);
	public Invoice calulateValueOnAddressChange(Invoice invoice);
}
