package com.warcraftcentral.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CharacterNotFoundException extends RuntimeException {

    public CharacterNotFoundException(String characterName, String realm) {
        super(String.format("Character %s-%s has not been found", characterName, realm));
    }

}
