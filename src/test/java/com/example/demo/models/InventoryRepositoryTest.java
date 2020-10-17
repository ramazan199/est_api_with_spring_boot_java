package com.example.demo.models;

import com.example.demo.DemoApplication;
import com.example.demo.inventory.domain.repository.InventoryRepo;
import com.example.demo.inventory.domain.repository.PlantInventoryEntryRepository;
import com.example.demo.inventory.domain.repository.PlantInvItemRepo;
import com.example.demo.inventory.domain.repository.PlantReservationRepo;
import com.example.demo.maintenance.domain.repository.MaintenancePlanRepo;
import com.example.demo.maintenance.domain.repository.MaintenanceTaskRepo;
import com.example.demo.utils.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DemoApplication.class)
@Sql(scripts = "/plants-dataset.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class InventoryRepositoryTest {
    @Autowired
    PlantInventoryEntryRepository plantInventoryEntryRepository;

    @Autowired
    PlantInvItemRepo plantInvItemRepo;

    @Autowired
    PlantReservationRepo plantReservationRepo;

    @Autowired
    InventoryRepo inventoryRepo;

    @Autowired
    MaintenanceTaskRepo maintenanceTaskRepo;

    @Autowired
    MaintenancePlanRepo maintenancePlanRepo;

    @Test
   @Sql(scripts = "/query1.sql")
    public void query1Test() {
        //TODO
        List<Pair<String, BigDecimal>> expectedResult = new ArrayList<>();
        expectedResult.add(new Pair("Plant 1", new BigDecimal("1620.00")));

        List<Pair<String, BigDecimal>> plantEntries = inventoryRepo.query1();
        assertThat(plantEntries).containsAll(expectedResult);
    }

    @Test
//    @Sql(scripts = "/query2-dataset.sql")
    public void query2Test() {
        List<Pair<String, Long>> expectedResult = new ArrayList<>();
        expectedResult.add(new Pair("Mini excavator", new Long(78)));
        
        // Check the total idle days between possible input start date and end date which are around Jan 2017 until February 2017
        assertThat(inventoryRepo
                .query2(LocalDate.of(2017, 1, 1), LocalDate.of(2017, 2, 1)))
                .containsAll(expectedResult);

        // Check if the start date and end date input value are beyond the start date and end date in Plant Reservation and Maintenance Task
        assertThat(inventoryRepo
                .query2(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 2, 1)))
                .hasSize(0);

    }

    @Test
//    @Sql(scripts = "/query3-dataset.sql")
    public void query3Test() {
        List<Pair<String, Long>> expectedResult1 = new ArrayList<>();
        expectedResult1.add(new Pair("Mini excavator", new Long(2)));

        List<Pair<String, Long>> expectedResult2 = new ArrayList<>();
        expectedResult2.add(new Pair("Midi excavator", new Long(1)));

        assertThat(inventoryRepo
                .query3("Mini", LocalDate.of(2020, 3, 17), LocalDate.of(2020, 3, 20)))
                .containsAll(expectedResult1);

        assertThat(inventoryRepo
                .query3("Midi", LocalDate.of(2020, 3, 26), LocalDate.of(2020, 3, 30)))
                .containsAll(expectedResult2);

    }

    @Test
//    @Sql(scripts = "/query4-dataset.sql")
    public void query4Test() {
        assertThat(inventoryRepo.query4().get(0)).isEqualTo("A03");
        assertThat(inventoryRepo.query4().get(1)).isEqualTo("A02");
        assertThat(inventoryRepo.query4().get(2)).isEqualTo("A01");
    }
}
