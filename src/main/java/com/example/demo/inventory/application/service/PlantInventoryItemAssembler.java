package com.example.demo.inventory.application.service;

import com.example.demo.inventory.application.dto.PlantInvItemDTO;
import com.example.demo.inventory.domain.model.PlantInvItem;
import com.example.demo.inventory.rest.InventoryRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

@Service
public class PlantInventoryItemAssembler extends ResourceAssemblerSupport<PlantInvItem, PlantInvItemDTO> {
    public PlantInventoryItemAssembler()
    {
        super(InventoryRestController.class, PlantInvItemDTO.class);
    }
    @Autowired
    PlantInventoryEntryAssembler plantInventoryEntryAssembler;

    @Override
    public PlantInvItemDTO toResource(PlantInvItem plantInvItem) {
        PlantInvItemDTO dto = createResourceWithId(plantInvItem.getId(), plantInvItem);
        dto.set_id(plantInvItem.getId());
        dto.setSerialNumber(plantInvItem.getSerialNumber());
        dto.setEquipmentCondition(plantInvItem.getEquipmentCondition());
        dto.setPlantInfo( plantInventoryEntryAssembler.toResource(plantInvItem.getPlantInfo()));


        return dto;
    }
}
