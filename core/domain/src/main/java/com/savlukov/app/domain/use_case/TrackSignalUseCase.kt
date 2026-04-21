package com.savlukov.app.domain.use_case

import com.savlukov.app.domain.model.CommercialSignal
import com.savlukov.app.domain.repository.SignalRepository
import javax.inject.Inject

class TrackSignalUseCase @Inject constructor(
    private val repository: SignalRepository
) {
    suspend operator fun invoke(signal: CommercialSignal) {
        repository.trackSignal(signal)
    }
}
