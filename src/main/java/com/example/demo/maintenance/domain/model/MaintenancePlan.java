package com.example.demo.maintenance.domain.model;

import com.example.demo.inventory.domain.model.PlantInvItem;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor(force=true,access= AccessLevel.PROTECTED)
@AllArgsConstructor(staticName="of")
public class MaintenancePlan {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Integer yearOfAction;

//    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true)
//    @JoinColumn(name = "maintenance_plan_id")
//    List<MaintenanceTask> tasks;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "plant_id")
    PlantInvItem plant;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "plan_id")
    List<MaintenanceTask> tasks;


    public static MaintenancePlan of(PlantInvItem item, Integer yearOfAction) {
        MaintenancePlan plan = new MaintenancePlan();
        plan.plant = item;
        plan.yearOfAction = yearOfAction;
        plan.tasks = new ArrayList<>();

        return plan;
    }

    public void addTask(MaintenanceTask maintenanceTask){
        this.tasks.add(maintenanceTask);
    }
}
