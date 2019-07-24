package com.axelor.gst.service;

import java.math.BigDecimal;
import com.axelor.db.JPA;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Product;
import com.axelor.gst.db.State;
import com.google.inject.persist.Transactional;

public class InvoiceLineServiceImp implements InvoiceLineService {

	@Transactional
	@Override
	public InvoiceLine calculatedFieldValue(InvoiceLine invoiceLine) {

		BigDecimal sgst, cgst, igst, netAmount;
		BigDecimal divider = new BigDecimal(2);
		BigDecimal qty = new BigDecimal(invoiceLine.getQty());
		netAmount = qty.multiply(invoiceLine.getPrice());
		invoiceLine.setNetAmount(qty.multiply(invoiceLine.getPrice()));

		State state1 = invoiceLine.getInvoice().getInvoiceAddress().getState();
		String invoiceState = state1.getName();
		State state2 = invoiceLine.getInvoice().getCompany().getAddress().getState();
		String companyState = state2.getName();

		if (invoiceState.equals(companyState)) {
			invoiceLine.setCgst((invoiceLine.getNetAmount().multiply(invoiceLine.getGstRate())).divide(divider));
			invoiceLine.setSgst((invoiceLine.getNetAmount().multiply(invoiceLine.getGstRate())).divide(divider));
			sgst = (invoiceLine.getNetAmount().multiply(invoiceLine.getGstRate())).divide(divider);
			cgst = (invoiceLine.getNetAmount().multiply(invoiceLine.getGstRate())).divide(divider);
			invoiceLine.setGrossAmount(netAmount.add(cgst).add(sgst));
		} else {
			invoiceLine.setIgst((invoiceLine.getNetAmount().multiply(invoiceLine.getGstRate())));
			igst = (invoiceLine.getNetAmount().multiply(invoiceLine.getGstRate()));
			invoiceLine.setGrossAmount(netAmount.add(igst));
		}
		return invoiceLine;
	}

	@Override
	public String setProductName(InvoiceLine invoiceLine) {
		long productId = invoiceLine.getProduct().getId();
		Product product = JPA.em().find(Product.class, productId);
		String productName = "[" + product.getCode() + "] " + product.getName();
		return productName;
	}

	@Override
	public BigDecimal setGstRate(InvoiceLine invoiceLine) {
		long productId = invoiceLine.getProduct().getId();
		Product product = JPA.em().find(Product.class, productId);
		BigDecimal gstRate = product.getGstRate();
		return gstRate;
	}

}