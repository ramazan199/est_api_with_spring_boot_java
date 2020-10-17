package com.example.demo.inventory.rest;

import com.example.demo.inventory.application.dto.PlantInvItemDTO;
import com.example.demo.inventory.application.service.PlantInventoryItemAssembler;
import com.example.demo.inventory.domain.model.PlantInventoryEntry;
import com.example.demo.inventory.domain.model.PlantInvItem;
import com.example.demo.inventory.domain.repository.InventoryRepo;
import com.example.demo.inventory.domain.repository.PlantInventoryEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pitems")
public class PlantInvItemRestController {

    @Autowired
    InventoryRepo inventoryRepo;

    @Autowired
    PlantInventoryEntryRepository plantInventoryEntryRepository;

    @Autowired
    PlantInventoryItemAssembler plantInventoryItemAssembler;

    @GetMapping("/items")
    public List<PlantInvItemDTO> findAvailableInventoryItems(
            @RequestParam(name = "pieId") Long pieId,
            @RequestParam(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        PlantInventoryEntry plant = plantInventoryEntryRepository.getOne(pieId);
        List<PlantInvItem> items = inventoryRepo.findAvailableItems(plant, startDate, endDate);
        return items.stream().map(pii -> plantInventoryItemAssembler.toResource(pii)).collect(Collectors.toList());
    }



}
