package com.fraudshield.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IngestResponse {

    @JsonProperty("status")
    private String status;

    @JsonProperty("job_id")
    private String jobId;
}