package com.ge.exercise4.exception;

public class EngineException extends Exception {

    public EngineException(ErrorCode errorCode) {
        super(errorCode.toString());
    }
}
