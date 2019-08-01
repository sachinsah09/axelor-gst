package com.axelor.gst.service;

import java.math.BigDecimal;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.State;

public class InvoiceLineServiceImp implements InvoiceLineService {

	@Override
	public InvoiceLine calculatedFieldValue(InvoiceLine invoiceLine, Invoice invoice) {
		BigDecimal sgst = null, cgst = null, igst = null, netAmount;
		BigDecimal qty = new BigDecimal(invoiceLine.getQty());
		netAmount = qty.multiply(invoiceLine.getPrice());
		invoiceLine.setNetAmount(qty.multiply(invoiceLine.getPrice()));
		if (invoice.getInvoiceAddress() != null && invoice.getCompany() != null) {
			State invoiceState = invoice.getInvoiceAddress().getState();
			State companyState = invoice.getCompany().getAddress().getState();
			if (invoiceState.equals(companyState)) {
				sgst = (invoiceLine.getNetAmount().multiply((invoiceLine.getGstRate()).divide(new BigDecimal(100))))
						.divide(new BigDecimal(2));
				cgst = sgst;
				invoiceLine.setCgst(cgst);
				invoiceLine.setSgst(sgst);
				invoiceLine.setIgst(igst);
				invoiceLine.setGrossAmount(netAmount.add(cgst).add(sgst));
			} else {
				invoiceLine.setCgst(cgst);
				invoiceLine.setSgst(sgst);
				invoiceLine.setIgst(
						(invoiceLine.getNetAmount().multiply((invoiceLine.getGstRate()).divide(new BigDecimal(100)))));
				igst = (invoiceLine.getNetAmount().multiply((invoiceLine.getGstRate()).divide(new BigDecimal(100))));
				invoiceLine.setGrossAmount(netAmount.add(igst));
			}
		} else {
				invoiceLine.setCgst(cgst);
				invoiceLine.setSgst(sgst);
				invoiceLine.setIgst(igst);
			} 
		return invoiceLine;
	}
}