package com.example.demo.inventory.domain.repository;

import com.example.demo.inventory.domain.model.PlantInvItem;
import com.example.demo.inventory.domain.model.PlantInventoryEntry;
import com.example.demo.utils.Pair;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface CustomInventoryRepo {
    List<PlantInventoryEntry> findAvailablePlants(String name, LocalDate startDate, LocalDate endDate);
    List<PlantInvItem> findAvailableItems(PlantInventoryEntry entry, LocalDate startDate, LocalDate endDate);

    List<Pair<String, BigDecimal>> query1();

    List<Pair<String, Long>> query2(LocalDate startDate, LocalDate endDate);

    List<Pair<String, Long>> query3(String name, LocalDate startDate, LocalDate endDate);

    List<String> query4();


    Boolean isPlantInventoryItemExisting(Long piiId);
    Boolean isPlantInventoryEntryExisting(Long pieId);


}
