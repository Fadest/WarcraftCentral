package com.warcraftcentral.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class IllegalBracketException extends IllegalArgumentException {

    public IllegalBracketException(String bracket) {
        super(String.format("Invalid bracket %s", bracket));
    }
}
