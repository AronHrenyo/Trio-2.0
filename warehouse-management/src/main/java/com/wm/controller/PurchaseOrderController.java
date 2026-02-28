package com.wm.controller;

import com.wm.model.PurchaseOrder;
import com.wm.model.PurchaseOrderLine;
import com.wm.repository.PurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;

@Controller
@RequestMapping("/purchase-order")
@RequiredArgsConstructor
public class PurchaseOrderController {

    private final PurchaseOrderRepository purchaseOrderRepository;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("purchaseOrder", purchaseOrderRepository.findAll());
        return "purchase-order/list";
    }

    // Show create form
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        PurchaseOrder po = new PurchaseOrder();
        po.setPurchaseOrderDate(LocalDate.now());
        po.setPurchaseOrderLines(new ArrayList<>());
        model.addAttribute("purchaseOrder", po);
        return "purchase-order/form";
    }

    // Save purchase order (create and update)
    @PostMapping
    public String save(@ModelAttribute PurchaseOrder purchaseOrder) {

        // Set back-reference for lines
        if (purchaseOrder.getPurchaseOrderLines() != null) {
            for (PurchaseOrderLine line : purchaseOrder.getPurchaseOrderLines()) {
                line.setPurchaseOrder(purchaseOrder);
            }
        }

        purchaseOrderRepository.save(purchaseOrder);
        return "redirect:/purchase-order";
    }

    // View details
    @GetMapping("/{id}")
    public String view(@PathVariable Integer id, Model model) {
        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
        model.addAttribute("purchaseOrder", po);
        return "purchase-order/view";
    }

    // Show edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        model.addAttribute("purchaseOrder", po);
        return "purchase-order/form";
    }

    // Delete
    @GetMapping("/{orderId}/delete-line/{lineId}")
    public String deleteLine(@PathVariable Integer orderId,
                             @PathVariable Integer lineId) {

        PurchaseOrder po = purchaseOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Not found"));

        po.getPurchaseOrderLines()
                .removeIf(line -> line.getPurchaseOrderLineId().equals(lineId));

        purchaseOrderRepository.save(po);

        return "redirect:/purchase-order/edit/" + orderId;
    }
}