package com.wm.controller;

import com.wm.entity.PurchaseOrderLine;
import com.wm.service.PurchaseOrderLineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-lines")
@RequiredArgsConstructor
public class PurchaseOrderLineController {

    private final PurchaseOrderLineService service;

    @GetMapping
    public List<PurchaseOrderLine> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public PurchaseOrderLine getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public PurchaseOrderLine create(@RequestBody @Valid PurchaseOrderLine line) {
        return service.create(line);
    }

    @PutMapping("/{id}")
    public PurchaseOrderLine update(@PathVariable Long id,
                                    @RequestBody @Valid PurchaseOrderLine line) {
        return service.update(id, line);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}