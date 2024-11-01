package com.markian.rentitup.Exceptions;

import com.markian.rentitup.Machine.Machine;

public class MachineException extends  RuntimeException{
    public MachineException(String message) {
        super(message);
    }
    public MachineException(String message,Throwable cause) {
        super(message,cause);
    }
}
