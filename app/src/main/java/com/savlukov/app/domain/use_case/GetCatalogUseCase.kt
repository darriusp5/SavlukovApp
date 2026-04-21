package com.savlukov.app.domain.use_case

import com.savlukov.app.domain.model.Furniture
import com.savlukov.app.domain.repository.FurnitureRepository
import com.savlukov.app.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCatalogUseCase @Inject constructor(
    private val repository: FurnitureRepository
) : UseCase<Unit, Flow<Resource<List<Furniture>>>>() {
    override suspend fun invoke(params: Unit): Flow<Resource<List<Furniture>>> {
        return repository.getCatalog()
    }
}
