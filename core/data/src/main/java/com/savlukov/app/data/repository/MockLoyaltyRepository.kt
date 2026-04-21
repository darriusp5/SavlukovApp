package com.savlukov.app.data.repository

import com.savlukov.app.domain.model.GiveawayResult
import com.savlukov.app.domain.model.LoyaltyUser
import com.savlukov.app.domain.repository.LoyaltyRepository
import com.savlukov.app.domain.util.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MockLoyaltyRepository @Inject constructor() : LoyaltyRepository {
    override fun getProfile(token: String): Flow<Resource<LoyaltyUser>> = flow {
        emit(Resource.Loading())
        delay(500)
        emit(Resource.Success(LoyaltyUser("user_1", "+375291234567", 1500, 0, null, "dev_123")))
    }

    override fun enterGiveaway(token: String, deviceId: String): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())
        delay(1000)
        emit(Resource.Success(true))
    }

    override fun getPastWinners(): Flow<Resource<List<GiveawayResult>>> = flow {
        emit(Resource.Loading())
        delay(500)
        emit(Resource.Success(listOf(
            GiveawayResult("g1", "Ivan Ivanov", "Onyx Sofa", "2026-04-01"),
            GiveawayResult("g2", "Petr Petrov", "Gold Chair", "2026-04-08")
        )))
    }
}
