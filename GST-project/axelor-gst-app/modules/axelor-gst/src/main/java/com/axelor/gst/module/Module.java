package com.axelor.gst.module;

import com.axelor.app.AxelorModule;
import com.axelor.gst.service.PartyService;
import com.axelor.gst.service.PartyServiceImp;
import com.axelor.gst.service.ProductService;
import com.axelor.gst.service.ProductServiceImp;
import com.axelor.gst.service.SequenceService;
import com.axelor.gst.service.SequenceServiceImp;

public class Module extends AxelorModule {
	protected void configure() {
		bind(ProductService.class).to(ProductServiceImp.class);
		bind(SequenceService.class).to(SequenceServiceImp.class);
		bind(PartyService.class).to(PartyServiceImp.class);
	}
}