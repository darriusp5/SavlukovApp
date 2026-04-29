package com.savlukov.app.feature.stories.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.savlukov.app.domain.model.Story
import com.savlukov.app.domain.repository.StoriesRepository
import com.savlukov.app.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StoriesState(
    val stories: List<Story> = emptyList(),
    val currentStoryIndex: Int = 0,
    val currentSegmentIndex: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isPaused: Boolean = false,
    val progress: Float = 0f
)

@HiltViewModel
class StoriesViewModel @Inject constructor(
    private val repository: StoriesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(StoriesState())
    val state: StateFlow<StoriesState> = _state.asStateFlow()

    private var timerJob: Job? = null

    init {
        loadStories()
    }

    fun loadStories() {
        viewModelScope.launch {
            repository.getStories().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            stories = result.data ?: emptyList(),
                            isLoading = false
                        )
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            error = result.message,
                            isLoading = false
                        )
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(isLoading = true)
                    }
                }
            }
        }
    }

    fun nextStory() {
        val currentStory = _state.value.stories.getOrNull(_state.value.currentStoryIndex) ?: return
        
        if (_state.value.currentSegmentIndex < currentStory.segments.size - 1) {
            // Next segment in same story
            _state.value = _state.value.copy(
                currentSegmentIndex = _state.value.currentSegmentIndex + 1,
                progress = 0f
            )
            startTimer()
        } else {
            // Next story
            val nextStoryIndex = _state.value.currentStoryIndex + 1
            if (nextStoryIndex < _state.value.stories.size) {
                markAsWatched(currentStory.id)
                _state.value = _state.value.copy(
                    currentStoryIndex = nextStoryIndex,
                    currentSegmentIndex = 0,
                    progress = 0f
                )
                startTimer()
            } else {
                // End of all stories
                timerJob?.cancel()
            }
        }
    }

    fun previousStory() {
        if (_state.value.currentSegmentIndex > 0) {
            // Previous segment in same story
            _state.value = _state.value.copy(
                currentSegmentIndex = _state.value.currentSegmentIndex - 1,
                progress = 0f
            )
            startTimer()
        } else {
            // Previous story
            val prevStoryIndex = _state.value.currentStoryIndex - 1
            if (prevStoryIndex >= 0) {
                val prevStory = _state.value.stories[prevStoryIndex]
                _state.value = _state.value.copy(
                    currentStoryIndex = prevStoryIndex,
                    currentSegmentIndex = (prevStory.segments.size - 1).coerceAtLeast(0),
                    progress = 0f
                )
                startTimer()
            }
        }
    }

    fun pauseStory() {
        _state.value = _state.value.copy(isPaused = true)
        timerJob?.cancel()
    }

    fun resumeStory() {
        _state.value = _state.value.copy(isPaused = false)
        startTimer()
    }

    fun onShopTheLookClick(productId: String) {
        // Handled by navigation in NavGraph or via SharedFlow events
    }

    private fun startTimer() {
        timerJob?.cancel()
        val currentStory = _state.value.stories.getOrNull(_state.value.currentStoryIndex) ?: return
        val durationMillis = currentStory.durationSeconds * 1000L
        val stepMillis = 50L
        
        timerJob = viewModelScope.launch {
            while (_state.value.progress < 1f) {
                delay(stepMillis)
                if (!_state.value.isPaused) {
                    val newProgress = _state.value.progress + (stepMillis.toFloat() / durationMillis)
                    _state.value = _state.value.copy(progress = newProgress.coerceAtMost(1f))
                }
            }
            nextStory()
        }
    }

    private fun markAsWatched(id: String) {
        viewModelScope.launch {
            repository.markStoryAsWatched(id)
        }
    }
}
