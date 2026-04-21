package com.savlukov.app.domain.repository

import com.savlukov.app.domain.model.GiveawayResult
import com.savlukov.app.domain.model.LoyaltyUser
import com.savlukov.app.util.Resource
import kotlinx.coroutines.flow.Flow

interface LoyaltyRepository {
    fun getProfile(token: String): Flow<Resource<LoyaltyUser>>
    fun enterGiveaway(token: String, deviceId: String): Flow<Resource<Boolean>>
    fun getPastWinners(): Flow<Resource<List<GiveawayResult>>>
}
