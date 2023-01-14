package com.warcraftcentral.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class IllegalRegionException extends IllegalArgumentException {

    public IllegalRegionException(String region) {
        super(String.format("Invalid region %s", region));
    }
}
