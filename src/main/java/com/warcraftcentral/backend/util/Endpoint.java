package com.warcraftcentral.backend.util;

import java.util.Locale;
import lombok.Getter;

public class Endpoint {
    @Getter
    private final String apiUrl, name, realm, region;

    public Endpoint(String name, String realm, String region) {
        this("https://%s.api.blizzard.com", name, realm, region);
    }

    public Endpoint(String apiUrl, String name, String realm, String region) {
        this.apiUrl = apiUrl;
        this.name = name.toLowerCase(Locale.ROOT).replace(" ", "");
        this.realm = realm.toLowerCase(Locale.ROOT).replace("'", "").replace(" ", "");
        this.region = region.toLowerCase(Locale.ROOT);
    }

    public String getCharacterEndpointWithNamespace() {
        return String.format(this.getCharacterEndpoint() + "?namespace=profile-%s", this.region);
    }

    public String getCharacterEndpoint() {
        return String.format(apiUrl + "/profile/wow/character/%s/%s", this.region, this.realm, this.name);
    }

    public String getPvPSummaryEndpoint() {
        return String.format(this.getCharacterEndpoint() + "/pvp-summary?namespace=profile-%s", this.region);
    }

    public String getPVPBracketEndpoint(String bracket) {
        return String.format(this.getCharacterEndpoint() + "/pvp-bracket/%s?namespace=profile-%s", bracket, this.region);
    }

    public String getSeasonIndexEndpoint() {
        return String.format(apiUrl + "/data/wow/pvp-season/index?namespace=dynamic-%s", this.region, this.region);
    }

    public String getStatisticsEndpoint() {
        return String.format(this.getCharacterEndpoint() + "/achievements/statistics?namespace=profile-%s", this.region);
    }

}
