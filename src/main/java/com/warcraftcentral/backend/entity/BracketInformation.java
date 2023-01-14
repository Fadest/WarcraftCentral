package com.warcraftcentral.backend.entity;

import com.warcraftcentral.backend.enums.PvPBracket;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class BracketInformation {

    private PvPBracket bracket;
    private int specialization;
    private int wins;
    private int loses;
    private int rating;
    private int maxRating;
}
