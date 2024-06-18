package com.myProject.ecommerce.controller;

import com.myProject.ecommerce.dto.Purchase;
import com.myProject.ecommerce.dto.PurchaseResponse;
import com.myProject.ecommerce.service.CheckoutService;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/checkout")
public class checkoutController {

    private CheckoutService checkoutService;


    public checkoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping("/purchase")
    public PurchaseResponse placeOrder(@RequestBody Purchase purchase) {

        PurchaseResponse purchaseResponse = checkoutService.placeOrder(purchase);
        return purchaseResponse;
    }
}
