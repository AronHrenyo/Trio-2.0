package com.wm.controller;

import com.wm.entity.Warehouse;
import com.wm.service.WarehouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/api/warehouses")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService service;

    // ✅ HTML oldal
    @GetMapping("/warehouse-map") // http://localhost:8080/api/warehouses/warehouse-map
    public String warehousesMap() {
        return "warehouse/warehouse-map";
    }

    // ✅ JSON API
    @GetMapping
    @ResponseBody
    public List<Map<String, Object>> getAll() {
        return service.findAll().stream()
                .map(this::toMap)
                .toList();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Map<String, Object> getById(@PathVariable Long id) {
        return toMap(service.findById(id));
    }

    @PostMapping
    @ResponseBody
    public Map<String, Object> create(@RequestBody @Valid Warehouse warehouse) {
        return toMap(service.create(warehouse));
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Map<String, Object> update(@PathVariable Long id,
                                      @RequestBody @Valid Warehouse warehouse) {
        return toMap(service.update(id, warehouse));
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    // ENTITY → MAP
    private Map<String, Object> toMap(Warehouse w) {
        Map<String, Object> map = new HashMap<>();

        map.put("warehouseId", w.getWarehouseId());
        map.put("warehouseName", w.getWarehouseName());
        map.put("warehouseCapacity", w.getWarehouseCapacity());

        if (w.getWarehouseLocation() != null) {
            map.put("lat", w.getWarehouseLocation().getY());
            map.put("lon", w.getWarehouseLocation().getX());
        } else {
            map.put("lat", null);
            map.put("lon", null);
        }

        return map;
    }
}