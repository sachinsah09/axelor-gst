package com.axelor.gst.service;

import java.util.List;

import com.axelor.common.ObjectUtils;
import com.axelor.db.JPA;
import com.axelor.gst.db.Address;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
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

			for (int i = 0; i < padding; i++) {
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
		Contact setInvoicePartyPrimaryContact = null ;

		if (ObjectUtils.isEmpty(invoice.getId())) {
			return null;
		}
		if (!ObjectUtils.isEmpty(invoice.getId())) {
			System.out.println(invoice.getId());
			Party party = invoice.getParty();
			long partyId = party.getId();
			System.out.println(partyId);
			List<Contact> partyContactId = JPA.all(Contact.class).filter("self.party = " + partyId).fetch();
			
			for (Contact contact : partyContactId) {
					if(contact.getType().equals("primary"))
					{
						setInvoicePartyPrimaryContact=JPA.em().find(Contact.class,contact.getId());;		
					}
			}
		}
		return setInvoicePartyPrimaryContact;
	}

	@Override
	public Address setInvoicePartyAddress(Invoice invoice) {
		Address setInvoicePartyAddress = null ;

		if (ObjectUtils.isEmpty(invoice.getId())) {
			return null;
		}
		if (!ObjectUtils.isEmpty(invoice.getId())) {
			System.out.println(invoice.getId());
			Party party = invoice.getParty();
			long partyId = party.getId();
			System.out.println(partyId);
			List<Address> partyAddressId = JPA.all(Address.class).filter("self.party = " + partyId).fetch();
			
			for (Address address : partyAddressId) {
					if(address.getType().equals("default"))
					{
						setInvoicePartyAddress=JPA.em().find(Address.class,address.getId());		
					}
					else if(address.getType().equals("invoice")){
						setInvoicePartyAddress=JPA.em().find(Address.class,address.getId());
					}
					else
					{
						
					}
			}
		}
		return setInvoicePartyAddress;
	}

}
