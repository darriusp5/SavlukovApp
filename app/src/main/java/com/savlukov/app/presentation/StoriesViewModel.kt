package com.savlukov.app.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.savlukov.app.domain.model.Story
import com.savlukov.app.domain.repository.StoriesRepository
import com.savlukov.app.util.Resource
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
                        if (result.data?.isNotEmpty() == true) {
                            startTimer()
                        }
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
        val nextIndex = _state.value.currentStoryIndex + 1
        if (nextIndex < _state.value.stories.size) {
            markAsWatched(_state.value.stories[_state.value.currentStoryIndex].id)
            _state.value = _state.value.copy(
                currentStoryIndex = nextIndex,
                progress = 0f
            )
            startTimer()
        } else {
            // Close stories or loop? Closing usually.
        }
    }

    fun previousStory() {
        val prevIndex = _state.value.currentStoryIndex - 1
        if (prevIndex >= 0) {
            _state.value = _state.value.copy(
                currentStoryIndex = prevIndex,
                progress = 0f
            )
            startTimer()
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
