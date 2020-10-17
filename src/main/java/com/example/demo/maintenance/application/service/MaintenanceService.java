package com.example.demo.maintenance.application.service;

import com.example.demo.common.application.dto.BusinessPeriodValidator;
import com.example.demo.common.application.dto.ItemConditionValidator;
import com.example.demo.common.application.exception.*;
import com.example.demo.inventory.application.dto.PlantInvItemDTO;
import com.example.demo.inventory.application.service.PlantReservationAssembler;
import com.example.demo.inventory.domain.model.*;
import com.example.demo.inventory.domain.repository.PlantInvItemRepo;
import com.example.demo.inventory.domain.repository.PlantReservationRepo;
import com.example.demo.maintenance.application.dto.MaintenancePlanDTO;
import com.example.demo.maintenance.application.dto.MaintenanceTaskDTO;
import com.example.demo.maintenance.domain.model.MaintenancePlan;
import com.example.demo.maintenance.domain.model.MaintenanceTask;
import com.example.demo.maintenance.domain.repository.MaintenancePlanRepo;
import com.example.demo.maintenance.domain.repository.MaintenanceTaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.DataBinder;

import java.util.ArrayList;
import java.util.List;

@Service
public class MaintenanceService {
    @Autowired
    PlantInvItemRepo plantInvItemRepo;

    @Autowired
    MaintenancePlanRepo maintenancePlanRepo;

    @Autowired
    MaintenanceTaskRepo maintenanceTaskRepo;

    @Autowired
    MaintenancePlanAssembler maintenancePlanAssembler;

    @Autowired
    MaintenanceTaskAssembler maintenanceTaskAssembler;

    @Autowired
    PlantReservationAssembler plantReservationAssembler;

    PlantReservationRepo plantReservationRepo;

    @Autowired
    public MaintenanceService(PlantReservationRepo plantReservationRepo) {
        this.plantReservationRepo = plantReservationRepo;
    }

    public MaintenancePlanDTO createPlan(MaintenancePlanDTO planDTO) throws Exception {
        PlantInvItem plant = plantInvItemRepo.findById(planDTO.getPlant().get_id()).orElse(null);

        if (plant == null)
            throw new PlantNotFoundException("Plant NOT found");

        MaintenancePlan plan = MaintenancePlan.of(plant, planDTO.getYearOfAction());
        maintenancePlanRepo.save(plan);
        return maintenancePlanAssembler.toResource(plan);
    }


    public MaintenanceTaskDTO createTask(MaintenanceTaskDTO maintenanceTaskDTO, PlantReservation plantReservation, Long id) throws Exception {
        if(maintenancePlanRepo.findById(id).orElse(null) == null){
            throw new PlanNotFoundException("Plan NOT found");
        }

        BusinessPeriod period = BusinessPeriod.of(maintenanceTaskDTO.getTaskPeriod().getStartDate(), maintenanceTaskDTO.getTaskPeriod().getEndDate());

        DataBinder binder1 = new DataBinder(period);
        binder1.addValidators(new BusinessPeriodValidator());
        binder1.validate();

        if (binder1.getBindingResult().hasErrors())
            throw new InvalidTimePeriodException(binder1.getBindingResult().getAllErrors().get(0).getCode());


        DataBinder binder2 = new DataBinder(maintenanceTaskDTO);
        binder2.addValidators(new ItemConditionValidator(plantReservationRepo));
        binder2.validate();

        if (binder2.getBindingResult().hasErrors())
            throw new InvalidConditionException(binder2.getBindingResult().getAllErrors().get(0).getCode());


        MaintenanceTask maintenanceTask = MaintenanceTask.of(plantReservation, maintenanceTaskDTO.getDescription(),
                maintenanceTaskDTO.getTypeOfWork(), maintenanceTaskDTO.getPrice(), period);

        maintenanceTaskRepo.save(maintenanceTask);
        MaintenancePlan plan = maintenancePlanRepo.getOne(id);
        plan.addTask(maintenanceTask);
        maintenancePlanRepo.save(plan);
        return maintenanceTaskAssembler.toResource(maintenanceTask);
    }

    public MaintenancePlanDTO updateMaintenancePlan(MaintenancePlanDTO planDTO, Long id) throws Exception {
        MaintenancePlan maintenancePlan = maintenancePlanRepo.findById(id).orElse(null);
        if (maintenancePlan == null)
            throw new PlanNotFoundException("Plan NOT found");

        if (planDTO.getYearOfAction() != null)
            maintenancePlan.setYearOfAction(planDTO.getYearOfAction());

        PlantInvItem plantInvItem = null;

        PlantInvItemDTO plantInvItemDTO = planDTO.getPlant();

        if (plantInvItemDTO != null) {
            plantInvItem = plantInvItemRepo.findById(planDTO.getPlant().get_id()).orElse(null);
            maintenancePlan.setPlant(plantInvItem);
        }

        maintenancePlanRepo.save(maintenancePlan);
        return maintenancePlanAssembler.toResource(maintenancePlan);
    }


