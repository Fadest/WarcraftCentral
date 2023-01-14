package com.warcraftcentral.backend.service.implementation;

import com.warcraftcentral.backend.entity.Character;
import com.warcraftcentral.backend.enums.Region;
import com.warcraftcentral.backend.model.CharacterDTO;
import com.warcraftcentral.backend.repository.CharacterRepository;
import com.warcraftcentral.backend.service.BlizzardService;
import com.warcraftcentral.backend.service.CharacterService;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CharacterServiceImplementation implements CharacterService {

    private final CharacterRepository characterRepository;
    private final BlizzardService blizzardService;
    private final ModelMapper modelMapper;

    @Override
    public Character getCharacter(String name, String realm, String regionName, boolean update) {
        final Region region = Region.getRegionByName(regionName);

        String fixedName = name.replace(" ", "");
        String realmSlug = realm.toLowerCase(Locale.ROOT).replace("'", "").replace(" ", "");
        if (update) {
            return this.buildAndSaveCharacter(fixedName, realmSlug, region);
        }

        return this.fetchCharacter(fixedName, realmSlug, region);
    }

    @Override
    public List<CharacterDTO> getCharacters(String query) {
        String name = query;
        String realm = "";
        if (query.contains("-")) {
            String[] split = query.split("-");
            name = split[0];
            realm = split[1];
        }

        String fixedName = StringUtils.capitalize(name.toLowerCase(Locale.ROOT)).replace(" ", "");
        String realmSlug = realm.toLowerCase(Locale.ROOT).replace("'", "").replace(" ", "");

        if (realmSlug.isEmpty()) {
            return this.characterRepository.findCharactersByNameStartingWith(fixedName)
                .stream().map(character -> this.modelMapper.map(character, CharacterDTO.class)).toList();
        }

        return this.characterRepository.findCharactersByNameStartingWithAndRealmSlugStartsWith(fixedName, realmSlug)
            .stream().map(character -> this.modelMapper.map(character, CharacterDTO.class)).toList();

    }

    private Character fetchCharacter(String name, String realm, Region region) {
        Optional<Character> characterOptional = Optional.ofNullable(
            this.characterRepository.findCharactersByNameEqualsIgnoreCaseAndRealmSlugAndRegion(name, realm, region.getName()));
        if (characterOptional.isEmpty()) {
            return this.buildAndSaveCharacter(name, realm, region);
        }

        final Character character = characterOptional.get();
        final long lastUpdate = System.currentTimeMillis() - character.getLastUpdate();

        if (TimeUnit.MILLISECONDS.toDays(lastUpdate) >= 10) {
            return this.buildAndSaveCharacter(name, realm, region);
        }

        return character;
    }

    private Character buildAndSaveCharacter(String name, String realm, Region region) {
        Character character = this.blizzardService.buildCharacter(name, realm, region);
        if (character == null) {
            return null;
        }

        this.characterRepository.save(character);
        return character;
    }
}
