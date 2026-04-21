package com.savlukov.app.data.repository

import app.cash.turbine.test
import com.savlukov.app.data.remote.StoriesApi
import com.savlukov.app.domain.model.Story
import com.savlukov.app.domain.util.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class StoriesRepositoryImplTest {

    private val api = mockk<StoriesApi>()
    private val repository = StoriesRepositoryImpl(api)

    @Test
    fun `getStories should return success when api call succeeds`() = runTest {
        // Arrange
        val stories = listOf(
            Story("1", "Title 1", imageUrl = "url1", date = "2026-04-18")
        )
        coEvery { api.getStories() } returns stories

        // Act & Assert
        repository.getStories().test {
            assertEquals(Resource.Loading, awaitItem())
            val success = awaitItem()
            assert(success is Resource.Success)
            assertEquals(stories, (success as Resource.Success).data)
            awaitComplete()
        }
    }
}
