package com.fraudshield.app.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FraudShieldApi {

    @POST("api/ingest")
    suspend fun ingest(@Body request: IngestRequest): IngestResponse

    @GET("api/getmlfeedback/{job_id}")
    suspend fun getVerdict(@Path("job_id") jobId: String): VerdictResponse

    @POST("api/userfeedback")
    suspend fun sendFeedback(@Body request: UserFeedbackRequest): Map<String, String>

    @GET("api/delta-sync")
    suspend fun deltaSync(): DeltaSyncResponse
}
