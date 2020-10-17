package com.example.demo.maintenance.application.dto;

import com.example.demo.inventory.application.dto.PlantInvItemDTO;
import lombok.Data;

import org.springframework.hateoas.ResourceSupport;

@Data
public class MaintenancePlanDTO extends ResourceSupport {
    Long _id;
    Integer yearOfAction;

    PlantInvItemDTO plant;


    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }
    public PlantInvItemDTO getPlant() {
        return plant;
    }

    public void setPlant(PlantInvItemDTO plant) {
        this.plant = plant;
    }

    public Integer getYearOfAction() {
        return yearOfAction;
    }

    public void setYearOfAction(Integer yearOfAction) {
        this.yearOfAction = yearOfAction;
    }
}
