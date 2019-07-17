package com.axelor.gst.service;

import java.math.BigDecimal;
import com.axelor.common.ObjectUtils;
import com.axelor.gst.db.Product;
import com.axelor.gst.db.ProductCategory;

public class ProductServiceImp implements ProductService {

	@Override
	public BigDecimal setGstRate(Product product) {
		BigDecimal gstRate = null;
		if (!ObjectUtils.isEmpty(product.getId())) {
			ProductCategory pc = product.getCategory();
			gstRate = pc.getGstRate();
		} 
		return gstRate;
	}
}
