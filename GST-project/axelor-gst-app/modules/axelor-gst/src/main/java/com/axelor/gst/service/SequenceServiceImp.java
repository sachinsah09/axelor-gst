package com.axelor.gst.service;

import com.axelor.common.ObjectUtils;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.inject.Beans;

public class SequenceServiceImp implements SequenceService {

	@Override
	public String calculateSequenceNumber(Sequence sequence) {
		String sequenceNumber = "";
		int addPaddingZero = 0;
		if (ObjectUtils.isEmpty(sequence.getId())) {
			sequenceNumber = "not";
			return sequenceNumber;
		}
		if (!ObjectUtils.isEmpty(sequence.getId())) {
			String prefix = sequence.getPrefix();
			String suffix = sequence.getSuffix();
			int padding = sequence.getPadding();
			int nextNumber = Integer.parseInt(sequence.getNextNumber());

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
			sequence.setNextNumber(setNextNumber);
			Beans.get(SequenceRepository.class).save(sequence);
		}
		return sequenceNumber;
	}

}
