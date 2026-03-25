package com.wm.controller;

import com.wm.entity.SalesOrderLine;
import com.wm.service.SalesOrderLineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales-order-lines")
@RequiredArgsConstructor
public class SalesOrderLineController {

    private final SalesOrderLineService service;

    // Get all sales order lines
    @GetMapping
    public List<SalesOrderLine> getAll() {
        return service.findAll();
    }

    // Get a single sales order line by ID
    @GetMapping("/{id}")
    public SalesOrderLine getById(@PathVariable Long id) {
        return service.findById(id);
    }

    // Create a new sales order line
    @PostMapping
    public SalesOrderLine create(@RequestBody @Valid SalesOrderLine line) {

        if (line.getSalesOrder() == null || line.getSalesOrder().getSalesOrderId() == null) {
            throw new RuntimeException("SalesOrder must be specified for the line");
        }

        return service.create(line);
    }

    // Update an existing sales order line
    @PutMapping("/{id}")
    public SalesOrderLine update(@PathVariable Long id,
                                 @RequestBody @Valid SalesOrderLine line) {

        if (line.getSalesOrder() == null || line.getSalesOrder().getSalesOrderId() == null) {
            throw new RuntimeException("SalesOrder must be specified for the line");
        }

        return service.update(id, line);
    }

    // Delete a sales order line
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}