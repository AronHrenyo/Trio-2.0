package com.wm.controller;

import com.wm.entity.Warehouse;
import com.wm.service.WarehouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService service;

    @GetMapping
    public List<Warehouse> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Warehouse getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public Warehouse create(@RequestBody @Valid Warehouse warehouse) {
        return service.create(warehouse);
    }

    @PutMapping("/{id}")
    public Warehouse update(@PathVariable Long id,
                            @RequestBody @Valid Warehouse warehouse) {
        return service.update(id, warehouse);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}