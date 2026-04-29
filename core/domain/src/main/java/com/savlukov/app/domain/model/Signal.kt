package com.savlukov.app.domain.model

import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class CommercialSignal(
    val subject: String,
    val category: SignalCategory,
    val confidence: Float,
    val sentiment: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val metadata: Map<String, String> = emptyMap()
)

@Serializable
enum class SignalCategory {
    INTEREST,
    EVALUATION,
    PROBLEM,
    PURCHASE_INTENT,
    BRAND_AFFINITY,
    BUSINESS_CONTEXT,
    RECOMMENDATION_REQUEST
}
