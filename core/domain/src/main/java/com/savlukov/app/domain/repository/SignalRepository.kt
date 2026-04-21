package com.savlukov.app.domain.repository

import com.savlukov.app.domain.model.CommercialSignal

interface SignalRepository {
    suspend fun trackSignal(signal: CommercialSignal)
}
