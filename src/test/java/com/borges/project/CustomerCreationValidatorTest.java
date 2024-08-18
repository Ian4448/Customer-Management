package com.borges.project;

import com.borges.project.errorhandling.CustomerRequestException;
import com.borges.project.model.Customer;
import com.borges.project.validation.CustomerCreationValidator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CustomerCreationValidatorTest {
    private static final CustomerCreationValidator CUSTOMER_CREATION_VALIDATOR = new CustomerCreationValidator();

    private final Customer VALID_CUSTOMER =
            new Customer("ianffdfdfdf",
                    "borgesfdfdfdf",
                    "ianburger.fg@gmail.com",
                    "ianborges552",
                    "Password123!");

    @Test
    public void validateCustomerCreation_validData_throwsNoException() {
        CUSTOMER_CREATION_VALIDATOR.validate(VALID_CUSTOMER);
    }

    @Test
    public void validateCustomerCreation_invalidDataCustomerFirstNameNotSet_throwsException() {
        Customer customer = VALID_CUSTOMER.clearFirstName();
        System.out.println(customer);
        CustomerRequestException exception =
                assertThrows(CustomerRequestException.class, () -> CUSTOMER_CREATION_VALIDATOR.validate(customer));
        assertThat(exception).hasMessage("First name and last name must be set");
    }

    // Todo (ianb): create different message for last name error.
    @Test
    public void validateCustomerCreation_invalidDataCustomerLastNameNotSet_throwsException() {
        Customer customer = VALID_CUSTOMER.clearLastName();
        CustomerRequestException exception =
                assertThrows(CustomerRequestException.class, () -> CUSTOMER_CREATION_VALIDATOR.validate(customer));
        assertThat(exception).hasMessage("First name and last name must be set");
    }

    @Test
    public void validateCustomerCreation_invalidDataCustomerEmailNotSet_throwsException() {
        Customer customer = VALID_CUSTOMER.clearEmail();
        CustomerRequestException exception =
                assertThrows(CustomerRequestException.class, () -> CUSTOMER_CREATION_VALIDATOR.validate(customer));
        assertThat(exception).hasMessage("email must be set");
    }

    @Test
    public void validateCustomerCreation_invalidDataCustomerEmailNoAtSymbol_throwsException() {
        Customer customer = VALID_CUSTOMER.toBuilder().email("helloGoogle.com").build();
        CustomerRequestException exception =
                assertThrows(CustomerRequestException.class, () -> CUSTOMER_CREATION_VALIDATOR.validate(customer));
        assertThat(exception).hasMessage("email does not contain @");
    }

    @Test
    public void validateCustomerCreation_invalidDataCustomerUserNameTooShort_throwsException() {
        Customer customer = VALID_CUSTOMER.toBuilder().userName("ian").build();
        CustomerRequestException exception =
                assertThrows(CustomerRequestException.class, () -> CUSTOMER_CREATION_VALIDATOR.validate(customer));
        assertThat(exception).hasMessage("username must be at least 5 characters");
    }

    @Test
    public void validateCustomerCreation_invalidDataCustomerUserNameTooLong_throwsException() {
        Customer customer = VALID_CUSTOMER.toBuilder().userName("ianborgesianborgesianborges").build();
        CustomerRequestException exception =
                assertThrows(CustomerRequestException.class, () -> CUSTOMER_CREATION_VALIDATOR.validate(customer));
        assertThat(exception).hasMessage("username must be at most 20 characters");
    }

    @Test
    public void validateCustomerCreation_invalidDataCustomerUserNameHasSpecialCharacters_throwsException() {
        Customer customer = VALID_CUSTOMER.toBuilder().userName("ian@borges").build();
        CustomerRequestException exception =
                assertThrows(CustomerRequestException.class, () -> CUSTOMER_CREATION_VALIDATOR.validate(customer));
        assertThat(exception).hasMessage("username can only contain letters and digits");
    }

    @Test
    public void validateCustomerCreation_invalidDataCustomerPasswordTooShort_throwsException() {
        Customer customer = VALID_CUSTOMER.toBuilder().password("Pass1!").build();
        CustomerRequestException exception =
                assertThrows(CustomerRequestException.class, () -> CUSTOMER_CREATION_VALIDATOR.validate(customer));
        assertThat(exception).hasMessage("password must be at least 8 characters");
    }

    @Test
    public void validateCustomerCreation_invalidDataCustomerPasswordNoUppercase_throwsException() {
        Customer customer = VALID_CUSTOMER.toBuilder().password("password1!").build();
        CustomerRequestException exception =
                assertThrows(CustomerRequestException.class, () -> CUSTOMER_CREATION_VALIDATOR.validate(customer));
        assertThat(exception).hasMessage("password must contain at least one uppercase letter");
    }

    @Test
    public void validateCustomerCreation_invalidDataCustomerPasswordNoLowercase_throwsException() {
        Customer customer = VALID_CUSTOMER.toBuilder().password("PASSWORD1!").build();
        CustomerRequestException exception =
                assertThrows(CustomerRequestException.class, () -> CUSTOMER_CREATION_VALIDATOR.validate(customer));
        assertThat(exception).hasMessage("password must contain at least one lowercase letter");
    }

    @Test
    public void validateCustomerCreation_invalidDataCustomerPasswordNoDigit_throwsException() {
        Customer customer = VALID_CUSTOMER.toBuilder().password("Password!").build();
        CustomerRequestException exception =
                assertThrows(CustomerRequestException.class, () -> CUSTOMER_CREATION_VALIDATOR.validate(customer));
        assertThat(exception).hasMessage("password must contain at least one digit");
    }

    @Test
    public void validateCustomerCreation_invalidDataCustomerPasswordNoSpecialChar_throwsException() {
        Customer customer = VALID_CUSTOMER.toBuilder().password("Password1").build();
        CustomerRequestException exception =
                assertThrows(CustomerRequestException.class, () -> CUSTOMER_CREATION_VALIDATOR.validate(customer));
        assertThat(exception).hasMessage("password must contain at least one special character");
    }
}
