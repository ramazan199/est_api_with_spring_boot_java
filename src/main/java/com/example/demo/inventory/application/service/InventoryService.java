package com.example.demo.inventory.application.service;

import com.example.demo.inventory.application.dto.PlantInventoryEntryDTO;
import com.example.demo.inventory.domain.model.PlantInvItem;
import com.example.demo.inventory.domain.model.PlantInventoryEntry;
import com.example.demo.inventory.domain.repository.InventoryRepo;
import com.example.demo.inventory.domain.repository.PlantInvItemRepo;
import com.example.demo.inventory.domain.repository.PlantInventoryEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class InventoryService {

    @Autowired
    InventoryRepo inventoryRepo;

    @Autowired
    PlantInvItemRepo plantInvItemRepo;

    @Autowired
    PlantInventoryEntryAssembler plantInventoryEntryAssembler;

    @Autowired
    PlantInventoryEntryRepository plantInventoryEntryRepository;

    public List<PlantInventoryEntryDTO> findAvailablePlants(String name, LocalDate startDate, LocalDate endDate)     {
        // Complete the implementation here -- assembler required
        // Remove the return of the empty list
//        return new ArrayList<PlantInventoryEntryDTO>();
        List<PlantInventoryEntry> res = inventoryRepo.findAvailablePlants(name,startDate, endDate);
        return plantInventoryEntryAssembler.toResources(res);
    }

    public PlantInvItem findPlantInventoryItem(Long id) {
        return plantInvItemRepo.findById(id).orElse(null);
    }

    public PlantInventoryEntry findPlantInventoryEntry(Long id) { return  plantInventoryEntryRepository.getOne(id);}

    public Boolean isPlantInventoryItemExisting(Long id) { return  inventoryRepo.isPlantInventoryItemExisting(id);}

    public Boolean isPlantInventoryEntryExisting(Long id) { return  inventoryRepo.isPlantInventoryEntryExisting(id);}
}
