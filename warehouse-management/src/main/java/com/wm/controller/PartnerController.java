package com.wm.controller;

import com.wm.entity.Partner;
import com.wm.repository.PartnerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PartnerController {

    private final PartnerRepository partnerRepository;

    public PartnerController(PartnerRepository partnerRepository) {
        this.partnerRepository = partnerRepository;
    }

    // List partners with optional name filter
    @GetMapping("/partner-view") // List partners: http://localhost:8080/partner-view
    public String listPartners(
            @RequestParam(value = "name", required = false) String name,
            Model model) {

        List<Partner> partners;

        if (name != null && !name.isEmpty()) {
            partners = partnerRepository.findByPartnerNameContainingIgnoreCase(name);
        } else {
            partners = partnerRepository.findAll();
        }

        model.addAttribute("partners", partners);
        model.addAttribute("name", name);

        return "partner/partner-view"; // partner-view.html
    }

    // Show the create form
    @GetMapping("/partner-create") // Create new partner form: http://localhost:8080/partner-create
    public String showCreateForm(Model model) {
        model.addAttribute("partner", new Partner());
        return "partner/partner-create"; // partner-create.html
    }

    // Handle form submission
    @PostMapping("/partner-create")
    public String createPartner(Partner partner) {
        partnerRepository.save(partner);
        return "redirect:/partner-view"; // redirect to partner list
    }
}