package com.warcraftcentral.backend.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.warcraftcentral.backend.enums.Faction;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "characters")
public class Character {

    @Id
    private Long id;
    private String name;
    private String region;
    private String realm;
    private String realmSlug;

    @Enumerated(EnumType.STRING)
    private Faction faction;

    private int race;
    private int currentClass;
    private int currentSpecialization;

    private long lastUpdate;

    @JsonProperty("pvp")
    @JoinColumn(name = "pvp_characters")
    private PvPCharacter pvpCharacter;
}
