package com.borges.project.utils;

import com.borges.project.errorhandling.CustomerRequestException;

public final class ValidationUtil {

    /* Checks the statement, if false
     * @throws CustomerRequestException */
    public static void checkRequest(boolean statement, String message) {
        if (!statement) {
            throw new CustomerRequestException(message);
        }
    }

    private ValidationUtil() {
    }
}
