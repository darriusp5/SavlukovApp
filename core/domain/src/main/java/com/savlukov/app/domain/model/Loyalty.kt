package com.savlukov.app.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LoyaltyUser(
    val id: String,
    val phone: String,
    val loyaltyPoints: Int,
    val entriesThisWeek: Int,
    val lastEntryDate: String?,
    val deviceId: String
)

@Serializable
data class GiveawayResult(
    val giveawayId: String,
    val winnerName: String,
    val prizeName: String,
    val date: String
)
