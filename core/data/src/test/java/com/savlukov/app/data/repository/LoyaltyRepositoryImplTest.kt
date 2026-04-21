package com.savlukov.app.data.repository

import app.cash.turbine.test
import com.savlukov.app.data.remote.GiveawayEntryRequest
import com.savlukov.app.data.remote.GiveawayEntryResponse
import com.savlukov.app.data.remote.LoyaltyApi
import com.savlukov.app.domain.model.LoyaltyUser
import com.savlukov.app.domain.util.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class LoyaltyRepositoryImplTest {

    private val api = mockk<LoyaltyApi>()
    private val repository = LoyaltyRepositoryImpl(api)

    @Test
    fun `getProfile should return success when api call is successful`() = runTest {
        // Arrange
        val token = "valid_token"
        val user = LoyaltyUser("1", "John Doe", 100, "Platinum")
        coEvery { api.getLoyaltyProfile("Bearer $token") } returns user

        // Act & Assert
        repository.getProfile(token).test {
            assertEquals(Resource.Loading, awaitItem())
            val success = awaitItem()
            assert(success is Resource.Success)
            assertEquals(user, (success as Resource.Success).data)
            awaitComplete()
        }
    }

    @Test
    fun `enterGiveaway should return success when api call succeeds`() = runTest {
        // Arrange
        val token = "valid_token"
        val deviceId = "device123"
        val response = GiveawayEntryResponse(true, "Entered successfully", 100)
        coEvery { api.enterGiveaway("Bearer $token", GiveawayEntryRequest(deviceId)) } returns response

        // Act & Assert
        repository.enterGiveaway(token, deviceId).test {
            assertEquals(Resource.Loading, awaitItem())
            val success = awaitItem()
            assert(success is Resource.Success)
            assertEquals(true, (success as Resource.Success).data)
            awaitComplete()
        }
    }

    @Test
    fun `enterGiveaway should return error when api call fails with success=false`() = runTest {
        // Arrange
        val token = "valid_token"
        val deviceId = "device123"
        val errorMessage = "Already entered"
        val response = GiveawayEntryResponse(false, errorMessage, null)
        coEvery { api.enterGiveaway("Bearer $token", GiveawayEntryRequest(deviceId)) } returns response

        // Act & Assert
        repository.enterGiveaway(token, deviceId).test {
            assertEquals(Resource.Loading, awaitItem())
            val error = awaitItem()
            assert(error is Resource.Error)
            assertEquals(errorMessage, (error as Resource.Error).message)
            awaitComplete()
        }
    }
}
