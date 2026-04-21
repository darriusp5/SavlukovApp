package com.savlukov.app.domain.use_case

import com.savlukov.app.domain.model.Furniture
import com.savlukov.app.domain.repository.FurnitureRepository
import com.savlukov.app.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFurnitureDetailUseCase @Inject constructor(
    private val repository: FurnitureRepository
) : UseCase<String, Flow<Resource<Furniture>>>() {
    override suspend fun invoke(params: String): Flow<Resource<Furniture>> {
        return repository.getFurnitureDetail(params)
    }
}
