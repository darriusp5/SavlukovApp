package com.savlukov.app.data.remote

import com.savlukov.app.domain.model.Furniture
import retrofit2.http.GET

interface FurnitureApi {
    @GET("catalog")
    suspend fun getFurnitureList(): List<Furniture> // Using domain model as DTO for simplicity here, usually separate
}
