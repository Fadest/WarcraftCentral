package com.warcraftcentral.backend;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.warcraftcentral.backend.enums.PvPBracket;
import com.warcraftcentral.backend.enums.Region;
import com.warcraftcentral.backend.exception.CharacterNotFoundException;
import com.warcraftcentral.backend.exception.IllegalBracketException;
import com.warcraftcentral.backend.exception.IllegalRegionException;
import com.warcraftcentral.backend.service.BlizzardService;
import com.warcraftcentral.backend.util.Endpoint;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@WireMockTest
public class ExceptionTests {
    @Autowired
    private BlizzardService blizzardService;

    @Test
    void testInvalidRegionException() {
        Assertions.assertThrows(IllegalRegionException.class, () -> {
            Region.getRegionByName("America");
        });
    }

    @Test
    void testInvalidBracketException() {
        Assertions.assertThrows(IllegalBracketException.class, () -> {
            PvPBracket.getBracketByName("5v5");
        });
    }

    @Test
    void testCharacterNotFoundException(WireMockRuntimeInfo runtimeInfo) {
        final Endpoint endpoint = new Endpoint("http://localhost:" + runtimeInfo.getHttpPort() + "/%s", "Keyber", "Quel'Thalas", Region.US.getName());

        Assertions.assertThrows(CharacterNotFoundException.class, () -> blizzardService.buildCharacter(endpoint));
    }

}
