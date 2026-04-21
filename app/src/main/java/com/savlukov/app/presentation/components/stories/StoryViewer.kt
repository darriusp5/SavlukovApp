package com.savlukov.app.presentation.components.stories

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.savlukov.app.domain.model.Story
import com.savlukov.app.domain.model.StorySegment
import kotlinx.coroutines.delay

@Composable
fun StoryViewer(
    story: Story,
    onClose: () -> Unit,
    onNextStory: () -> Unit,
    onPreviousStory: () -> Unit,
    onShopTheLook: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentSegmentIndex by remember { mutableIntStateOf(0) }
    val currentSegment = story.segments[currentSegmentIndex]
    
    var isPaused by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPaused = true
                        tryAwaitRelease()
                        isPaused = false
                    },
                    onTap = { offset ->
                        if (offset.x < size.width / 3) {
                            if (currentSegmentIndex > 0) {
                                currentSegmentIndex--
                            } else {
                                onPreviousStory()
                            }
                        } else {
                            if (currentSegmentIndex < story.segments.size - 1) {
                                currentSegmentIndex++
                            } else {
                                onNextStory()
                            }
                        }
                    }
                )
            }
    ) {
        // Content
        AsyncImage(
            model = currentSegment.imageUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Gradient for readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.4f),
                            Color.Transparent,
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.4f)
                        )
                    )
                )
        )

        // Progress Bar
        StoryProgressBar(
            segmentsCount = story.segments.size,
            currentIndex = currentSegmentIndex,
            isPaused = isPaused,
            onSegmentComplete = {
                if (currentSegmentIndex < story.segments.size - 1) {
                    currentSegmentIndex++
                } else {
                    onNextStory()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 8.dp, end = 8.dp)
                .statusBarsPadding()
        )

        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, start = 16.dp, end = 16.dp)
                .statusBarsPadding(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = story.brandLogoUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = story.brandName,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
            }
        }

        // Overlay Text
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 120.dp, start = 24.dp, end = 24.dp)
        ) {
            currentSegment.headline?.let {
                Text(
                    text = it,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            currentSegment.body?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = it,
                    color = Color.White.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        // CTA: Shop the Look
        currentSegment.linkedProductId?.let { productId ->
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp)
                    .clickable { onShopTheLook(productId) },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = null,
                    tint = Color(0xFFD4AF37),
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    text = "Experience the Look",
                    color = Color.White,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
fun StoryProgressBar(
    segmentsCount: Int,
    currentIndex: Int,
    isPaused: Boolean,
    onSegmentComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        repeat(segmentsCount) { index ->
            val progress = remember { Animatable(0f) }
            
            LaunchedEffect(index, currentIndex, isPaused) {
                if (index < currentIndex) {
                    progress.snapTo(1f)
                } else if (index == currentIndex) {
                    if (isPaused) {
                        progress.stop()
                    } else {
                        progress.animateTo(
                            targetValue = 1f,
                            animationSpec = tween(
                                durationMillis = (5000 * (1f - progress.value)).toInt(),
                                easing = LinearEasing
                            )
                        )
                        onSegmentComplete()
                    }
                } else {
                    progress.snapTo(0f)
                }
            }

            LinearProgressIndicator(
                progress = progress.value,
                modifier = Modifier
                    .weight(1f)
                    .height(2.dp)
                    .clip(RoundedCornerShape(1.dp)),
                color = Color.White,
                trackColor = Color.White.copy(alpha = 0.3f)
            )
        }
    }
}
