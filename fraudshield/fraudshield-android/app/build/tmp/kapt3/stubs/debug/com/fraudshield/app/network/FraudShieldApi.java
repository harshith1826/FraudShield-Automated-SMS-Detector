package com.fraudshield.app.network;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u000e\u0010\u0002\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0018\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010\u0007\u001a\u00020\bH\u00a7@\u00a2\u0006\u0002\u0010\tJ\u0018\u0010\n\u001a\u00020\u000b2\b\b\u0001\u0010\f\u001a\u00020\rH\u00a7@\u00a2\u0006\u0002\u0010\u000eJ$\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\b0\u00102\b\b\u0001\u0010\f\u001a\u00020\u0011H\u00a7@\u00a2\u0006\u0002\u0010\u0012\u00a8\u0006\u0013"}, d2 = {"Lcom/fraudshield/app/network/FraudShieldApi;", "", "deltaSync", "Lcom/fraudshield/app/network/DeltaSyncResponse;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getVerdict", "Lcom/fraudshield/app/network/VerdictResponse;", "jobId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "ingest", "Lcom/fraudshield/app/network/IngestResponse;", "request", "Lcom/fraudshield/app/network/IngestRequest;", "(Lcom/fraudshield/app/network/IngestRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "sendFeedback", "", "Lcom/fraudshield/app/network/UserFeedbackRequest;", "(Lcom/fraudshield/app/network/UserFeedbackRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface FraudShieldApi {
    
    @retrofit2.http.POST(value = "api/ingest")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object ingest(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.fraudshield.app.network.IngestRequest request, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.fraudshield.app.network.IngestResponse> $completion);
    
    @retrofit2.http.GET(value = "api/getmlfeedback/{job_id}")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getVerdict(@retrofit2.http.Path(value = "job_id")
    @org.jetbrains.annotations.NotNull()
    java.lang.String jobId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.fraudshield.app.network.VerdictResponse> $completion);
    
    @retrofit2.http.POST(value = "api/userfeedback")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object sendFeedback(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.fraudshield.app.network.UserFeedbackRequest request, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.Map<java.lang.String, java.lang.String>> $completion);
    
    @retrofit2.http.GET(value = "api/delta-sync")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deltaSync(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.fraudshield.app.network.DeltaSyncResponse> $completion);
}