package com.axelor.gst.service;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;

public interface InvoiceLineService {
		public InvoiceLine calculatedFieldValue(InvoiceLine invoiceLine,Invoice invoice);
}
