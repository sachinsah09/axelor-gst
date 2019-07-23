package com.axelor.gst.service;

import java.math.BigDecimal;
import java.util.List;
import com.axelor.db.JPA;
import com.axelor.gst.db.Address;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.Sequence;
import com.axelor.meta.db.MetaModel;
import com.google.inject.persist.Transactional;

public class InvoiceServiceImp implements InvoiceService {

	@Override
	@Transactional
	public String setInvoiceSequence(Invoice invoice) {

		String sequenceNumber = "";
		if (invoice.getInvoiceSeq() == null) {
			long modelId;
			int addPaddingZero = 0;

			// method 1 to find model id
			modelId = JPA.all(MetaModel.class).filter("self.id = 45").fetchOne().getId();

			// method 2 to find model id
			// MetaModel model = Beans.get(MetaModelRepository.class).findByName("Party");
			long seqId = JPA.all(Sequence.class).filter("self.model = " + modelId).fetchOne().getId();
			String prefix = JPA.all(Sequence.class).filter("self.model = " + modelId).fetchOne().getPrefix();
			String suffix = JPA.all(Sequence.class).filter("self.model = " + modelId).fetchOne().getSuffix();
			int padding = JPA.all(Sequence.class).filter("self.model = " + modelId).fetchOne().getPadding();
			int nextNumber = Integer
					.parseInt(JPA.all(Sequence.class).filter("self.model = " + modelId).fetchOne().getNextNumber());

			if (suffix == null) {
				suffix = "";
			}
			sequenceNumber = prefix;

			for (int i = 1; i < padding; i++) {
				sequenceNumber = sequenceNumber + addPaddingZero;
			}
			sequenceNumber = sequenceNumber + nextNumber + suffix;

			nextNumber++;
			String setNextNumber = "" + nextNumber;
			Sequence sequence = JPA.em().find(Sequence.class, seqId);
			sequence.setNextNumber(setNextNumber);
			JPA.em().persist(sequence);

		} else {
			sequenceNumber = invoice.getInvoiceSeq();
		}
		return sequenceNumber;
	}

	@Override
	public Contact setInvoicePartyPrimaryContact(Invoice invoice) {
		Contact setInvoicePartyPrimaryContact = null;
		Party party = invoice.getParty();
		long partyId = party.getId();
		List<Contact> partyContactList = JPA.all(Contact.class).filter("self.party = " + partyId).fetch();

		for (Contact contact : partyContactList) {
			if (contact.getType().equals("primary")) {
				setInvoicePartyPrimaryContact = JPA.em().find(Contact.class, contact.getId());
			}
		}
		return setInvoicePartyPrimaryContact;
	}

	@Override
	public Address setInvoicePartyAddress(Invoice invoice) {
		Address setInvoicePartyAddress = null;

		Party party = invoice.getParty();
		long partyId = party.getId();
		 List<Address> partyAddressList = JPA.all(Address.class).filter("self.party = " + partyId).fetch();

		for (Address address : partyAddressList) {
			if (address.getType().equals("default")) {
				setInvoicePartyAddress = JPA.em().find(Address.class, address.getId());
			} else if (address.getType().equals("invoice")) {
				setInvoicePartyAddress = JPA.em().find(Address.class, address.getId());
			}
		}
		return setInvoicePartyAddress;
	}

	@Override
	public Company setInvoiceDefaultCompany(Invoice invoice) {
		Company setInvoiceDefaultCompany = null;
		List<Company> companyList = JPA.all(Company.class).fetch();

		for (Company company : companyList) {
			if (company.getName().equals("Axelor pvt ltd")) {
				setInvoiceDefaultCompany = JPA.em().find(Company.class, company.getId());
			} else {
				setInvoiceDefaultCompany = JPA.em().find(Company.class, company.getId());
			}
		}
		return setInvoiceDefaultCompany;
	}

	@Override
	public Address setInvoiceShippingAddress(Invoice invoice) {
		Address setInvoiceShippingAddress = null;

		Party party = invoice.getParty();
		long partyId = party.getId();
		List<Address> partyAddressList = JPA.all(Address.class).filter("self.party = " + partyId).fetch();

		if (invoice.getIsInvoiceAddressAsShippingAddress() == true) {
			for (Address address : partyAddressList) {
				if (address.getType().equals("default")) {
					setInvoiceShippingAddress = JPA.em().find(Address.class, address.getId());
				} else if (address.getType().equals("shipping")) {
					setInvoiceShippingAddress = JPA.em().find(Address.class, address.getId());
				}
			}
		}
		return setInvoiceShippingAddress;
	}

	@Override
	public Invoice invoiceCalculateFieldValue(Invoice invoice) {

		long invoiceId = invoice.getId();
		List<InvoiceLine> invoiceLineList = JPA.all(InvoiceLine.class).filter("self.invoice = " + invoiceId).fetch();
		System.out.println(invoiceLineList);

		BigDecimal netAmount = new BigDecimal(0);
		BigDecimal netCgst = new BigDecimal(0);
		BigDecimal netSgst = new BigDecimal(0);
		BigDecimal netIgst = new BigDecimal(0);
		for (InvoiceLine invoiceLine : invoiceLineList) {
			netAmount = netAmount.add(invoiceLine.getNetAmount());
			netCgst = netCgst.add(invoiceLine.getCgst());
			netSgst = netSgst.add(invoiceLine.getSgst());
			netIgst = netIgst.add(invoiceLine.getIgst());
		}
		System.out.println("hello");
		System.out.println(netAmount);
		invoice.setNetAmount(netAmount);
		invoice.setNetCgst(netCgst);
		invoice.setNetIgst(netIgst);
		invoice.setNetSgst(netSgst);
		return invoice;
	}
}
