package com.warcraftcentral.backend.model;

import com.warcraftcentral.backend.enums.Faction;
import lombok.Data;

@Data
public class CharacterDTO {
    private String name;
    private String realm;
    private Faction faction;
    private int race;
    private int currentClass;
    private int currentSpecialization;
}
