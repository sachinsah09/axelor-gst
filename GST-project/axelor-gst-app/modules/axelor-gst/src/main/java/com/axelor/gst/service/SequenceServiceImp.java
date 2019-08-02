package com.axelor.gst.service;

import com.axelor.common.ObjectUtils;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.inject.Beans;
import com.axelor.meta.db.MetaModel;
import com.google.inject.persist.Transactional;

public class SequenceServiceImp implements SequenceService {

	@Override
	@Transactional
	public String calculateSequenceNumber(MetaModel metaModel) {
		Sequence sequence = Beans.get(SequenceRepository.class).all().filter("self.model = ?", metaModel).fetchOne();
		String sequenceNumber = null;
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
			String middle = String.format("%0" + padding + "d", nextNumber);
			sequenceNumber = prefix + middle + suffix;
			nextNumber++;
			String setNextNumber = "" + nextNumber;
			sequence.setNextNumber(setNextNumber);
			Beans.get(SequenceRepository.class).save(sequence);
		}
		return sequenceNumber;
	}
}
