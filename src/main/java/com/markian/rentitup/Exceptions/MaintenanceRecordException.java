package com.markian.rentitup.Exceptions;

public class MaintenanceRecordException extends RuntimeException {
    public MaintenanceRecordException(String message) {
        super(message);
    }

    public MaintenanceRecordException(String message, Throwable cause) {
        super(message, cause);
    }
}
