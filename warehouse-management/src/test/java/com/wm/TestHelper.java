package com.wm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.wm.entity.Invoice;
import com.wm.entity.InvoiceLine;
import com.wm.entity.Partner;
import com.wm.repository.InvoiceRepository;

public class TestHelper {
	public static void assertEqualsBigDecimal(BigDecimal expected, BigDecimal actual, String content) {
		assertNotNull(expected);
		assertNotNull(actual);
		
		assertEquals(0, expected.compareTo(actual), String.format("várt %s (%s) != tényleges %s (%s)",   content, expected.toPlainString(), content, actual.toPlainString()));
	}
	
	public static void assertEqualsInvoice(Invoice expected, Invoice actual) {
		assertEquals(expected.getInvoiceId(), actual.getInvoiceId());
		assertEquals(expected.getInvoiceNumber(), actual.getInvoiceNumber());
		assertEquals(expected.getInvoiceDate(), actual.getInvoiceDate());
		assertEquals(expected.getInvoiceStatus(), actual.getInvoiceStatus());
		assertEqualsBigDecimal(expected.getInvoiceNetSum(), actual.getInvoiceNetSum(), "net sum");
		assertEqualsBigDecimal(expected.getInvoiceVatSum(), actual.getInvoiceVatSum(), "vat sum");
		assertEqualsBigDecimal(expected.getInvoiceGrossSum(), actual.getInvoiceGrossSum(), "gross sum");
	    assertEquals(expected.getPartner().getPartnerId(), actual.getPartner().getPartnerId());
	}
	
	public static Invoice createTestInvoice(Partner testPartner, InvoiceRepository invoiceRepository, List<Long> testInvoiceIdList) {
		Invoice testInvoice =  new Invoice(
    			null,
    			String.valueOf(System.currentTimeMillis()),
    			LocalDate.now(),
    			"",
    			BigDecimal.ZERO,
    			BigDecimal.ZERO,
    			BigDecimal.ZERO,
    			testPartner,
    			new ArrayList<InvoiceLine>()
    	);
		testInvoice = invoiceRepository.save(testInvoice);
		testInvoiceIdList.add(testInvoice.getInvoiceId());
		return testInvoice;
	}
	
	
	public static InvoiceLine createTestInvoiceLine() {
		return new InvoiceLine(
				null, 
				null, 
				null, 
				BigDecimal.ZERO, 
				0, 
				0, 
				BigDecimal.TEN, 
				BigDecimal.TEN, 
				BigDecimal.TEN
		);
	}
	
}
