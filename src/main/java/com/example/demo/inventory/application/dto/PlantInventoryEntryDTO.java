package com.example.demo.inventory.application.dto;

import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

import java.math.BigDecimal;

@Data
public class PlantInventoryEntryDTO extends ResourceSupport {
    Long _id;
    String name;
    String description;
    BigDecimal price;
}
