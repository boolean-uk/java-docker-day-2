package com.booleanuk.api.payload.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public abstract class Response<T> {
    protected String status;
    protected T message;

    public void set (T message) {
        this.status  = "Success";
        this.message = message;
    }
}
