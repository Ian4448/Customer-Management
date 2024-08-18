package com.borges.project.api;

import com.borges.project.model.Customer;
import com.borges.project.service.CustomerDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
public class CustomerInformationAPI {
    private final CustomerDatabaseService customerDatabaseService;

    @Autowired
    public CustomerInformationAPI(CustomerDatabaseService customerDatabaseService) {
        this.customerDatabaseService = customerDatabaseService;
    }

    @PostMapping({"/customers", "/customers/"})
    public Customer addCustomer(@RequestBody Customer customer) {
        customerDatabaseService.addCustomer(customer);
        return customer;
    }

    // Todo(07/26/2024) test functionality, add unit tests
    @PutMapping({"/updateCustomer", "/updateCustomer/"})
    public Customer updateCustomer(@RequestBody Customer customer) {
        customerDatabaseService.updateCustomer(customer.getUserName(), customer);
        return customer;
    }

    // Todo(07/26/2024) test functionality, add unit tests
    @DeleteMapping
    public void deleteCustomer(@RequestBody Customer customer) {
        customerDatabaseService.deleteCustomer(customer.getUserName());
    }

}
