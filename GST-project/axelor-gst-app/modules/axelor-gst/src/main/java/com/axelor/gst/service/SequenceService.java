package com.axelor.gst.service;

import com.axelor.meta.db.MetaModel;

public interface SequenceService {
	public String calculateSequenceNumber(MetaModel metaModel);
}
