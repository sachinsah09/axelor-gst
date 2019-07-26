package com.axelor.gst.service;

import java.math.BigDecimal;
import com.axelor.gst.db.Product;

public interface ProductService {
	public BigDecimal setGstRate(Product product);
}

