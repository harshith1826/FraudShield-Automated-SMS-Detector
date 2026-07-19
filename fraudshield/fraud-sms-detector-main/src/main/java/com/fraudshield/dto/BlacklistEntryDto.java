package com.fraudshield.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BlacklistEntryDto {

    @JsonProperty("domain")
    private String domain;

    @JsonProperty("confidence")
    private float confidence;

    @JsonProperty("category")
    private String category;
}