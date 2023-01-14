package com.warcraftcentral.backend.enums;

import com.warcraftcentral.backend.exception.IllegalBracketException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PvPBracket {
    TWO_VERSUS_TWO("ARENA_2v2"),
    THREE_VERSUS_TREE("ARENA_3v3"),
    BATTLEGROUNDS("BATTLEGROUNDS"),
    SOLO_SHUFFLE("SHUFFLE");

    private final String name;

    public static PvPBracket getBracketByName(String name) {
        for (PvPBracket value : values()) {
            if (value.getName().equalsIgnoreCase(name)) {
                return value;
            }
        }

        throw new IllegalBracketException(name);
    }

}
