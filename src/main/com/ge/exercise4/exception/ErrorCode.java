package com.ge.exercise4.exception;

public enum ErrorCode {

    REBUILD_COUNT_EXCEEDS_MAX("Entered rebuild exceeds max. possible rebuild for this engine"),
    FLIGHT_HOURS_EXCEED_MAX("Entered flights hours max. possible flight hours for this engine"),
    FLIGHT_HOURS_INCONSISTENT("Entered flight hours do not match rebuilt count");

    public String getMessage() {
        return message;
    }

    private String message;

    ErrorCode(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
