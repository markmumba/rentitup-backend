package com.markian.rentitup.Exceptions;

public class MachineImageException extends RuntimeException {
    public MachineImageException(String message) {
        super(message);
    }

    public MachineImageException(String message, Throwable cause) {
        super(message, cause);
    }
}
