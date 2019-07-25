package com.axelor.gst.service;

import java.math.BigDecimal;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;

public interface InvoiceLineService {
		public InvoiceLine calculatedFieldValue(InvoiceLine invoiceLine,Invoice invoice);
		public String setProductName(InvoiceLine invoiceLine);
		public BigDecimal setGstRate(InvoiceLine invoiceLine);
		public BigDecimal setPrice(InvoiceLine invoiceLine);
		public String setHsbn(InvoiceLine invoiceLine);
}
