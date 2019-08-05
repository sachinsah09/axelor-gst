package com.axelor.gst.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.axelor.gst.db.Address;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.Product;
import com.axelor.gst.db.repo.AddressRepository;
import com.axelor.gst.db.repo.ContactRepository;
import com.axelor.gst.db.repo.ProductRepository;
import com.axelor.gst.repository.PartyRepository;
import com.axelor.inject.Beans;
import com.google.inject.Inject;

public class InvoiceServiceImp implements InvoiceService {

	@Inject
	private InvoiceLineService invoiceLineService;
	private AddressRepository addressRepository;
	private ContactRepository contactRepository;

	@Override
	public Contact setInvoicePartyPrimaryContact(Invoice invoice) {
		Contact setInvoicePartyPrimaryContact = null;
		Party party = invoice.getParty();
		for (Contact contact : party.getContactList()) {
			if (contact.getType().equals(ContactRepository.CONTACT_TYPE_PRIMARY)) {
				setInvoicePartyPrimaryContact = contact;
			}
		}
		return setInvoicePartyPrimaryContact;
	}

	@Override
	public Address setInvoicePartyAddress(Invoice invoice) {
		Address setInvoicePartyAddress = null;
		Party party = invoice.getParty();
		for (Address address : party.getAddressList()) {
			if (address.getType().equals(AddressRepository.ADDRESS_TYPE_INVOICE)) {
				setInvoicePartyAddress = address;
			}
		}
		if (setInvoicePartyAddress == null) {
			for (Address address : party.getAddressList()) {
				if (address.getType().equals(AddressRepository.ADDRESS_TYPE_DEFAULT)) {
					setInvoicePartyAddress = address;
				}
			}
		}
		return setInvoicePartyAddress;
	}

	@Override
	public Address setInvoiceShippingAddress(Invoice invoice) {
		Address setInvoiceShippingAddress = null;
		Party party = invoice.getParty();
		if (invoice.getIsInvoiceAddressAsShippingAddress() == false) {
			for (Address address : party.getAddressList()) {
				if (address.getType().equals(AddressRepository.ADDRESS_TYPE_SHIPPING)) {
					setInvoiceShippingAddress = address;
				}
			}
			if (setInvoiceShippingAddress == null) {
				for (Address address : party.getAddressList()) {
					if (address.getType().equals(AddressRepository.ADDRESS_TYPE_DEFAULT)) {
						setInvoiceShippingAddress = address;
					}
				}
			}
		} else {
			setInvoiceShippingAddress = invoice.getInvoiceAddress();
		}
		return setInvoiceShippingAddress;
	}

	@Override
	public Invoice invoiceCalculateFieldValue(Invoice invoice) {
		BigDecimal netAmount = BigDecimal.ZERO;
		BigDecimal netCgst = BigDecimal.ZERO;
		BigDecimal netSgst = BigDecimal.ZERO;
		BigDecimal netIgst = BigDecimal.ZERO;
		BigDecimal grossAmount = BigDecimal.ZERO;
		for (InvoiceLine invoiceLine : invoice.getInvoiceItemsList()) {
			netAmount = netAmount.add(invoiceLine.getNetAmount());
			netCgst = netCgst.add(invoiceLine.getCgst());
			netSgst = netSgst.add(invoiceLine.getSgst());
			netIgst = netIgst.add(invoiceLine.getIgst());
		}
		grossAmount = netAmount.add(netIgst).add(netSgst).add(netCgst);
		invoice.setNetAmount(netAmount);
		invoice.setNetCgst(netCgst);
		invoice.setNetIgst(netIgst);
		invoice.setNetSgst(netSgst);
		invoice.setGrossAmount(grossAmount);
		return invoice;
	}

	@Override
	public Invoice setProductItem(Invoice invoice, String idList, int partyId) {
		if (idList != null) {
			Party party = Beans.get(PartyRepository.class).all().filter("self.id = ?", partyId).fetchOne();
			invoice.setParty(party);
			invoice.setInvoiceAddress(setInvoicePartyAddress(invoice));
			setInvoicePartyAddress(invoice);
			List<InvoiceLine> invoiceItemList = new ArrayList<InvoiceLine>();
			String[] items = idList.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "").split(",");
			long[] results = new long[items.length];
			for (int i = 0; i < items.length; i++) {
				results[i] = Integer.parseInt(items[i]);
				InvoiceLine invoiceLine = new InvoiceLine();
				Product product = Beans.get(ProductRepository.class).find(results[i]);
				invoiceLine.setItem("[" + product.getCode() + "] " + product.getName());
				invoiceLine.setPrice(product.getSalesPrice());
				invoiceLine.setHsbn(product.getHsbn());
				invoiceLine.setGstRate(product.getGstRate());
				invoiceLine.setProduct(product);
				invoiceLine.setNetAmount(product.getSalesPrice());
				invoiceLine = invoiceLineService.calculatedFieldValue(invoiceLine, invoice);
				invoiceItemList.add(invoiceLine);
			}
			invoice.setInvoiceItemsList(invoiceItemList);
			invoice = invoiceCalculateFieldValue(invoice);
		}
		return invoice;
	}

	@Override
	public Invoice calulateValueOnAddressChange(Invoice invoice) {
		List<InvoiceLine> invoiceItemList = new ArrayList<InvoiceLine>();
		if (invoice.getInvoiceItemsList() != null) {
			for (InvoiceLine invoiceLine : invoice.getInvoiceItemsList()) {
				invoiceLine = invoiceLineService.calculatedFieldValue(invoiceLine, invoice);
				invoiceItemList.add(invoiceLine);
			}
			invoice.setInvoiceItemsList(invoiceItemList);
			invoice = invoiceCalculateFieldValue(invoice);
		}
		return invoice;
	}
}
