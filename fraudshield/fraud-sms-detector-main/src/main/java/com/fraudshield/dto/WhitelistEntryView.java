package com.fraudshield.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WhitelistEntryView {
    private String sender;
    private String url;
    private String type;
    private String createdDate;
}