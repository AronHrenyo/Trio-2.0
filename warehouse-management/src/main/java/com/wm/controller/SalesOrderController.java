package com.wm.controller;

import com.wm.entity.Partner;
import com.wm.entity.SalesOrder;
import com.wm.repository.PartnerRepository;
import com.wm.repository.SalesOrderRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class SalesOrderController {

    private final SalesOrderRepository salesOrderRepository;
    private final PartnerRepository partnerRepository;

    public SalesOrderController(SalesOrderRepository salesOrderRepository,
                                PartnerRepository partnerRepository) {
        this.salesOrderRepository = salesOrderRepository;
        this.partnerRepository = partnerRepository;
    }

    // List sales orders with optional date filter
    @GetMapping("/sales-order-view")
    public String listSalesOrders(
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            Model model) {

        List<SalesOrder> orders;

        if (from != null && to != null) {
            orders = salesOrderRepository.findBySalesOrderDateBetween(from, to);
        } else {
            orders = salesOrderRepository.findAll();
        }

        model.addAttribute("orders", orders);
        model.addAttribute("from", from);
        model.addAttribute("to", to);

        return "sales-order/sales-order-view";
    }

    // Show create form
    @GetMapping("/sales-order-create")
    public String showCreateForm(Model model) {
        model.addAttribute("salesOrder", new SalesOrder());

        List<Partner> partners = partnerRepository.findAll();
        model.addAttribute("partners", partners);

        return "sales-order/sales-order-create";
    }

    // Handle form submission
    @PostMapping("/sales-order-create")
    public String createSalesOrder(SalesOrder salesOrder) {

        if (salesOrder.getPartner() == null) {
            throw new RuntimeException("Partner must be selected for a sales order");
        }

        salesOrderRepository.save(salesOrder);

        return "redirect:/sales-order-view";
    }
}