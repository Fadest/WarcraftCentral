package com.warcraftcentral.backend.enums;

import com.warcraftcentral.backend.exception.IllegalRegionException;
import java.util.Locale;

public enum Region {
    US,
    EU,
    KR,
    TW;

    public static Region getRegionByName(String name) {
        for (Region value : values()) {
            if (value.name().equalsIgnoreCase(name)) {
                return value;
            }
        }

        throw new IllegalRegionException(name);
    }

    public String getName() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}
