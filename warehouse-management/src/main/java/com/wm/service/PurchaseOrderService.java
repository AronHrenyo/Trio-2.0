package com.wm.service;

import com.wm.entity.PurchaseOrder;
import com.wm.repository.PurchaseOrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PurchaseOrderService {

    private final PurchaseOrderRepository repository;

    public List<PurchaseOrder> findAll() {
        return repository.findAll();
    }

    public PurchaseOrder findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }

    public PurchaseOrder create(PurchaseOrder order) {

        repository.findByPurchaseOrderNumber(order.getPurchaseOrderNumber())
                .ifPresent(o -> {
                    throw new RuntimeException("Order number already exists: " + order.getPurchaseOrderNumber());
                });

        order.setPurchaseOrderDate(LocalDate.now());
        order.setPurchaseOrderStatus("NEW");

        // kezdeti összegek
        order.setPurchaseOrderNetSum(BigDecimal.ZERO);
        order.setPurchaseOrderVatSum(BigDecimal.ZERO);
        order.setPurchaseOrderGrossSum(BigDecimal.ZERO);

        return repository.save(order);
    }

    public PurchaseOrder update(Long id, PurchaseOrder order) {
        PurchaseOrder existing = findById(id);

        existing.setPurchaseOrderNumber(order.getPurchaseOrderNumber());
        existing.setPurchaseOrderStatus(order.getPurchaseOrderStatus());

        return repository.save(existing);
    }

    public void delete(Long id) {
        findById(id);
        repository.deleteById(id);
    }
}