package com.wm.service;

import com.wm.entity.Warehouse;
import com.wm.repository.WarehouseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WarehouseService {

    private final WarehouseRepository repository;

    public List<Warehouse> findAll() {
        return repository.findAll();
    }

    public Warehouse findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + id));
    }

    public Warehouse create(Warehouse warehouse) {
        return repository.save(warehouse);
    }

    public Warehouse update(Long id, Warehouse warehouse) {
        Warehouse existing = findById(id);

        existing.setWarehouseName(warehouse.getWarehouseName());
        existing.setWarehouseCapacity(warehouse.getWarehouseCapacity());

        return repository.save(existing);
    }

    public void delete(Long id) {
        findById(id);
        repository.deleteById(id);
    }
}