package com.wm.controller;

import com.wm.entity.Invoice;
import com.wm.entity.Partner;
import com.wm.repository.InvoiceRepository;
import com.wm.repository.PartnerRepository;
import com.wm.servicePDF.InvoicePdfService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class InvoiceController {

    private final InvoiceRepository invoiceRepository;
    private final PartnerRepository partnerRepository;
    private final InvoicePdfService invoicePdfService;

    public InvoiceController(InvoiceRepository invoiceRepository,
                             PartnerRepository partnerRepository,
                             InvoicePdfService invoicePdfService) {
        this.invoiceRepository = invoiceRepository;
        this.partnerRepository = partnerRepository;
        this.invoicePdfService = invoicePdfService;
    }

    // List invoices with optional date filter
    @GetMapping("/invoice-view")
    public String listInvoices(
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            Model model) {

        List<Invoice> invoices;

        if (from != null && to != null) {
            invoices = invoiceRepository.findByInvoiceDateBetween(from, to);
        } else {
            invoices = invoiceRepository.findAll();
        }

        model.addAttribute("orders", invoices);
        model.addAttribute("from", from);
        model.addAttribute("to", to);

        return "invoice/invoice-view";
    }

    // Show create form
    @GetMapping("/invoice-create")
    public String showCreateForm(Model model) {
        model.addAttribute("invoice", new Invoice());

        List<Partner> partners = partnerRepository.findAll();
        model.addAttribute("partners", partners);

        return "invoice/invoice-create";
    }

    // Handle form submission
    @PostMapping("/invoice-create")
    public String createInvoice(Invoice invoice) {

        if (invoice.getPartner() == null) {
            throw new RuntimeException("Partner must be selected for an invoice");
        }

        invoiceRepository.save(invoice);

        return "redirect:/invoice-view";
    }

    // ---------------- PDF EXPORT -----------------
    @GetMapping("/invoice/{id}/pdf")
    public ResponseEntity<byte[]> downloadInvoicePdf(@PathVariable Long id) throws Exception {

        Invoice invoice = invoiceRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        byte[] pdf = invoicePdfService.generateInvoicePdf(invoice);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=invoice-" + invoice.getInvoiceNumber() + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}