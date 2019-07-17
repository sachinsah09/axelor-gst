package com.axelor.gst.module;

import org.eclipse.core.internal.runtime.Product;

import com.axelor.app.AxelorModule;
import com.axelor.gst.service.ProductService;
import com.axelor.gst.service.ProductServiceImp;

public class Module extends AxelorModule {

	  protected void configure() {
	    bind(ProductService.class).to(ProductServiceImp.class);
	  }
	}