package com.borges.project.api;

import com.borges.project.errorhandling.CustomerRequestException;
import com.borges.project.model.Customer;
import com.borges.project.service.CustomerDatabaseService;
import com.borges.project.validation.CustomerCreationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import static com.borges.project.utils.ValidationUtil.checkRequest;

@Controller
public class CustomerCreationAPI {
    private static final CustomerCreationValidator customerCreationValidator = new CustomerCreationValidator();
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final CustomerDatabaseService customerDatabaseService;
    private final CustomerInformationAPI customerInformationAPI; // REST controller

    @Autowired
    public CustomerCreationAPI(CustomerDatabaseService customerDatabaseService, CustomerInformationAPI customerInformationAPI) {
        this.customerDatabaseService = customerDatabaseService;
        this.customerInformationAPI = customerInformationAPI;
    }

    @GetMapping({"/create", "/create/"})
    public String showCustomerForm(Model model) {
        model.addAttribute("customer", new Customer("", "", "", "", ""));
        return "index";
    }

    /*
     * Todo(07/26/2024) add unit tests for various examples
     * <p1> Verifies that customer userName & email don't already exist in h2 db.
     * <p2> Calls Customer Creation Validator for specific field validation
     * <p3> Encodes user's password and adds customer to the database
     * @throws CustomerRequestException from invalid user input in form
     * */
    @PostMapping({"/create", "/create/"})
    public String createCustomer(@ModelAttribute Customer customer, Model model) {
        try {
            checkRequest(!customerDatabaseService.hasUserName(customer.getUserName()), "Username already exists");
            checkRequest(!customerDatabaseService.hasEmail(customer.getEmail()), "Email already exists");
            customerCreationValidator.validate(customer);

            customer.setPassword(passwordEncoder.encode(customer.getPassword()));

            model.addAttribute("customer", customer);
            customerInformationAPI.addCustomer(customer); // Calling the REST controller to add customer
            return "result";
        } catch (CustomerRequestException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "index";
        }
    }
}
