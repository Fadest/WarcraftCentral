package com.warcraftcentral.backend.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.warcraftcentral.backend.entity.BracketInformation;
import com.warcraftcentral.backend.entity.Character;
import com.warcraftcentral.backend.entity.PvPCharacter;
import com.warcraftcentral.backend.enums.Faction;
import com.warcraftcentral.backend.enums.PvPBracket;
import com.warcraftcentral.backend.enums.Region;
import com.warcraftcentral.backend.util.Endpoint;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@WireMockTest
public class BlizzardServiceTests {
    @Autowired
    private BlizzardService blizzardService;

    @Test
    void testPlayerBuild(WireMockRuntimeInfo runtimeInfo) {
        final Endpoint endpoint = new Endpoint("http://localhost:" + runtimeInfo.getHttpPort() + "/%s", "Fadest", "Quel'Thalas", Region.US.getName());
        Character character = blizzardService.buildCharacter(endpoint);
        System.out.println("TESTING");
        assertThat(character.getName()).isEqualTo("Fadest");
        assertThat(character.getRegion()).isEqualTo("us");
        assertThat(character.getRealm()).isEqualTo("Quel'Thalas");
        assertThat(character.getRealmSlug()).isEqualTo("quelthalas");
        assertThat(character.getFaction()).isEqualTo(Faction.HORDE);

        assertThat(character.getRace()).isEqualTo(2);
        assertThat(character.getCurrentClass()).isEqualTo(6);
        assertThat(character.getCurrentSpecialization()).isEqualTo(252);
        assertThat(character.getId()).isEqualTo(228565007L);
        assertThat(character.getPvpCharacter()).isNotNull();
    }

    @Test
    void testPvPPlayer(WireMockRuntimeInfo runtimeInfo) {
        final Endpoint endpoint = new Endpoint("http://localhost:" + runtimeInfo.getHttpPort() + "/%s", "Fadest", "Quel'Thalas", Region.US.getName());
        final PvPCharacter character = blizzardService.buildPvPCharacter(endpoint);

        assertThat(character.getHonorLevel()).isEqualTo(32);
        assertThat(character.getTotalHonorableKills()).isEqualTo(3187);
        assertThat(character.getBrackets().size()).isEqualTo(4);
    }

    @Test
    void testBracketInformation(WireMockRuntimeInfo runtimeInfo) {
        final Endpoint endpoint = new Endpoint("http://localhost:" + runtimeInfo.getHttpPort() + "/%s", "Fadest", "Quel'Thalas", Region.US.getName());
        final BracketInformation bracketInformation = blizzardService.buildBracketInformation(endpoint.getPVPBracketEndpoint("shuffle-deathknight-unholy"), 34);

        assertThat(bracketInformation.getBracket()).isEqualTo(PvPBracket.SOLO_SHUFFLE);
        assertThat(bracketInformation.getSpecialization()).isEqualTo(252);
        assertThat(bracketInformation.getWins()).isEqualTo(2);
        assertThat(bracketInformation.getLoses()).isEqualTo(0);
        assertThat(bracketInformation.getRating()).isEqualTo(576);
        assertThat(bracketInformation.getMaxRating()).isEqualTo(0);
    }

}
