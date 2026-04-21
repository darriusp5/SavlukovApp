package com.savlukov.app.domain.use_case

import com.savlukov.app.domain.model.Fabric
import com.savlukov.app.domain.repository.FurnitureRepository
import com.savlukov.app.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFabricsUseCase @Inject constructor(
    private val repository: FurnitureRepository
) : UseCase<Unit, Flow<Resource<List<Fabric>>>>() {
    override suspend fun invoke(params: Unit): Flow<Resource<List<Fabric>>> {
        return repository.getFabrics()
    }
}
