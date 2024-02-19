package com.booleanuk.api.response;

public class ErrorResponse extends Response<Error>{

    public ErrorResponse(Error data) {
        super("Error", data);
    }
}
