package com.warcraftcentral.backend.service;

import com.warcraftcentral.backend.entity.Character;
import com.warcraftcentral.backend.enums.Faction;
import com.warcraftcentral.backend.model.CharacterDTO;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class CharacterServiceTests {
    @Autowired
    private ModelMapper modelMapper;
    @MockBean
    private CharacterService characterService;

    @Test
    public void testSearchCharacters() {
        Character character = new Character();
        character.setId(1L);
        character.setName("Fadest");
        character.setRealm("Quel'Thalas");
        character.setRealmSlug("quelthalas");
        character.setRace(2);
        character.setFaction(Faction.HORDE);
        character.setCurrentClass(6);
        character.setCurrentSpecialization(252);

        Mockito.when(characterService.getCharacters("F")).thenReturn(List.of(modelMapper.map(character, CharacterDTO.class)));

        List<CharacterDTO> characters = characterService.getCharacters("F");
        Assertions.assertThat(characters).isNotEmpty();

        CharacterDTO returnedCharacter = characters.get(0);

        Assertions.assertThat(returnedCharacter.getName()).isEqualTo(character.getName());
        Assertions.assertThat(returnedCharacter.getRace()).isEqualTo(character.getRace());
        Assertions.assertThat(returnedCharacter.getRealm()).isEqualTo(character.getRealm());
        Assertions.assertThat(returnedCharacter.getCurrentSpecialization()).isEqualTo(character.getCurrentSpecialization());
        Assertions.assertThat(returnedCharacter.getCurrentClass()).isEqualTo(character.getCurrentClass());
        Assertions.assertThat(returnedCharacter.getFaction()).isEqualTo(character.getFaction());
    }

}