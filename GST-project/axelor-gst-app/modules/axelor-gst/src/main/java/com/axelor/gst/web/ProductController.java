package com.axelor.gst.web;

import java.math.BigDecimal;

import com.axelor.db.JpaSupport;
import com.axelor.gst.db.Product;
import com.axelor.gst.service.ProductService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class ProductController extends JpaSupport {

	@Inject
	private ProductService service;

	public void setGstRate(ActionRequest request, ActionResponse response) {
		Product product = request.getContext().asType(Product.class);
		BigDecimal gstrate= service.setGstRate(product);
		response.setValue("gstRate", gstrate);
	}

}
