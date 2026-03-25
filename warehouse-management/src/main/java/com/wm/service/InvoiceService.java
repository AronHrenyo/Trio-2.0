package com.wm.service;

import com.wm.entity.Invoice;
import com.wm.repository.InvoiceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InvoiceService {

    private final InvoiceRepository repository;

    // List all invoices
    public List<Invoice> findAll() {
        return repository.findAll();
    }

    // Find invoice by ID
    public Invoice findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));
    }

    // Create a new invoice
    public Invoice create(Invoice invoice) {

        // Check duplicate invoice number
        repository.findByInvoiceNumber(invoice.getInvoiceNumber())
                .ifPresent(i -> {
                    throw new RuntimeException("Invoice number already exists: " + invoice.getInvoiceNumber());
                });

        // Default values
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setInvoiceStatus("NEW");

        invoice.setInvoiceNetSum(BigDecimal.ZERO);
        invoice.setInvoiceVatSum(BigDecimal.ZERO);
        invoice.setInvoiceGrossSum(BigDecimal.ZERO);

        return repository.save(invoice);
    }

    // Update invoice
    public Invoice update(Long id, Invoice invoice) {
        Invoice existing = findById(id);

        existing.setInvoiceNumber(invoice.getInvoiceNumber());
        existing.setInvoiceStatus(invoice.getInvoiceStatus());

        return repository.save(existing);
    }

    // Delete invoice
    public void delete(Long id) {
        findById(id);
        repository.deleteById(id);
    }
}