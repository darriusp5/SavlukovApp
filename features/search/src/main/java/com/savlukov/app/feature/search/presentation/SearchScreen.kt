package com.savlukov.app.feature.search.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.savlukov.app.domain.model.Furniture

// Транслитерация русский ↔ английский
private fun transliterateRussianToEnglish(text: String): String {
    val replacements = mapOf(
        "zh" to "ж", "ts" to "ц", "ch" to "ч", "sh" to "ш", "sch" to "щ",
        "yu" to "ю", "ya" to "я", "a" to "а", "b" to "б", "v" to "в",
        "g" to "г", "d" to "д", "e" to "е", "z" to "з", "i" to "и",
        "y" to "й", "k" to "к", "l" to "л", "m" to "м", "n" to "н",
        "o" to "о", "p" to "п", "r" to "р", "s" to "с", "t" to "т",
        "u" to "у", "f" to "ф", "h" to "х"
    )

    var result = text
    replacements.forEach { (eng, rus) ->
        result = result.replace(eng, rus)
    }

    // Заглавные буквы
    replacements.forEach { (eng, rus) ->
        result = result.replace(eng.replaceFirstChar { it.uppercase() }, rus.replaceFirstChar { it.uppercase() })
    }

    return result
}

private fun transliterateEnglishToRussian(text: String): String {
    // Простая обратная транслитерация для основных случаев
    return text
        .replace("zh", "ж").replace("ts", "ц").replace("ch", "ч").replace("sh", "ш").replace("sch", "щ")
        .replace("yu", "ю").replace("ya", "я")
        .replace("a", "а").replace("b", "б").replace("v", "в").replace("g", "г").replace("d", "д")
        .replace("e", "э").replace("z", "з").replace("i", "и").replace("y", "й").replace("k", "к")
        .replace("l", "л").replace("m", "м").replace("n", "н").replace("o", "о").replace("p", "п")
        .replace("r", "р").replace("s", "с").replace("t", "т").replace("u", "у").replace("f", "ф")
        .replace("h", "х").replace("x", "кс")  // Добавлено для "x" -> "кс"
        // Заглавные буквы
        .replace("A", "А").replace("B", "Б").replace("V", "В").replace("G", "Г").replace("D", "Д")
        .replace("E", "Э").replace("Z", "З").replace("I", "И").replace("Y", "Й").replace("K", "К")
        .replace("L", "Л").replace("M", "М").replace("N", "Н").replace("O", "О").replace("P", "П")
        .replace("R", "Р").replace("S", "С").replace("T", "Т").replace("U", "У").replace("F", "Ф")
        .replace("H", "Х").replace("X", "КС")  // Заглавная X
        .replace("Zh", "Ж").replace("Ts", "Ц").replace("Ch", "Ч").replace("Sh", "Ш").replace("Sch", "Щ")
        .replace("Yu", "Ю").replace("Ya", "Я")
}

// Функция для создания всех возможных вариантов поиска
private fun createSearchVariants(text: String): Set<String> {
    val variants = mutableSetOf<String>()
    variants.add(text.lowercase()) // Оригинал
    variants.add(transliterateRussianToEnglish(text).lowercase()) // Русский → Английский
    variants.add(transliterateEnglishToRussian(text).lowercase()) // Английский → Русский
    return variants
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    products: List<Furniture>,
    onProductClick: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }

    // Popular search terms - specific models as requested
    val popularTags = remember {
        listOf(
            "Dolce", "FLY", "Fold", "Mercury",
            "Аляска", "Bonta Sleep", "Чикаго",
            "Диваны", "Кресла", "Тускан"
        )
    }

    // Real-time search filtering with transliteration
    val filteredProducts = remember(searchQuery, products) {
        if (searchQuery.isBlank()) {
            emptyList()
        } else {
            val searchLower = searchQuery.lowercase()
            val searchEnglish = transliterateRussianToEnglish(searchLower)

            android.util.Log.d("SearchScreen", "Search query: '$searchQuery' -> searchLower: '$searchLower', searchEnglish: '$searchEnglish'")

            products.filter { product ->
                // Проверяем все поля товара
                val productFields = listOfNotNull(
                    product.name,
                    product.category,
                    product.description,
                    product.fabric,
                    product.style,
                    product.mechanism
                )

                // Проверяем прямое совпадение и транслитерацию
                val matches = productFields.any { field ->
                    val fieldLower = field.lowercase()
                    val fieldTransliterated = transliterateEnglishToRussian(fieldLower)

                    // Прямое совпадение
                    val directMatch = fieldLower.contains(searchLower)
                    // Английский поиск в поле
                    val englishMatch = fieldLower.contains(searchEnglish)
                    // Обратная транслитерация - ищем русские буквы в английском тексте
                    val reverseTranslit = fieldTransliterated.contains(searchLower)
                    // Проверяем, содержит ли транслитерированное поле английский поисковый запрос
                    val translitEnglishMatch = fieldTransliterated.contains(searchEnglish)

                    val match = directMatch || englishMatch || reverseTranslit || translitEnglishMatch

                    if (match) {
                        android.util.Log.d("SearchScreen", "MATCH for '${product.name}': field='$fieldLower' -> translit='$fieldTransliterated', direct=$directMatch, english=$englishMatch, reverse=$reverseTranslit, translitEng=$translitEnglishMatch")
                    }

                    match
                }

                matches
            }.also { filtered ->
                android.util.Log.d("SearchScreen", "Search results: ${filtered.size} products found")
            }
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        // Simple header with back button and title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Назад"
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Поиск товаров",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        // Simple search text field
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Поиск диванов, кресел...") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        // Search Results
        if (searchQuery.isBlank()) {
            // Empty state with popular tags
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
                // Popular search tags
                Text(
                    text = "Популярные запросы",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 32.dp)
                ) {
                    items(popularTags) { tag ->
                        AssistChip(
                            onClick = { searchQuery = tag },
                            label = {
                                Text(
                                    tag,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }

                // Empty state illustration
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Начните поиск товаров",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Или выберите популярный запрос выше",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        } else if (filteredProducts.isEmpty()) {
            // No results
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Ничего не найдено",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Попробуйте изменить запрос",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        } else {
            // Results header
            Text(
                text = "Найдено ${filteredProducts.size} товаров",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
            )

            // Results list
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredProducts) { product ->
                    SearchResultItem(
                        product = product,
                        onClick = { onProductClick(product.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun SearchResultItem(
    product: Furniture,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Product Image
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // Product Info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Product Name
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Category badge
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = product.category,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                // Key features (dimensions, mechanism)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    product.length?.let {
                        Text(
                            text = "${it}×${product.width ?: 0}×${product.height ?: 0} см",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }

                    product.mechanism?.let {
                        Text(
                            text = "• $it",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }

                // Price
                product.price?.let { price ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            text = "${price.toInt()} BYN",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        product.oldPrice?.let { oldPrice ->
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${oldPrice.toInt()} BYN",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                textDecoration = TextDecoration.LineThrough
                            )
                        }

                        product.discountPercent?.let { discount ->
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                color = MaterialTheme.colorScheme.error,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "-${discount}%",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onError,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Action arrow
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}