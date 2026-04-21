package com.savlukov.app.presentation.components.loyalty

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CircleOfCraft(
    rewards: List<String>,
    onWin: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var rotationAngle by remember { mutableFloatStateOf(0f) }
    val animatableAngle = remember { Animatable(0f) }
    var isSpinning by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .size(300.dp)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier
            .fillMaxSize()
            .clickable(enabled = !isSpinning) {
                isSpinning = true
                val targetAngle = 360f * 5 + (0..360).random().toFloat()
                kotlinx.coroutines.launch {
                    animatableAngle.animateTo(
                        targetValue = targetAngle,
                        animationSpec = tween(
                            durationMillis = 4000,
                            easing = FastOutSlowInEasing
                        )
                    )
                    isSpinning = false
                    val winningIndex = (((targetAngle % 360) / (360f / rewards.size)).toInt())
                    // Simplified winning logic
                    onWin(rewards[rewards.size - 1 - winningIndex])
                }
            }
        ) {
            val canvasSize = size
            val radius = canvasSize.minDimension / 2
            val center = Offset(canvasSize.width / 2, canvasSize.height / 2)

            // Draw segments
            val segmentAngle = 360f / rewards.size
            rewards.forEachIndexed { index, reward ->
                val startAngle = index * segmentAngle + animatableAngle.value
                drawArc(
                    color = if (index % 2 == 0) Color(0xFFF8F8F8) else Color(0xFFFFFFFF),
                    startAngle = startAngle,
                    sweepAngle = segmentAngle,
                    useCenter = true,
                    size = Size(radius * 2, radius * 2),
                    topLeft = Offset(center.x - radius, center.y - radius)
                )
                
                // Text drawing logic (native canvas)
                val textAngle = (startAngle + segmentAngle / 2) * (PI / 180).toFloat()
                val textRadius = radius * 0.7f
                val x = center.x + textRadius * cos(textAngle)
                val y = center.y + textRadius * sin(textAngle)
                
                drawContext.canvas.nativeCanvas.apply {
                    save()
                    translate(x, y)
                    rotate(startAngle + segmentAngle / 2 + 90f)
                    // drawText(...) - simplified
                    restore()
                }
            }

            // Draw border
            drawCircle(
                color = Color(0xFF1A1A1A),
                radius = radius,
                style = Stroke(width = 2.dp.toPx())
            )
            
            // Draw center pin
            drawCircle(
                color = Color(0xFFD4AF37),
                radius = 12.dp.toPx()
            )
        }
    }
}
