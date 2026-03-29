package com.wm.service;



import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.wm.TestHelper;
import com.wm.WarehouseManagementApplication;
import com.wm.entity.Invoice;
import com.wm.entity.InvoiceLine;
import com.wm.entity.Partner;
import com.wm.repository.InvoiceRepository;
import com.wm.repository.PartnerRepository;


@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		classes = {WarehouseManagementApplication.class}
)
@AutoConfigureMockMvc
public class InvoiceServiceTest {

	@Autowired
	private InvoiceService invoiceService;
	
	@Autowired
	private InvoiceRepository invoiceRepository;
	
	@Autowired
	private PartnerRepository partnerRepository;
	
	private Partner testPartner;
	
	private List<Long> testInvoiceIdList = new ArrayList<Long>();
	
	
	@BeforeAll
	void createTestRecords() {
		testPartner = partnerRepository.save(new Partner(null, "", "", "", ""));
	}
	
	@Test
	void testFindById_InvalidId() {
		assertThrows(RuntimeException.class, () -> invoiceService.findById(-1L));
	}
	
	
    @Test
    void testFindById_ValidId() {
		Invoice testInvoice = TestHelper.createTestInvoice(testPartner, invoiceRepository, testInvoiceIdList);
		TestHelper.assertEqualsInvoice(testInvoice, invoiceService.findById(testInvoice.getInvoiceId()));
    }
    
    @Test
    void testCreate_AlreadyExists() {
    	Invoice testInvoice = TestHelper.createTestInvoice(testPartner, invoiceRepository, testInvoiceIdList);
    	assertThrows(RuntimeException.class, () -> invoiceService.create(testInvoice));
    }
    
    @Test
    void testCreate_NotExists() {
    	Invoice testInvoice = new Invoice();
    	testInvoice.setInvoiceNumber(String.valueOf(System.currentTimeMillis()));
    	testInvoice.setPartner(testPartner);
    	testInvoice = invoiceService.create(testInvoice);
    	testInvoiceIdList.add(testInvoice.getInvoiceId());
    	
    	Invoice expectedInvoice = new Invoice(
    			testInvoice.getInvoiceId(),
    			testInvoice.getInvoiceNumber(),
    			LocalDate.now(),
    			"NEW",
    			BigDecimal.ZERO,
    			BigDecimal.ZERO,
    			BigDecimal.ZERO,
    			testPartner,
    			null
    	);
    	TestHelper.assertEqualsInvoice(expectedInvoice, testInvoice);
    }
    
    @Test
    void testSave() {
    	Invoice testInvoice = TestHelper.createTestInvoice(testPartner, invoiceRepository, testInvoiceIdList);
    	testInvoice.getLines().add(TestHelper.createTestInvoiceLine());
    	testInvoice.getLines().add(TestHelper.createTestInvoiceLine());
    	invoiceService.save(testInvoice);
    	testInvoice = invoiceService.findById(testInvoice.getInvoiceId());
    	
    	Invoice expectedInvoice = new Invoice(
    			testInvoice.getInvoiceId(),
    			testInvoice.getInvoiceNumber(),
    			LocalDate.now(),
    			"",
    			BigDecimal.TEN.add(BigDecimal.TEN),
    			BigDecimal.TEN.add(BigDecimal.TEN),
    			BigDecimal.TEN.add(BigDecimal.TEN),
    			testPartner,
    			new ArrayList<InvoiceLine>()
    	);
    	
    	TestHelper.assertEqualsInvoice(expectedInvoice, testInvoice);
    }
    
    
	@AfterAll
	void deleteTestRecords() {
		invoiceRepository.deleteAllById(testInvoiceIdList);
    	partnerRepository.delete(testPartner);
	}
	
}