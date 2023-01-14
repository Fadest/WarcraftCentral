package com.warcraftcentral.backend.service.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warcraftcentral.backend.entity.BracketInformation;
import com.warcraftcentral.backend.entity.Character;
import com.warcraftcentral.backend.entity.PvPCharacter;
import com.warcraftcentral.backend.enums.Faction;
import com.warcraftcentral.backend.enums.PvPBracket;
import com.warcraftcentral.backend.enums.Region;
import com.warcraftcentral.backend.exception.CharacterNotFoundException;
import com.warcraftcentral.backend.service.BlizzardService;
import com.warcraftcentral.backend.util.Endpoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class BlizzardServiceImplementation implements BlizzardService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final static int PLAYER_VERSUS_PLAYER_CATEGORY = 21;
    private final static Map<Integer, PvPBracket> BRACKET_CATEGORIES_IDS = Map.of(
        595, PvPBracket.THREE_VERSUS_TREE,
        370, PvPBracket.TWO_VERSUS_TWO
    );

    @Autowired
    public BlizzardServiceImplementation(RestTemplateBuilder restTemplateBuilder, ObjectMapper objectMapper,
                                         AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientServiceAndManager) {
        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest.withClientRegistrationId("blizzard")
            .principal("Demo Service")
            .build();

        OAuth2AuthorizedClient authorizedClient = authorizedClientServiceAndManager.authorize(authorizeRequest);
        OAuth2AccessToken accessToken = Objects.requireNonNull(authorizedClient).getAccessToken();

        this.restTemplate = restTemplateBuilder
            .defaultHeader("Authorization", "Bearer " + accessToken.getTokenValue())
            .build();
        this.objectMapper = objectMapper;
    }

    @Override
    public Character buildCharacter(Endpoint endpoint) {
        System.out.println("1");

        try {
            final ResponseEntity<String> responseEntity = restTemplate.getForEntity(endpoint.getCharacterEndpointWithNamespace(), String.class);

            final Character character = new Character();
            final JsonNode characterNode;
            System.out.println("2");

            try {
                characterNode = this.objectMapper.readTree(responseEntity.getBody());
            } catch (JsonProcessingException e) {
                System.out.println("null");
                throw new RuntimeException(e);
            }

            final JsonNode realmNode = characterNode.get("realm");
            character.setId(characterNode.get("id").asLong());
            character.setRealm(realmNode.get("name").get("en_US").asText());
            character.setRealmSlug(realmNode.get("slug").asText());

            character.setRegion(endpoint.getRegion());
            character.setName(characterNode.get("name").asText());
            character.setRace(characterNode.get("race").get("id").asInt());
            character.setFaction(Faction.valueOf(characterNode.get("faction").get("type").asText()));
            character.setCurrentClass(characterNode.get("character_class").get("id").asInt());
            character.setCurrentSpecialization(characterNode.get("active_spec").get("id").asInt());
            character.setLastUpdate(System.currentTimeMillis());

            final PvPCharacter pvpCharacter = buildPvPCharacter(endpoint);
            character.setPvpCharacter(pvpCharacter);

            return character;
        } catch (HttpClientErrorException e) {
            throw new CharacterNotFoundException(endpoint.getName(), endpoint.getRealm());
        }
    }

    @Override
    public Character buildCharacter(String name, String realm, Region region) {
        final Endpoint endpoint = new Endpoint(name.toLowerCase(Locale.ROOT), realm, region.getName());

        return this.buildCharacter(endpoint);
    }

    @Override
    public PvPCharacter buildPvPCharacter(Endpoint endpoint) {
        final PvPCharacter pvpCharacter = new PvPCharacter();
        try {
            final ResponseEntity<String> responseEntity = restTemplate.getForEntity(endpoint.getPvPSummaryEndpoint(), String.class);
            final JsonNode characterNode = this.objectMapper.readTree(responseEntity.getBody());

            pvpCharacter.setHonorLevel(characterNode.get("honor_level").asInt());
            pvpCharacter.setTotalHonorableKills(characterNode.get("honorable_kills").asLong());

            String seasonIndexRawJson = restTemplate.getForObject(endpoint.getSeasonIndexEndpoint(), String.class);
            final int currentSeason = this.objectMapper.readTree(seasonIndexRawJson).get("current_season").get("id").asInt();

            String statisticRawJson = restTemplate.getForObject(endpoint.getStatisticsEndpoint(), String.class);
            final JsonNode statisticsNode = this.objectMapper.readTree(statisticRawJson);

            Map<PvPBracket, Integer> maxRating = new HashMap<>();

            outerLoop:
            for (JsonNode categoryNode : statisticsNode.get("categories")) {
                if (categoryNode.get("id").asInt() != PLAYER_VERSUS_PLAYER_CATEGORY) {
                    continue;
                }

                for (JsonNode subCategoriesNode : categoryNode.get("sub_categories")) {
                    for (JsonNode statistics : subCategoriesNode.get("statistics")) {
                        int id = statistics.get("id").asInt();
                        if (!BRACKET_CATEGORIES_IDS.containsKey(id)) {
                            continue;
                        }

                        maxRating.put(BRACKET_CATEGORIES_IDS.get(id), statistics.get("quantity").asInt());

                        if (BRACKET_CATEGORIES_IDS.size() == 2) {
                            break outerLoop;
                        }
                    }
                }
            }

            List<BracketInformation> brackets = new ArrayList<>();
            JsonNode bracketsNode = characterNode.get("brackets");
            if (bracketsNode != null) {
                for (JsonNode bracketNode : bracketsNode) {
                    // We split this to get the URL of the bracket, that way we can use our Endpoint to get the URL.
                    // We also remove the namespace query.
                    String linkReference = bracketNode.get("href").asText().split("pvp-bracket/")[1].split("\\?")[0];
                    final BracketInformation bracketInformation = buildBracketInformation(endpoint.getPVPBracketEndpoint(linkReference), currentSeason);

                    bracketInformation.setMaxRating(maxRating.getOrDefault(bracketInformation.getBracket(), 0));

                    brackets.add(bracketInformation);
                }
            }

            // Let's add a default bracket if no found for the player in this season
            // This will not include Solo Shuffle since we don't know what specs this player has played.
            for (PvPBracket bracket : PvPBracket.values()) {
                if (bracket == PvPBracket.SOLO_SHUFFLE) {
                    continue;
                }

                if (brackets.stream().anyMatch(bracketInformation -> bracketInformation.getBracket() == bracket)) {
                    continue;
                }

                final BracketInformation bracketInformation = new BracketInformation();
                bracketInformation.setBracket(bracket);
                bracketInformation.setMaxRating(maxRating.getOrDefault(bracket, 0));
                brackets.add(bracketInformation);
            }

            pvpCharacter.setBrackets(brackets);
        } catch (HttpClientErrorException | JsonProcessingException e) {
            // No PVP data on this season.
        }

        return pvpCharacter;
    }

    @Override
    public BracketInformation buildBracketInformation(String url, int currentSeason) {
        final BracketInformation bracketInformation = new BracketInformation();
        try {
            final ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
            final JsonNode characterNode = this.objectMapper.readTree(responseEntity.getBody());

            if (characterNode.get("season").get("id").asInt() != currentSeason) {
                return bracketInformation;
            }

            final JsonNode seasonMatchStatistics = characterNode.get("season_match_statistics");

            bracketInformation.setRating(characterNode.get("rating").asInt());

            bracketInformation.setWins(seasonMatchStatistics.get("won").asInt());
            bracketInformation.setLoses(seasonMatchStatistics.get("lost").asInt());

            final PvPBracket pvpBracket = PvPBracket.getBracketByName(characterNode.get("bracket").get("type").asText());
            bracketInformation.setBracket(pvpBracket);

            if (pvpBracket == PvPBracket.SOLO_SHUFFLE) {
                bracketInformation.setSpecialization(characterNode.get("specialization").get("id").asInt());
            }
        } catch (HttpClientErrorException | JsonProcessingException ignored) {
        }

        return bracketInformation;
    }
}
