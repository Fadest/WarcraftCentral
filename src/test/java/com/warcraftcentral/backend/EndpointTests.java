package com.warcraftcentral.backend;


import static org.assertj.core.api.Assertions.assertThat;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.warcraftcentral.backend.enums.PvPBracket;
import com.warcraftcentral.backend.enums.Region;
import com.warcraftcentral.backend.util.Endpoint;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@WireMockTest
class EndpointTests {

    @Test
    void testAmericaEndpoints() {
        final Endpoint endpoint = new Endpoint("Fadest", "Quel'Thalas", Region.US.getName());

        this.testEndpoints(endpoint);
    }

    private void testEndpoints(Endpoint endpoint) {
        String name = endpoint.getName();
        String realm = endpoint.getRealm();
        String region = endpoint.getRegion();

        assertThat(endpoint.getCharacterEndpoint()).isEqualTo(
            String.format("https://%s.api.blizzard.com/profile/wow/character/%s/%s", region, realm, name)
        );

        assertThat(endpoint.getCharacterEndpointWithNamespace()).isEqualTo(
            String.format("https://%s.api.blizzard.com/profile/wow/character/%s/%s?namespace=profile-%s", region, realm, name, region)
        );

        assertThat(endpoint.getPvPSummaryEndpoint()).isEqualTo(
            String.format("https://%s.api.blizzard.com/profile/wow/character/%s/%s/pvp-summary?namespace=profile-%s", region, realm, name, region)
        );

        assertThat(endpoint.getStatisticsEndpoint()).isEqualTo(
            String.format("https://%s.api.blizzard.com/profile/wow/character/%s/%s/achievements/statistics?namespace=profile-%s", region, realm, name, region)
        );

        assertThat(endpoint.getSeasonIndexEndpoint()).isEqualTo(
            String.format("https://%s.api.blizzard.com/data/wow/pvp-season/index?namespace=dynamic-%s", region, region)
        );

        for (PvPBracket value : PvPBracket.values()) {
            assertThat(endpoint.getPVPBracketEndpoint(value.getName())).isEqualTo(
                String.format("https://%s.api.blizzard.com/profile/wow/character/%s/%s/pvp-bracket/%s?namespace=profile-%s", region, realm, name,
                    value.getName(), region
                )
            );
        }
    }

    @Test
    void testEuropeEndpoints() {
        final Endpoint endpoint = new Endpoint("Mabk", "Ravencrest", Region.EU.getName());

        this.testEndpoints(endpoint);
    }

    @Test
    void testKoreaEndpoints() {
        final Endpoint endpoint = new Endpoint("응하하", "Azshara", Region.KR.getName());

        this.testEndpoints(endpoint);
    }

    @Test
    void testTaiwanEndpoints() {
        final Endpoint endpoint = new Endpoint("芷煙", "Hellscream", Region.TW.getName());

        this.testEndpoints(endpoint);
    }

}
