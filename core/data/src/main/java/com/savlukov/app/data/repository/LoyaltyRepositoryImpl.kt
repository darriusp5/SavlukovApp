package com.savlukov.app.data.repository

import com.savlukov.app.data.remote.GiveawayEntryRequest
import com.savlukov.app.data.remote.LoyaltyApi
import com.savlukov.app.domain.model.GiveawayResult
import com.savlukov.app.domain.model.LoyaltyUser
import com.savlukov.app.domain.repository.LoyaltyRepository
import com.savlukov.app.domain.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LoyaltyRepositoryImpl @Inject constructor(
    private val api: LoyaltyApi
) : LoyaltyRepository {

    override fun getProfile(token: String): Flow<Resource<LoyaltyUser>> = flow {
        emit(Resource.Loading)
        try {
            val profile = api.getLoyaltyProfile("Bearer $token")
            emit(Resource.Success(profile))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to fetch profile"))
        }
    }.flowOn(Dispatchers.IO)

    override fun enterGiveaway(token: String, deviceId: String): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading)
        try {
            val response = api.enterGiveaway("Bearer $token", GiveawayEntryRequest(deviceId))
            if (response.success) {
                emit(Resource.Success(true))
            } else {
                emit(Resource.Error(response.message))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to enter giveaway"))
        }
    }.flowOn(Dispatchers.IO)

    override fun getPastWinners(): Flow<Resource<List<GiveawayResult>>> = flow {
        emit(Resource.Loading)
        try {
            val winners = api.getPastWinners()
            emit(Resource.Success(winners))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to fetch winners"))
        }
    }.flowOn(Dispatchers.IO)
}
