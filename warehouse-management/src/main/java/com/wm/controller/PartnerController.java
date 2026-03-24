package com.wm.controller;

import com.wm.entity.Partner;
import com.wm.service.PartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/partners")
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerService service;

    @GetMapping
    public List<Partner> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Partner getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public Partner create(@RequestBody @Valid Partner partner) {
        return service.create(partner);
    }

    @PutMapping("/{id}")
    public Partner update(@PathVariable Long id,
                          @RequestBody @Valid Partner partner) {
        return service.update(id, partner);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}