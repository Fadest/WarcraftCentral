package com.warcraftcentral.backend.service;

import com.warcraftcentral.backend.entity.Character;
import com.warcraftcentral.backend.model.CharacterDTO;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface CharacterService {

    Character getCharacter(String name, String realm, String region, boolean update);

    List<CharacterDTO> getCharacters(String query);
}
