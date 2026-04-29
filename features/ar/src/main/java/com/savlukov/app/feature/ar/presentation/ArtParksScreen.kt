package com.savlukov.app.feature.ar.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import org.osmdroid.config.Configuration
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

data class Store(
    val name: String,
    val type: String, // "Арт-парк" or "Салон"
    val address: String,
    val hours: String,
    val phone: String,
    val position: GeoPoint
)

@Composable
fun ArtParksScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Stores data with precise coordinates for each address
    val stores = remember {
        listOf(
            Store("Арт-парк «Савлуков-Мебель»", "Арт-парк", "г. Витебск, пр-т Строителей 15", "ежедневно 10:00–21:00", "+375292921641", GeoPoint(55.1881, 30.1765)),
            Store("Фирменный салон «Савлуков-Мебель»", "Салон", "г. Могилёв, ул. Габровская 41", "пон. - пят.: 10:00–20:00; суб. - воскр.: 10:00–18:00", "+375297371641", GeoPoint(53.8921, 30.3172)),
            Store("Фирменный салон «Савлуков-Мебель»", "Салон", "г. Гродно, ул.Горького, 91\"Г\"", "ежедневно 10:00 - 20:00", "+375297011641", GeoPoint(53.6698, 23.8272)),
            Store("Арт-парк «Савлуков-Мебель»", "Арт-парк", "г. Брест, ул. Кирова 56", "ежедневно 10:00-21:00", "+375298351641", GeoPoint(52.0958, 23.7018)),
            Store("Арт-парк «Савлуков-Мебель»", "Арт-парк", "г. Новополоцк, ул. Василевцы 15", "ежедневно 10:00–21:00", "+375292481641", GeoPoint(55.5312, 28.6385)),
            Store("Фирменный салон «Савлуков-Мебель»", "Салон", "г. Солигорск, ул. Октябрьская 38", "ежедневно 10:00-21:00", "+375298381641", GeoPoint(52.7878, 27.5265)),
            Store("Фирменный салон «Савлуков-Мебель»", "Салон", "г. Минск, прт- Независимости 134", "ежедневно 10:00-22:00", "+375298681641", GeoPoint(53.9178, 27.5895)),
            Store("Арт-парк «Савлуков-Мебель»", "Арт-парк", "г. Минск, Свердлова 2", "ежедневно 10:00-21:00", "+375297841641", GeoPoint(53.9101, 27.5478)),
            Store("Арт-парк «Савлуков-Мебель»", "Арт-парк", "г. Минск, Победителей 102А", "ежедневно 10:00-21:00", "+375336161641", GeoPoint(53.9378, 27.4978)),
            Store("Арт-парк «Савлуков-Мебель»", "Арт-парк", "г. Гомель, ул. Рогачёвская 2", "ежедневно 10:00-21:00", "+375292991641", GeoPoint(52.4258, 30.9785))
        )
    }

    var darkTheme by remember { mutableStateOf(false) }

    val mapView = remember {
        Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", 0))
        MapView(context).apply {
            setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            setTilesScaledToDpi(true)
            setUseDataConnection(true)
        }
    }

    // Create custom marker icon
    fun createStoreIcon(store: Store): android.graphics.drawable.BitmapDrawable {
        val size = 80
        val bitmap = android.graphics.Bitmap.createBitmap(size, size, android.graphics.Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)
        val paint = android.graphics.Paint().apply {
            isAntiAlias = true
            style = android.graphics.Paint.Style.FILL
        }

        // Background circle
        val bgColor = if (store.type == "Арт-парк") android.graphics.Color.GREEN else android.graphics.Color.BLUE
        paint.color = bgColor
        canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint)

        // Border
        paint.color = android.graphics.Color.WHITE
        paint.style = android.graphics.Paint.Style.STROKE
        paint.strokeWidth = 3f
        canvas.drawCircle(size / 2f, size / 2f, size / 2f - 1.5f, paint)

        // Icon
        val iconText = if (store.type == "Арт-парк") "🏪" else "🛋️"
        paint.style = android.graphics.Paint.Style.FILL
        paint.color = android.graphics.Color.WHITE
        paint.textSize = 30f
        paint.textAlign = android.graphics.Paint.Align.CENTER
        val textY = size / 2f + 10f
        canvas.drawText(iconText, size / 2f, textY, paint)

        return android.graphics.drawable.BitmapDrawable(context.resources, bitmap)
    }

    // Update map
    LaunchedEffect(darkTheme) {
        mapView.overlays.clear()

        // Add store markers
        stores.forEach { store ->
            val marker = Marker(mapView)
            marker.position = store.position
            marker.title = store.name
            marker.snippet = "${store.type}\n${store.address}\n${store.hours}\n☎️ ${store.phone}"
            marker.icon = createStoreIcon(store)
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

            // Add click listener to zoom in on marker
            marker.setOnMarkerClickListener(object : Marker.OnMarkerClickListener {
                override fun onMarkerClick(marker: Marker?, mapView: MapView?): Boolean {
                    marker?.let {
                        val mapController = mapView?.controller
                        mapController?.animateTo(marker.position, 18.0, 1000L)
                    }
                    return true // Return true to consume the event
                }
            })

            mapView.overlays.add(marker)
        }

        mapView.invalidate()
    }

    // Initial map setup
    LaunchedEffect(Unit) {
        val mapController = mapView.controller
        mapController.setZoom(7.0)
        val centerPoint = GeoPoint(53.9, 27.6)
        mapController.setCenter(centerPoint)

        // Ограничить область просмотра Беларусью
        val belarusBounds = BoundingBox(
            56.17, // North (Максимальная широта Беларуси)
            32.78, // East (Максимальная долгота Беларуси)
            51.26, // South (Минимальная широта Беларуси)
            23.17  // West (Минимальная долгота Беларуси)
        )
        mapView.setScrollableAreaLimitDouble(belarusBounds)
        mapView.setMinZoomLevel(6.0) // Минимальный зум для обзора всей страны
        mapView.setMaxZoomLevel(19.0) // Максимальный зум для детального просмотра
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mapView.onPause()
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        // Header
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FloatingActionButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(40.dp),
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Назад",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Магазины Savlukov-Mebel",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = { darkTheme = !darkTheme }) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = "Сменить тему"
                    )
                }
            }
        }

        // Map
        AndroidView(
            factory = { mapView },
            modifier = Modifier.fillMaxSize()
        )
    }
}