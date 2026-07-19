package com.fraudshield.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WhitelistEntryDto {

    @JsonProperty("domain")
    private String domain;

    @JsonProperty("organisation")
    private String organisation;

    @JsonProperty("trust_score")
    private float trustScore;
}