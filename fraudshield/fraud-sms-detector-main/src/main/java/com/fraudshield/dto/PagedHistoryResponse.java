package com.fraudshield.dto;

import com.fraudshield.model.SmsAnalysis;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PagedHistoryResponse {
    private List<SmsAnalysis> records;
    private long totalRecords;
    private int totalPages;
    private int currentPage;
    private int pageSize;
}