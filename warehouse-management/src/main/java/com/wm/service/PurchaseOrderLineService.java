package com.wm.service;

import com.wm.entity.PurchaseOrder;
import com.wm.entity.PurchaseOrderLine;
import com.wm.repository.PurchaseOrderLineRepository;
import com.wm.repository.PurchaseOrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PurchaseOrderLineService {

    private final PurchaseOrderLineRepository repository;
    private final PurchaseOrderRepository orderRepository;

    public List<PurchaseOrderLine> findAll() {
        return repository.findAll();
    }

    public PurchaseOrderLine findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order line not found with id: " + id));
    }

    public PurchaseOrderLine create(PurchaseOrderLine line) {
        // kiszámoljuk a line összegét
        calculateLineSums(line);

        // mentés
        PurchaseOrderLine saved = repository.save(line);

        // frissítjük a parent order összegeit
        updateOrderTotals(line.getPurchaseOrder());

        return saved;
    }

    public PurchaseOrderLine update(Long id, PurchaseOrderLine line) {
        PurchaseOrderLine existing = findById(id);

        existing.setProduct(line.getProduct());
        existing.setPartner(line.getPartner());
        existing.setPurchaseOrderLinePrice(line.getPurchaseOrderLinePrice());
        existing.setPurchaseOrderLineVatKey(line.getPurchaseOrderLineVatKey());
        existing.setPurchaseOrderLineQuantity(line.getPurchaseOrderLineQuantity());

        calculateLineSums(existing);

        PurchaseOrderLine saved = repository.save(existing);

        updateOrderTotals(existing.getPurchaseOrder());

        return saved;
    }

    public void delete(Long id) {
        PurchaseOrderLine line = findById(id);
        PurchaseOrder order = line.getPurchaseOrder();

        repository.delete(line);

        updateOrderTotals(order);
    }

    // 🔹 segédfüggvények

    private void calculateLineSums(PurchaseOrderLine line) {
        BigDecimal qty = BigDecimal.valueOf(line.getPurchaseOrderLineQuantity());
        BigDecimal price = line.getPurchaseOrderLinePrice();
        BigDecimal net = price.multiply(qty);
        BigDecimal vat = net.multiply(BigDecimal.valueOf(line.getPurchaseOrderLineVatKey()))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal gross = net.add(vat);

        line.setPurchaseOrderLineNetSum(net);
        line.setPurchaseOrderLineVatSum(vat);
        line.setPurchaseOrderLineGrossSum(gross);
    }

    private void updateOrderTotals(PurchaseOrder order) {
        List<PurchaseOrderLine> lines = repository.findByPurchaseOrder_PurchaseOrderId(order.getPurchaseOrderId());

        BigDecimal netTotal = lines.stream()
                .map(PurchaseOrderLine::getPurchaseOrderLineNetSum)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal vatTotal = lines.stream()
                .map(PurchaseOrderLine::getPurchaseOrderLineVatSum)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal grossTotal = lines.stream()
                .map(PurchaseOrderLine::getPurchaseOrderLineGrossSum)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setPurchaseOrderNetSum(netTotal);
        order.setPurchaseOrderVatSum(vatTotal);
        order.setPurchaseOrderGrossSum(grossTotal);

        orderRepository.save(order);
    }
}