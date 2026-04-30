package com.savlukov.app.domain.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class Furniture(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val categoryId: String,
    val imageUrl: String,
    val arModelUrl: String,
    val price: Double? = null,
    val oldPrice: Double? = null,
    val discountPercent: Int? = null,
    // Габариты
    val length: Int? = null, // Длина в мм
    val width: Int? = null,  // Ширина в мм
    val height: Int? = null, // Высота в мм
    // Спальное место
    val bedLength: Int? = null, // Длина спального места в мм
    val bedWidth: Int? = null,  // Ширина спального места в мм
    // Технические характеристики
    val frame: String? = null,     // Каркас
    val upholstery: String? = null, // Наполнение подушек
    val legs: String? = null,      // Материал опор
    val mechanism: String? = null, // Механизм трансформации
    val maxLoad: Int? = null,      // Максимальная нагрузка в кг
    // Функциональность
    val storageBoxes: String? = null, // Ящик для белья
    val removableCover: String? = null, // Съемный чехол
    val decorativePillows: String? = null, // Декоративные подушки
    val sleepingPlace: String? = null, // Спальное место
    // Дизайн
    val fabric: String? = null,    // Ткань
    val style: String? = null,     // Стиль
    val seatsCount: String? = null, // Количество посадочных мест
    // Производство
    val manufacturer: String? = null // Производство
)

@Immutable
@Serializable
data class Fabric(
    val id: String,
    val name: String,
    val thumbnail: String,
    val textureUrl: String = "",
    val colorHex: String? = null
)

@Immutable
@Serializable
data class Story(
    val id: String,
    val title: String,
    val imageUrl: String,
    val videoUrl: String? = null,
    val isVideo: Boolean = false,
    val duration: Long = 5000L,
    val createdAt: String = "",
    val isActive: Boolean = true,
    val permalink: String? = null, // Instagram permalink
    val source: String = "local" // "instagram" or "local"
)

@Immutable
@Serializable
data class StorySegment(
    val id: String,
    val contentUrl: String,
    val targetProductId: String? = null,
    val textOverlay: String
)

@Immutable
@Serializable
data class Category(
    val id: String,
    val name: String,
    val parentId: String? = null
)
