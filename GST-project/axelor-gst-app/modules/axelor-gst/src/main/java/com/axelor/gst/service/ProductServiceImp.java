package com.axelor.gst.service;

import java.math.BigDecimal;
import com.axelor.common.ObjectUtils;
import com.axelor.gst.db.Product;
import com.axelor.gst.db.ProductCategory;

public class ProductServiceImp implements ProductService {

	@Override
	public BigDecimal setGstRate(Product product) {
		BigDecimal gstrate = null;
		if (!ObjectUtils.isEmpty(product.getId())) {
			ProductCategory pc = product.getCategory();
			gstrate = pc.getGstRate();
			System.out.print(gstrate);
		} else {
			System.out.println("ID is NULL");
		}
		return gstrate;
	}
}
