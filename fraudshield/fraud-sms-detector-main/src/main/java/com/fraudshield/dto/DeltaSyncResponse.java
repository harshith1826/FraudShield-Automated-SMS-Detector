package com.fraudshield.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DeltaSyncResponse {

    @JsonProperty("new_whitelist_entries")
    private List<WhitelistEntryDto> newWhitelistEntries;

    @JsonProperty("new_blacklist_entries")
    private List<BlacklistEntryDto> newBlacklistEntries;

    @JsonProperty("last_sync_timestamp")
    private long lastSyncTimestamp;
}