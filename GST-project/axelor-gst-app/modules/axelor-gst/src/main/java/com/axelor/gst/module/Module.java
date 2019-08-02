package com.axelor.gst.module;

import com.axelor.app.AxelorModule;
import com.axelor.gst.service.InvoiceLineService;
import com.axelor.gst.service.InvoiceLineServiceImp;
import com.axelor.gst.service.InvoiceService;
import com.axelor.gst.service.InvoiceServiceImp;
import com.axelor.gst.service.SequenceService;
import com.axelor.gst.service.SequenceServiceImp;
import com.axelor.gst.web.ProductController;

public class Module extends AxelorModule {
	protected void configure() {
		bind(SequenceService.class).to(SequenceServiceImp.class);
		bind(InvoiceService.class).to(InvoiceServiceImp.class);
		bind(InvoiceLineService.class).to(InvoiceLineServiceImp.class);
		bind(ProductController.class);
	}
}