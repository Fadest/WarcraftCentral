package com.warcraftcentral.backend.entity;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import java.util.List;
import lombok.Data;

@Embeddable
@Data
public class PvPCharacter {

    private int honorLevel;
    private long totalHonorableKills;

    @ElementCollection
    @JoinColumn(name = "brackets")
    private List<BracketInformation> brackets;
}
