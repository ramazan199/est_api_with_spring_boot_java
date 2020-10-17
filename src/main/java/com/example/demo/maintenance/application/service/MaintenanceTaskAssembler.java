package com.example.demo.maintenance.application.service;

import com.example.demo.common.application.dto.BusinessPeriodDTO;
import com.example.demo.inventory.application.service.PlantInventoryItemAssembler;
import com.example.demo.inventory.application.service.PlantReservationAssembler;
import com.example.demo.maintenance.application.dto.MaintenanceTaskDTO;
import com.example.demo.maintenance.domain.model.MaintenanceTask;
import com.example.demo.maintenance.rest.MaintenanceRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

@Service
public class MaintenanceTaskAssembler extends ResourceAssemblerSupport<MaintenanceTask, MaintenanceTaskDTO> {
    public MaintenanceTaskAssembler(){
        super(MaintenanceRestController.class, MaintenanceTaskDTO.class);
    }

    @Autowired
    PlantInventoryItemAssembler plantInventoryItemAssembler;

    @Autowired
    PlantReservationAssembler plantReservationAssembler;
    @Override
    public MaintenanceTaskDTO toResource(MaintenanceTask maintenanceTask) {
        MaintenanceTaskDTO dto = createResourceWithId(maintenanceTask.getId(), maintenanceTask);
        dto.set_id(maintenanceTask.getId());
        dto.setDescription(maintenanceTask.getDescription());
        dto.setPrice(maintenanceTask.getPrice());
        dto.setTypeOfWork(maintenanceTask.getTypeOfWork());
        dto.setTaskPeriod( BusinessPeriodDTO.of(maintenanceTask.getTaskPeriod().getStartDate(), maintenanceTask.getTaskPeriod().getEndDate()));

        dto.setReservation(plantReservationAssembler.toResource(maintenanceTask.getReservation()));
        return dto;
    }
}
