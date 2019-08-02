package com.axelor.gst.service;

import java.math.BigDecimal;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.State;

public class InvoiceLineServiceImp implements InvoiceLineService {

	@Override
	public InvoiceLine calculatedFieldValue(InvoiceLine invoiceLine, Invoice invoice) {
		BigDecimal sgst = BigDecimal.ZERO, cgst = BigDecimal.ZERO, igst = BigDecimal.ZERO, netAmount = BigDecimal.ZERO,
				grossAmount = BigDecimal.ZERO;
		BigDecimal qty = new BigDecimal(invoiceLine.getQty());
		invoiceLine.setNetAmount(qty.multiply(invoiceLine.getPrice()));
		if (invoice.getInvoiceAddress() != null && invoice.getCompany() != null) {
			State invoiceState = invoice.getInvoiceAddress().getState();
			State companyState = invoice.getCompany().getAddress().getState();
			netAmount = qty.multiply(invoiceLine.getPrice());
			if (invoiceState.equals(companyState)) {
				sgst = (invoiceLine.getNetAmount().multiply((invoiceLine.getGstRate()).divide(new BigDecimal(100))))
						.divide(new BigDecimal(2));
				cgst = sgst;
				grossAmount = netAmount.add(cgst).add(sgst);
			} else {
				igst = (invoiceLine.getNetAmount().multiply((invoiceLine.getGstRate()).divide(new BigDecimal(100))));
				grossAmount = netAmount.add(igst);
			}
			invoiceLine.setCgst(cgst);
			invoiceLine.setSgst(sgst);
			invoiceLine.setIgst(igst);
			invoiceLine.setGrossAmount(grossAmount);
		} else {
			invoiceLine.setCgst(cgst);
			invoiceLine.setSgst(sgst);
			invoiceLine.setIgst(igst);
			invoiceLine.setNetAmount(netAmount);
			invoiceLine.setGrossAmount(grossAmount);
		}
		return invoiceLine;
	}
}