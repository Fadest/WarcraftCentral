package com.warcraftcentral.backend.service;

import com.warcraftcentral.backend.entity.BracketInformation;
import com.warcraftcentral.backend.entity.Character;
import com.warcraftcentral.backend.entity.PvPCharacter;
import com.warcraftcentral.backend.enums.Region;
import com.warcraftcentral.backend.util.Endpoint;
import org.springframework.stereotype.Service;

@Service
public interface BlizzardService {

    Character buildCharacter(Endpoint endpoint);

    Character buildCharacter(String name, String realm, Region region);

    PvPCharacter buildPvPCharacter(Endpoint endpoint);

    BracketInformation buildBracketInformation(String pvpBracketEndpoint, int i);
}
