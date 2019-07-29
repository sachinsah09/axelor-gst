package com.axelor.gst.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.axelor.gst.db.Address;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.Product;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.AddressRepository;
import com.axelor.gst.db.repo.CompanyRepository;
import com.axelor.gst.db.repo.ContactRepository;
import com.axelor.gst.db.repo.InvoiceLineRepository;
import com.axelor.gst.db.repo.ProductRepository;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.inject.Beans;
import com.axelor.meta.db.MetaModel;
import com.axelor.meta.db.repo.MetaModelRepository;
import com.google.inject.persist.Transactional;

public class InvoiceServiceImp implements InvoiceService {

	@Override
	@Transactional
	public String setInvoiceSequence(Invoice invoice) {

		String sequenceNumber = "";
		if (invoice.getInvoiceSeq() == null) {

			int addPaddingZero = 0;
//			long modelId;
			// method 1 to find model id
			// modelId = JPA.all(MetaModel.class).filter("self.name =
			// Invoice").fetchOne().getId();

			// method 2 to find model id
			MetaModel model = Beans.get(MetaModelRepository.class).findByName("Invoice");
			long modelId = model.getId();
			long seqId = Beans.get(SequenceRepository.class).all().filter("self.model = " + modelId).fetchOne().getId();
			Sequence sequence = Beans.get(SequenceRepository.class).find(seqId);
			String prefix = sequence.getPrefix();
			String suffix = sequence.getSuffix();
			int padding = sequence.getPadding();
			int nextNumber = Integer.parseInt(sequence.getNextNumber());

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
			Sequence sequenceUpdate = Beans.get(SequenceRepository.class).find(seqId);
			sequenceUpdate.setNextNumber(setNextNumber);
			Beans.get(SequenceRepository.class).persist(sequence);
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
		List<Contact> partyContactList = Beans.get(ContactRepository.class).all().filter("self.party = " + partyId)
				.fetch();
		for (Contact contact : partyContactList) {
			if (contact.getType().equals("primary")) {
				setInvoicePartyPrimaryContact = Beans.get(ContactRepository.class).find(contact.getId());
			}
		}
		return setInvoicePartyPrimaryContact;
	}

	@Override
	public Address setInvoicePartyAddress(Invoice invoice) {
		Address setInvoicePartyAddress = null;

		Party party = invoice.getParty();
		long partyId = party.getId();
		List<Address> partyAddressList = Beans.get(AddressRepository.class).all().filter("self.party = " + partyId)
				.fetch();

		for (Address address : partyAddressList) {
			if (address.getType().equals("default")) {
				setInvoicePartyAddress = Beans.get(AddressRepository.class).find(address.getId());
			} else if (address.getType().equals("invoice")) {
				setInvoicePartyAddress = Beans.get(AddressRepository.class).find(address.getId());
			}
		}
		return setInvoicePartyAddress;
	}

	@Override
	public Company setInvoiceDefaultCompany(Invoice invoice) {
		Company setInvoiceDefaultCompany = null;
		List<Company> companyList = Beans.get(CompanyRepository.class).all().fetch();

		for (Company company : companyList) {
			if (company.getName().equals("Axelor pvt ltd")) {
				setInvoiceDefaultCompany = Beans.get(CompanyRepository.class).find(company.getId());
			} else {
				setInvoiceDefaultCompany = Beans.get(CompanyRepository.class).find(company.getId());
			}
		}
		return setInvoiceDefaultCompany;
	}

	@Override
	public Address setInvoiceShippingAddress(Invoice invoice) {
		Address setInvoiceShippingAddress = null;
		Party party = invoice.getParty();
		long partyId = party.getId();
		List<Address> partyAddressList = Beans.get(AddressRepository.class).all().filter("self.party = " + partyId)
				.fetch();

		if (invoice.getIsInvoiceAddressAsShippingAddress() == true) {
			for (Address address : partyAddressList) {
				if (address.getType().equals("default")) {
					setInvoiceShippingAddress = Beans.get(AddressRepository.class).find(address.getId());
				} else if (address.getType().equals("shipping")) {
					setInvoiceShippingAddress = Beans.get(AddressRepository.class).find(address.getId());
				}
			}
		} else {
			for (Address address : partyAddressList) {
				if (address.getType().equals("invoice")) {
					setInvoiceShippingAddress = Beans.get(AddressRepository.class).find(address.getId());
				}
			}
		}
		return setInvoiceShippingAddress;
	}

	@Override
	public Invoice invoiceCalculateFieldValue(Invoice invoice) {

		long invoiceId = invoice.getId();
		List<InvoiceLine> invoiceLineList = Beans.get(InvoiceLineRepository.class).all()
				.filter("self.invoice = " + invoiceId).fetch();

		BigDecimal netAmount = new BigDecimal(0);
		BigDecimal netCgst = new BigDecimal(0);
		BigDecimal netSgst = new BigDecimal(0);
		BigDecimal netIgst = new BigDecimal(0);
		BigDecimal grossAmount = new BigDecimal(0);
		for (InvoiceLine invoiceLine : invoiceLineList) {
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
	public Invoice setProductItem(Invoice invoice, String idList, String partyName) {

		if (idList != null) {
			String companyState = invoice.getCompany().getAddress().getState().getName();
			Party party = Beans.get(PartyRepository.class).all().filter("self.name = '" + partyName + "'").fetchOne();
			invoice.setParty(party);
			List<Address> partyAddressList = Beans.get(AddressRepository.class).all()
					.filter("self.party = " + party.getId()).fetch();
			Address setInvoiceShippingAddress = null;
			if (invoice.getIsInvoiceAddressAsShippingAddress() == true) {
				for (Address address : partyAddressList) {
					if (address.getType().equals("default")) {
						setInvoiceShippingAddress = Beans.get(AddressRepository.class).find(address.getId());
					} else if (address.getType().equals("shipping")) {
						setInvoiceShippingAddress = Beans.get(AddressRepository.class).find(address.getId());
					}
				}
			}
			String partyAddress = setInvoiceShippingAddress.getState().getName();

			List<InvoiceLine> invoiceItemList = new ArrayList<InvoiceLine>();
			String[] items = idList.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "").split(",");
			long[] results = new long[items.length];

			for (int i = 0; i < items.length; i++) {

				try {
					results[i] = Integer.parseInt(items[i]);
					InvoiceLine invoiceLine = new InvoiceLine();
					Product product = Beans.get(ProductRepository.class).find(results[i]);
					invoiceLine.setItem("[" + product.getCode() + "] " + product.getName());
					invoiceLine.setPrice(product.getSalesPrice());
					invoiceLine.setHsbn(product.getHsbn());
					invoiceLine.setGstRate(product.getGstRate());
					invoiceLine.setProduct(product);
					invoiceLine.setNetAmount(product.getSalesPrice().multiply(new BigDecimal(1)));
					BigDecimal cgst = null, sgst = null, igst = null;
					sgst = product.getSalesPrice().multiply(new BigDecimal(1))
							.multiply((product.getGstRate()).divide(new BigDecimal(100))).divide(new BigDecimal(2));
					cgst = sgst;

					if (companyState.equals(partyAddress)) {
						invoiceLine.setCgst(cgst);
						invoiceLine.setSgst(sgst);
						invoiceLine.setGrossAmount(
								product.getSalesPrice().multiply(new BigDecimal(1)).add(cgst).add(sgst));
					} else {
						igst = sgst.add(cgst);
						invoiceLine.setIgst(igst);
						invoiceLine.setGrossAmount(
								product.getSalesPrice().multiply(new BigDecimal(1)).add(cgst).add(sgst));

					}
					invoiceItemList.add(invoiceLine);
				} catch (NumberFormatException nfe) {
					System.out.println(nfe);
				}
			}
			invoice.setInvoiceItemsList(invoiceItemList);
		}
		return invoice;
	}
}
