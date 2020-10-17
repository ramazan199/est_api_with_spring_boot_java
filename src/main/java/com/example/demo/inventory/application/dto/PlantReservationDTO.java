package com.example.demo.inventory.application.dto;

import com.example.demo.common.application.dto.BusinessPeriodDTO;
import com.example.demo.maintenance.application.dto.MaintenancePlanDTO;
import com.example.demo.sales.application.dto.PurchaseOrderDTO;
import lombok.Data;
import org.springframework.hateoas.ResourceSupport;


@Data
public class PlantReservationDTO extends ResourceSupport {

    Long _id;
    BusinessPeriodDTO schedule;
    PurchaseOrderDTO rental;
    PlantInvItemDTO plant;
    MaintenancePlanDTO maint_plan;
}
