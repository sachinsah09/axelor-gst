package com.axelor.gst.service;

import com.axelor.db.JPA;
import com.axelor.gst.db.Invoice;
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
				 sequenceNumber=invoice.getInvoiceSeq();
		}
		return sequenceNumber;
	}


}
