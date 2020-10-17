package com.example.demo.sales;

import com.example.demo.DemoApplication;
import com.example.demo.inventory.domain.model.PlantInventoryEntry;
import com.example.demo.inventory.domain.model.PlantInvItem;
import com.example.demo.inventory.domain.model.EquipmentCondition;
import com.example.demo.inventory.domain.repository.PlantInventoryEntryRepository;
import com.example.demo.inventory.domain.repository.PlantInvItemRepo;
import com.example.demo.sales.domain.repository.PurchaseOrderRepository;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlDateInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static java.lang.Long.parseLong;

@ContextConfiguration(classes = DemoApplication.class)
@WebAppConfiguration
public class CreationOfPurchaseOrderSteps {

    @Autowired
    private WebApplicationContext wac;

    private WebClient customerBrowser;
    HtmlPage customerPage;

    @Autowired
    PlantInventoryEntryRepository plantInventoryEntryRepository;
    @Autowired
    PlantInvItemRepo plantInventoryItemRepository;

    @Autowired
    PurchaseOrderRepository purchaseOrderRepository;

    @Before  // Use `Before` from Cucumber library
    public void setUp() {
        customerBrowser = MockMvcWebClientBuilder.webAppContextSetup(wac).build();
    }

    @After  // Use `After` from Cucumber library
    public void tearOff() {
        purchaseOrderRepository.deleteAll();
        plantInventoryItemRepository.deleteAll();
        plantInventoryEntryRepository.deleteAll();
    }





    @Given("^the following plant catalog$")
    public void the_following_plant_catalog(List<PlantInventoryEntry> entries) throws Throwable {
        plantInventoryEntryRepository.saveAll(entries);
    }



    @Given("^the following inventory$")
    public void the_following_inventory(DataTable table) throws Throwable {

        for (Map<String, String> row: table.asMaps(String.class, String.class)) {
            plantInventoryItemRepository.save(
                    PlantInvItem.of(
                    parseLong(row.get("id")),
                    row.get("serialNumber"),
                    EquipmentCondition.valueOf(row.get("equipmentCondition")),
                    plantInventoryEntryRepository.findById( parseLong( row.get("plantInfo") ) ).orElse(null)
                    )
            );

        }}


    @Given("^a customer is in the \"([^\"]*)\" web page$")
    public void a_customer_is_in_the_web_page(String pageTitle) throws Throwable {
        customerPage = customerBrowser.getPage("http://localhost/dashboard/catalog/form");
    }



    @Given("^no purchase order exists in the system$")
    public void no_purchase_order_exists_in_the_system() throws Throwable {
    }

    @When("^the customer queries the plant catalog for an \"([^\"]*)\" available from \"([^\"]*)\" to \"([^\"]*)\"$")
    public void the_customer_queries_the_plant_catalog_for_an_available_from_to(String plantName, String startDate, String endDate) throws Throwable {
        // The following elements are selected by their identifier
        HtmlTextInput nameInput = (HtmlTextInput)customerPage.getElementById("name");
        HtmlDateInput startDateInput = (HtmlDateInput)customerPage.getElementById("rental-start-date");
        HtmlDateInput endDateInput = (HtmlDateInput)customerPage.getElementById("rental-end-date");
        HtmlButton submit = (HtmlButton)customerPage.getElementById("submit-button");

        nameInput.setValueAttribute(plantName);
        startDateInput.setValueAttribute(startDate);
        endDateInput.setValueAttribute(endDate);

        customerPage = submit.click();
    }

    @Then("^(\\d+) plants are shown$")
    public void plants_are_shown(int numberOfPlants) throws Throwable {
        List<?> rows = customerPage.getByXPath("//tr[contains(@class, 'table-row')]");
        assert(rows.size()==6);
//        purchaseOrderRepository.deleteAll();
//        plantInventoryItemRepository.deleteAll();
//        plantInventoryEntryRepository.deleteAll();
        // Complete this step and do not forget to remove the following line
//        throw new PendingException();
    }

    @When("^the customer selects a \"([^\"]*)\"$")
    public void the_customer_selects_a(String plantDescription) throws Throwable {
        List<HtmlButton> buttons = customerPage.getByXPath(String.format("//tr[./td = '%s']//button", plantDescription));
        customerPage = buttons.get(0).click();

    }

    @Then("^a purchase order should be created with a total price of (\\d+\\.\\d+)$")
    public void a_purchase_order_should_be_created_with_a_total_price_of(BigDecimal total) throws Throwable {
        // Complete this step and do not forget to remove the following line
        throw new PendingException();
    }
}






