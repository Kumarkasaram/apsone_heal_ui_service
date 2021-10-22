package com.heal.uiservice.exception;



public class CustomExceptionHandler extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private final String errorMessage;

    public CustomExceptionHandler(Throwable cause, String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public CustomExceptionHandler(String errorMessage)
    {
        super(errorMessage);
        this.errorMessage  = errorMessage;
    }

    public String getSimpleMessage()    {
        return this.errorMessage;
    }

}

