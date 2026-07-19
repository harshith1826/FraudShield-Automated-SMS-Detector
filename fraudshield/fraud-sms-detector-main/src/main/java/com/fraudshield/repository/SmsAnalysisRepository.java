package com.fraudshield.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.fraudshield.model.SmsAnalysis;

@Repository
public interface SmsAnalysisRepository extends MongoRepository<SmsAnalysis, String> {

    List<SmsAnalysis> findAllByOrderByAnalyzedAtDesc();

    Page<SmsAnalysis> findBySenderContainingIgnoreCaseAndPredictionContainingIgnoreCase(
            String sender, String prediction, Pageable pageable);
}