package com.example.demo.inventory.rest;

import com.example.demo.inventory.application.service.InventoryService;
import com.example.demo.inventory.domain.model.PlantInvItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/inventory")
public class InventoryRestController {
    @Autowired
    InventoryService inventoryService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PlantInvItem fetchPurchaseOrder(@PathVariable("id") Long id) {
        return inventoryService.findPlantInventoryItem(id);
    }

}
