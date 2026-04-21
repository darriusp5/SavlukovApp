package com.savlukov.app.data.remote

import com.savlukov.app.domain.model.LoyaltyUser
import com.savlukov.app.domain.model.GiveawayResult
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface LoyaltyApi {
    @GET("loyalty/profile")
    suspend fun getLoyaltyProfile(@Header("Authorization") token: String): LoyaltyUser

    @POST("loyalty/giveaway/enter")
    suspend fun enterGiveaway(
        @Header("Authorization") token: String,
        @Body request: GiveawayEntryRequest
    ): GiveawayEntryResponse

    @GET("loyalty/giveaway/winners")
    suspend fun getPastWinners(): List<GiveawayResult>
}

data class GiveawayEntryRequest(
    val deviceId: String
)

data class GiveawayEntryResponse(
    val success: Boolean,
    val message: String,
    val newPointsBalance: Int?
)