    public MaintenanceTaskDTO updateTask(MaintenanceTaskDTO maintenanceTaskDTO, Long id) throws Exception {
        MaintenanceTask maintenanceTask = maintenanceTaskRepo.findById(id).orElse(null);
        if(maintenanceTask == null){
            throw new TaskNotFoundException("Task NOT found");
        }

        PlantReservation plantReservation = maintenanceTask.getReservation();



        if (maintenanceTaskDTO.getTaskPeriod() != null) {
            BusinessPeriod period = BusinessPeriod.of(maintenanceTaskDTO.getTaskPeriod().getStartDate(), maintenanceTaskDTO.getTaskPeriod().getEndDate());

            DataBinder binder1 = new DataBinder(period);
            binder1.addValidators(new BusinessPeriodValidator());
            binder1.validate();

            if (binder1.getBindingResult().hasErrors())
                throw new InvalidTimePeriodException(binder1.getBindingResult().getAllErrors().get(0).getCode());

            maintenanceTask.setTaskPeriod(BusinessPeriod.of(maintenanceTaskDTO.getTaskPeriod().getStartDate(),
                    maintenanceTaskDTO.getTaskPeriod().getEndDate()));
        }

        if (maintenanceTaskDTO.getPrice() != null) {
            maintenanceTask.setPrice(maintenanceTaskDTO.getPrice());
        }

        if (maintenanceTaskDTO.getDescription() != null) {
            maintenanceTask.setDescription(maintenanceTaskDTO.getDescription());
        }

        if (maintenanceTaskDTO.getReservation() != null && maintenanceTaskDTO.getTypeOfWork() != null) {
            // in feature send reservation itself to binder through constructor
            DataBinder binder2 = new DataBinder(maintenanceTaskDTO);
            binder2.addValidators(new ItemConditionValidator(plantReservationRepo));
            binder2.validate();
            if (binder2.getBindingResult().hasErrors())
                throw new InvalidConditionException(binder2.getBindingResult().getAllErrors().get(0).getCode());


            maintenanceTask.setTypeOfWork(maintenanceTaskDTO.getTypeOfWork());
            maintenanceTask.setReservation(plantReservationRepo.findById(maintenanceTaskDTO.getReservation().get_id()).orElse(null));
        }

        if (maintenanceTaskDTO.getReservation() != null && maintenanceTaskDTO.getTypeOfWork() == null) {
            maintenanceTaskDTO.setTypeOfWork(maintenanceTask.getTypeOfWork());

            DataBinder binder2 = new DataBinder(maintenanceTaskDTO);
            binder2.addValidators(new ItemConditionValidator(plantReservationRepo));
            binder2.validate();

            if (binder2.getBindingResult().hasErrors())
                throw new InvalidConditionException(binder2.getBindingResult().getAllErrors().get(0).getCode());

            maintenanceTask.setReservation(plantReservationRepo.findById(maintenanceTaskDTO.getReservation().get_id()).orElse(null));
        }

        if (maintenanceTaskDTO.getReservation() == null && maintenanceTask.getTypeOfWork() != null) {
            maintenanceTaskDTO.setReservation(plantReservationAssembler.toResource(maintenanceTask.getReservation()));

            DataBinder binder2 = new DataBinder(maintenanceTaskDTO);
            binder2.addValidators(new ItemConditionValidator(plantReservationRepo));
            binder2.validate();

            if (binder2.getBindingResult().hasErrors())
                throw new InvalidConditionException(binder2.getBindingResult().getAllErrors().get(0).getCode());


            maintenanceTask.setTypeOfWork(maintenanceTaskDTO.getTypeOfWork());
        }


        maintenanceTaskRepo.save(maintenanceTask);
        return maintenanceTaskAssembler.toResource(maintenanceTask);
    }

    public void deleteMaintenanceTask(Long id) throws Exception {
        if(maintenanceTaskRepo.findById(id).orElse(null) == null){
            throw new TaskNotFoundException("Task NOT found");
            }


        maintenanceTaskRepo.deleteById(id);
    }

    public void deleteMaintenancePlan(Long id) throws Exception{
        if(maintenancePlanRepo.findById(id).orElse(null) == null){
            throw new PlanNotFoundException("Plan NOT found");
        }
        maintenancePlanRepo.deleteById(id);
    }

    public MaintenancePlanDTO findPlan(Long id) throws Exception {
        if(maintenancePlanRepo.findById(id).orElse(null) == null){
            throw new PlanNotFoundException("Plan NOT found");
        }
        MaintenancePlan plan = maintenancePlanRepo.findById(id).orElse(null);
        return maintenancePlanAssembler.toResource(plan);
    }

    public List<MaintenanceTaskDTO> getMaintenanceTasks() {
        List<MaintenanceTask> tasks = maintenanceTaskRepo.findAll();
        List<MaintenanceTaskDTO> tasksAssembler = new ArrayList<>();
        tasks.forEach(t -> {
            tasksAssembler.add(maintenanceTaskAssembler.toResource(t));
        });

        return tasksAssembler;
    }

    public MaintenanceTaskDTO findTask(Long id) throws Exception {
        if(maintenanceTaskRepo.findById(id).orElse(null) == null){
            throw new TaskNotFoundException("Task NOT found");
        }
        MaintenanceTask task = maintenanceTaskRepo.findById(id).orElse(null);
        return maintenanceTaskAssembler.toResource(task);
    }
}
