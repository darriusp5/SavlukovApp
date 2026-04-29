package com.savlukov.app.domain.use_case

import com.savlukov.app.domain.repository.FurnitureRepository
import javax.inject.Inject

class RefreshCatalogUseCase @Inject constructor(
    private val repository: FurnitureRepository
) : UseCase<Unit, Unit>() {
    override suspend fun invoke(params: Unit) {
        repository.refreshCatalog()
    }
}
