package com.savlukov.app.domain.model

import java.util.Date

data class CommercialSignal(
    val subject: String,
    val category: SignalCategory,
    val confidence: Float,
    val sentiment: String? = null,
    val timestamp: Date = Date(),
    val metadata: Map<String, String> = emptyMap()
)

enum class SignalCategory {
    INTEREST,
    EVALUATION,
    PROBLEM,
    PURCHASE_INTENT,
    BRAND_AFFINITY,
    BUSINESS_CONTEXT,
    RECOMMENDATION_REQUEST
}
