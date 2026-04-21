package com.savlukov.app.domain.use_case

import com.savlukov.app.domain.repository.FurnitureRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class RefreshCatalogUseCaseTest {

    private val repository = mockk<FurnitureRepository>()
    private val refreshCatalogUseCase = RefreshCatalogUseCase(repository)

    @Test
    fun `invoke_callsRefreshCatalogOnRepository`() = runTest {
        // Arrange
        coEvery { repository.refreshCatalog() } returns Unit

        // Act
        refreshCatalogUseCase(Unit)

        // Assert
        coVerify(exactly = 1) { repository.refreshCatalog() }
    }
}
