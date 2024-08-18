package com.borges.project.validation;

import com.borges.project.model.Customer;
import org.springframework.stereotype.Component;

import static com.borges.project.utils.ValidationUtil.checkRequest;

/* Customer Creation Validator */
@Component
public final class CustomerCreationValidator implements Validator {
    // Todo(07/24/2024) make error messages friendlier to customer
    // Todo(07/24/2024) write more tests
    @Override
    public void validate(Object object) {
        checkRequest(object instanceof Customer, "customer validation was given " +
                "non-customer object");
        Customer customer = (Customer) object;

        // Single check for all fields
        // Single check for all fields to ensure form completion
        boolean allFieldsIncomplete = (customer.getFirstName() == null || customer.getFirstName().isEmpty()) &&
                (customer.getLastName() == null || customer.getLastName().isEmpty()) &&
                (customer.getEmail() == null || customer.getEmail().isEmpty()) &&
                (customer.getUserName() == null || customer.getUserName().isEmpty()) &&
                (customer.getPassword() == null || customer.getPassword().isEmpty());

        checkRequest(!allFieldsIncomplete, "Form not completed");

        checkRequest((customer.getFirstName() != null && !customer.getFirstName().isEmpty())
                        && (customer.getLastName() != null && !customer.getLastName().isEmpty()),
                "First name and last name must be set");

        validateCustomer(customer);
    }

    private static void validateCustomer(Customer customer) {
        validateCustomerName(customer);

        checkRequest(customer.getEmail() != null && !customer.getEmail().isEmpty(),
                "email must be set");
        validateCustomerEmail(customer);

        checkRequest(customer.getUserName() != null && !customer.getUserName().isEmpty(),
                "username must be set");
        validateCustomerUserName(customer);

        checkRequest(customer.getPassword() != null && !customer.getPassword().isEmpty(),
                "password must be set");
        validateCustomerPassword(customer);
    }

    private static void validateCustomerName(Customer customer) {
        checkRequest(customer.getFirstName().length() > 3 || customer.getLastName().length() > 3,
                "customer name length should be above 1 character");
        checkRequest(customer.getFirstName().length() + customer.getLastName().length() < 30,
                "customer name length should be below 30 characters");

        // Use streams to validate characters
        customer.getFirstName().chars()
                .filter(c -> !Character.isLetter(c))
                .findAny()
                .ifPresent(c -> {
                    throw new IllegalArgumentException("Invalid character in first name: " + (char) c);
                });

        customer.getLastName().chars()
                .filter(c -> !Character.isLetter(c))
                .findAny()
                .ifPresent(c -> {
                    throw new IllegalArgumentException("Invalid character in last name: " + (char) c);
                });
    }

    private static void validateCustomerEmail(Customer customer) {
        String email = customer.getEmail();
        int count = 0;
        char[] chars = email.toCharArray();

        // mandatory for each email (needs . @)
        checkRequest(email.indexOf('@') != -1, "email does not contain @");
        checkRequest(email.indexOf('.') != -1, "email does not contain .");
        checkRequest(email.length() > 5 && email.length() < 254, "invalid customer email length");

        int atSymbolIndex = email.indexOf('@');

        for (char c : chars) {
            if (c == '@') {
                count++;
            }
        }
        checkRequest(count == 1, "multiple @ symbol");

        // Validate after @
        count = 0;
        for (int i = atSymbolIndex; i < chars.length; i++) {
            if (chars[i] == '.') {
                count++;
            }
        }
        checkRequest(count == 1, "multiple . symbol after @");

        // Ensure no characters after domain extension
        String domainPart = email.substring(atSymbolIndex + 1);
        checkRequest(domainPart.matches("^[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"), "invalid domain extension or characters after domain");

        // Ensure no characters after the last period
        String domainExtension = domainPart.substring(domainPart.lastIndexOf('.'));
        checkRequest(domainExtension.matches("\\.[a-zA-Z]{2,}$"), "invalid domain extension or characters after domain extension");
    }

    private static void validateCustomerUserName(Customer customer) {
        String userName = customer.getUserName();
        checkRequest(userName.length() >= 5, "username must be at least 5 characters");
        checkRequest(userName.length() <= 20, "username must be at most 20 characters");
        checkRequest(userName.chars().allMatch(Character::isLetterOrDigit), "username can only contain letters and digits");
    }

    private static void validateCustomerPassword(Customer customer) {
        String password = customer.getPassword();
        checkRequest(password.length() >= 8, "password must be at least 8 characters");
        checkRequest(password.length() <= 30, "password cannot be greater than 30 characters");

        boolean hasUppercase = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLowercase = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecialChar = password.chars().anyMatch(c -> !Character.isLetterOrDigit(c));

        checkRequest(hasUppercase, "password must contain at least one uppercase letter");
        checkRequest(hasLowercase, "password must contain at least one lowercase letter");
        checkRequest(hasDigit, "password must contain at least one digit");
        checkRequest(hasSpecialChar, "password must contain at least one special character");
    }
}
