package com.axelor.gst.service;

import java.math.BigDecimal;
import com.axelor.gst.db.InvoiceLine;

public interface InvoiceLineService {
		public InvoiceLine calculatedFieldValue(InvoiceLine invoiceLine);
		public String setProductName(InvoiceLine invoiceLine);
		public BigDecimal setGstRate(InvoiceLine invoiceLine);
}
