package com.axelor.gst.service;

import java.math.BigDecimal;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.State;

public class InvoiceLineServiceImp implements InvoiceLineService {

	@Override
	public InvoiceLine calculatedFieldValue(InvoiceLine invoiceLine, Invoice invoice) {
		BigDecimal sgst, cgst, igst, netAmount;
		BigDecimal qty = new BigDecimal(invoiceLine.getQty());
		netAmount = qty.multiply(invoiceLine.getPrice());
		invoiceLine.setNetAmount(qty.multiply(invoiceLine.getPrice()));
		State invoiceState = invoice.getInvoiceAddress().getState();
		State companyState = invoice.getCompany().getAddress().getState();
		if (invoiceState.equals(companyState)) {
			sgst = (invoiceLine.getNetAmount().multiply((invoiceLine.getGstRate()).divide(new BigDecimal(100)))).divide(new BigDecimal(2));
			cgst = sgst;
			invoiceLine.setCgst(cgst);
			invoiceLine.setSgst(sgst);
			invoiceLine.setGrossAmount(netAmount.add(cgst).add(sgst));
		} else {
			invoiceLine.setIgst((invoiceLine.getNetAmount().multiply((invoiceLine.getGstRate()).divide(new BigDecimal(100)))));
			igst = (invoiceLine.getNetAmount().multiply((invoiceLine.getGstRate()).divide(new BigDecimal(100))));
			invoiceLine.setGrossAmount(netAmount.add(igst));
		}
		return invoiceLine;
	}
}