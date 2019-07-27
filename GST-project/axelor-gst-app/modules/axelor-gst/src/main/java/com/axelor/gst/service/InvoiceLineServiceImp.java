package com.axelor.gst.service;

import java.math.BigDecimal;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Product;
import com.axelor.gst.db.repo.ProductRepository;
import com.axelor.inject.Beans;
import com.google.inject.persist.Transactional;

public class InvoiceLineServiceImp implements InvoiceLineService {

	@Transactional
	@Override
	public InvoiceLine calculatedFieldValue(InvoiceLine invoiceLine, Invoice invoice) {

		BigDecimal sgst, cgst, igst, netAmount;
		BigDecimal qty = new BigDecimal(invoiceLine.getQty());
		netAmount = qty.multiply(invoiceLine.getPrice());
		invoiceLine.setNetAmount(qty.multiply(invoiceLine.getPrice()));
		String invoiceState = invoice.getInvoiceAddress().getState().getName();
		String companyState = invoice.getCompany().getAddress().getState().getName();

		if (invoiceState.equals(companyState)) {
			sgst = (invoiceLine.getNetAmount().multiply((invoiceLine.getGstRate()).divide(new BigDecimal(100)))).divide(new BigDecimal(2));
			cgst = sgst;
			invoiceLine.setCgst(cgst);
			invoiceLine.setSgst(sgst);
			invoiceLine.setGrossAmount(netAmount.add(cgst).add(sgst));
		} else {
			invoiceLine.setIgst((invoiceLine.getNetAmount().multiply(invoiceLine.getGstRate())));
			igst = (invoiceLine.getNetAmount().multiply(invoiceLine.getGstRate()));
			invoiceLine.setGrossAmount(netAmount.add(igst));
		}
		return invoiceLine;
	}
}