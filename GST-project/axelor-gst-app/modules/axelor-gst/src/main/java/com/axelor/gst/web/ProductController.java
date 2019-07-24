package com.axelor.gst.web;

import java.io.File;
import java.math.BigDecimal;

import com.axelor.app.AppSettings;
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
		try {
			BigDecimal gstRate = service.setGstRate(product);
			response.setValue("gstRate", gstRate);
		} catch (Exception e) {
			response.setError("Error while fetching ID");
		}
	}

	public void setPathProduct(ActionRequest request, ActionResponse response) {
		String attachmentPathProduct = AppSettings.get().getPath("file.upload.dir", "");
		attachmentPathProduct = attachmentPathProduct.endsWith(File.separator) ? attachmentPathProduct : attachmentPathProduct + File.separator;
		request.getContext().put("AttachmentPathProduct", attachmentPathProduct);
		System.out.println(attachmentPathProduct);
	}
}
