package com.savlukov.app.feature.wheel.presentation

import androidx.compose.animation.core.*
import androidx.compose.foundation.border
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CircleOfCraft(
    rewards: List<String>,
    onWin: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val animatableAngle = remember { Animatable(0f) }
    var isSpinning by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        // The Wheel - using graphicsLayer for hardware-accelerated rotation
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    rotationZ = animatableAngle.value
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val canvasSize = size
                val radius = canvasSize.minDimension / 2
                val center = Offset(canvasSize.width / 2, canvasSize.height / 2)
                val segmentAngle = 360f / rewards.size

                rewards.forEachIndexed { index, reward ->
                    val startAngle = index * segmentAngle - 90f // Start from top
                    
                    // Draw segment background
                    drawArc(
                        color = if (index % 2 == 0) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.surface,
                        startAngle = startAngle,
                        sweepAngle = segmentAngle,
                        useCenter = true,
                        size = Size(radius * 2, radius * 2),
                        topLeft = Offset(center.x - radius, center.y - radius)
                    )

                    // Draw separator lines
                    val lineAngle = startAngle * (PI / 180).toFloat()
                    val lineEnd = Offset(
                        center.x + radius * cos(lineAngle),
                        center.y + radius * sin(lineAngle)
                    )
                    drawLine(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        start = center,
                        end = lineEnd,
                        strokeWidth = 1.dp.toPx()
                    )

                    // Draw reward text
                    drawContext.canvas.nativeCanvas.apply {
                        save()
                        val textAngle = (startAngle + segmentAngle / 2) * (PI / 180).toFloat()
                        val textRadius = radius * 0.65f
                        val x = center.x + textRadius * cos(textAngle)
                        val y = center.y + textRadius * sin(textAngle)
                        
                        translate(x, y)
                        rotate(startAngle + segmentAngle / 2 + 90f)
                        
                        val paint = android.graphics.Paint().apply {
                            color = android.graphics.Color.BLACK
                            textSize = 14.sp.toPx()
                            textAlign = android.graphics.Paint.Align.CENTER
                            isAntiAlias = true
                            typeface = android.graphics.Typeface.create("Montserrat", android.graphics.Typeface.BOLD)
                        }
                        drawText(reward.uppercase(), 0f, 0f, paint)
                        restore()
                    }
                }

                // Outer Ring
                drawCircle(
                    color = MaterialTheme.colorScheme.primary,
                    radius = radius,
                    style = Stroke(width = 4.dp.toPx())
                )
            }
        }

        // Center Pin (Non-rotating)
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.secondary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(MaterialTheme.colorScheme.secondary, CircleShape)
            )
        }

        // Pointer (Static)
        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val radius = size.minDimension / 2
            val center = Offset(size.width / 2, size.height / 2)
            
            // Draw a gold triangle pointing down at the top
            val path = Path().apply {
                moveTo(center.x, center.y - radius + 10.dp.toPx())
                lineTo(center.x - 15.dp.toPx(), center.y - radius - 20.dp.toPx())
                lineTo(center.x + 15.dp.toPx(), center.y - radius - 20.dp.toPx())
                close()
            }
            drawPath(path, MaterialTheme.colorScheme.secondary)
        }

        // Spin Button / Interaction
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    enabled = !isSpinning,
                    onClick = {
                        isSpinning = true
                        scope.launch {
                            val extraRotations = (5..10).random()
                            val randomOffset = (0..360).random().toFloat()
                            val targetAngle = animatableAngle.value + (360f * extraRotations) + randomOffset
                            
                            animatableAngle.animateTo(
                                targetValue = targetAngle,
                                animationSpec = tween(
                                    durationMillis = 6000,
                                    easing = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f) // Super smooth deceleration
                                )
                            )
                            
                            isSpinning = false
                            val finalAngle = (targetAngle % 360f + 360f) % 360f
                            val segmentSize = 360f / rewards.size
                            // Angle 0 is at -90deg in our draw logic. 
                            // So pointer at top (270deg relative to start)
                            val normalizedAngle = (360f - finalAngle) % 360f
                            val winningIndex = (normalizedAngle / segmentSize).toInt()
                            onWin(rewards[winningIndex % rewards.size])
                        }
                    }
                )
        )
    }
}
