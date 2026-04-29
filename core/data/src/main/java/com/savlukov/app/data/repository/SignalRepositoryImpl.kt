package com.savlukov.app.data.repository

import android.util.Log
import com.savlukov.app.domain.model.CommercialSignal
import com.savlukov.app.domain.repository.SignalRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignalRepositoryImpl @Inject constructor() : SignalRepository {
    
    // A dedicated scope for silent background tasks
    private val signalScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override suspend fun trackSignal(signal: CommercialSignal) {
        // We launch in a separate scope to ensure it's "silent" and doesn't block the caller
        signalScope.launch {
            try {
                // In production, this would send to an analytics endpoint or local DB
                Log.d("CommercialSignal", "Tracking: ${signal.subject} [${signal.category}] - Confidence: ${signal.confidence}")
                
                // Simulate network latency or processing
                // networkApi.sendSignal(signal.toDto())
            } catch (e: Exception) {
                // Silently handle errors to not disrupt user flow
                Log.e("CommercialSignal", "Failed to track signal", e)
            }
        }
    }
}
