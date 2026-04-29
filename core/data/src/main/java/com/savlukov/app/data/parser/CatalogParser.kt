package com.savlukov.app.data.parser

import android.content.Context
import com.savlukov.app.domain.model.Category
import com.savlukov.app.domain.model.Furniture
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory

class CatalogParser(private val context: Context) {

    data class CatalogData(
        val categories: List<Category> = emptyList(),
        val products: List<Furniture> = emptyList()
    )

    fun parseCatalog(): CatalogData {
        android.util.Log.e("SAVLUKOV_DEBUG", "=== STARTING CATALOG PARSING ===")
        android.util.Log.e("SAVLUKOV_DEBUG", "Opening catalog.txt from assets...")
        val input = context.assets.open("catalog.txt")
        android.util.Log.e("SAVLUKOV_DEBUG", "File opened successfully, size: ${input.available()} bytes")
        val result = parseXml(input)
        android.util.Log.e("SAVLUKOV_DEBUG", "=== FINISHED PARSING: ${result.products.size} products and ${result.categories.size} categories ===")
        return result
    }

    private fun parseXml(input: java.io.InputStream): CatalogData {
        val parser = XmlPullParserFactory.newInstance().newPullParser()
        parser.setInput(input, "UTF-8")
        val categories = mutableListOf<Category>()
        val products = mutableListOf<Furniture>()
        var eventType = parser.eventType

        android.util.Log.d("CatalogParser", "Starting XML parsing...")

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                when (parser.name) {
                    "category" -> {
                        val category = parseCategory(parser)
                        category?.let {
                            categories.add(it)
                            android.util.Log.d("CatalogParser", "Parsed category: ${it.name}")
                        }
                    }
                    "offer" -> {
                        val product = parseOffer(parser)
                        product?.let {
                            products.add(it)
                            android.util.Log.d("CatalogParser", "Parsed product: ${it.name}")
                        }
                    }
                }
            }
            eventType = parser.next()
        }

        android.util.Log.d("CatalogParser", "Finished parsing: ${categories.size} categories, ${products.size} products")
        return CatalogData(categories, products)
    }

    private fun parseCategory(parser: XmlPullParser): Category? {
        val id = parser.getAttributeValue(null, "id") ?: return null
        val parentId = parser.getAttributeValue(null, "parentId")
        val name = parser.nextText()
        return Category(id, name, parentId)
    }

    private fun parseOffer(parser: XmlPullParser): Furniture? {
        var id = parser.getAttributeValue(null, "id") ?: ""
        var name = ""
        var category = ""
        var categoryId = ""
        var imageUrl = ""
        var price: Double? = null
        var oldPrice: Double? = null
        var arModelUrl = "https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/SheenChair/glTF-Binary/SheenChair.glb"

        // Новые поля
        var length: Int? = null
        var width: Int? = null
        var height: Int? = null
        var bedLength: Int? = null
        var bedWidth: Int? = null
        var frame: String? = null
        var upholstery: String? = null
        var legs: String? = null
        var mechanism: String? = null
        var maxLoad: Int? = null
        var storageBoxes: String? = null
        var removableCover: String? = null
        var decorativePillows: String? = null
        var sleepingPlace: String? = null
        var fabric: String? = null
        var style: String? = null
        var seatsCount: String? = null
        var manufacturer: String? = null

        while (!(parser.eventType == XmlPullParser.END_TAG && parser.name == "offer")) {
            if (parser.eventType == XmlPullParser.START_TAG) {
                when (parser.name) {
                    "model" -> name = parser.nextText()
                    "typePrefix" -> category = parser.nextText()
                    "categoryId" -> categoryId = parser.nextText()
                    "picture" -> imageUrl = parser.nextText()
                    "price" -> {
                        val priceText = parser.nextText()
                        price = priceText.toDoubleOrNull()
                    }
                    "oldprice" -> {
                        val oldPriceText = parser.nextText()
                        oldPrice = oldPriceText.toDoubleOrNull()
                    }
                    "param" -> {
                        val paramName = parser.getAttributeValue(null, "name")
                        val paramValue = parser.nextText()
                        when (paramName) {
                            "Длина" -> length = paramValue.toIntOrNull()
                            "Ширина" -> width = paramValue.toIntOrNull()
                            "Высота" -> height = paramValue.toIntOrNull()
                            "Длина спального места" -> bedLength = paramValue.toIntOrNull()
                            "Ширина спального места" -> bedWidth = paramValue.toIntOrNull()
                            "Каркас" -> frame = paramValue
                            "Наполнение подушек" -> upholstery = paramValue
                            "Материал опор" -> legs = paramValue
                            "Механизм трансформации" -> mechanism = paramValue
                            "Максимальная нагрузка на спальное место" -> maxLoad = paramValue.toIntOrNull()
                            "Ящик для белья" -> storageBoxes = paramValue
                            "Съемный чехол" -> removableCover = paramValue
                            "Декоративные подушки" -> decorativePillows = paramValue
                            "Спальное место" -> sleepingPlace = paramValue
                            "Ткань" -> fabric = paramValue
                            "Стиль" -> style = paramValue
                            "Количество посадочных мест" -> seatsCount = paramValue
                            "Производство" -> manufacturer = paramValue
                        }
                    }
                }
            }
            parser.next()
        }

        val discountPercent = if (price != null && oldPrice != null && oldPrice > price) {
            ((oldPrice - price) / oldPrice * 100).toInt()
        } else null

        return if (id.isNotEmpty() && name.isNotEmpty()) {
            Furniture(
                id = id,
                name = name,
                description = "Premium furniture from Savlukov",
                category = category,
                categoryId = categoryId,
                imageUrl = imageUrl,
                arModelUrl = arModelUrl,
                price = price,
                oldPrice = oldPrice,
                discountPercent = discountPercent,
                length = length,
                width = width,
                height = height,
                bedLength = bedLength,
                bedWidth = bedWidth,
                frame = frame,
                upholstery = upholstery,
                legs = legs,
                mechanism = mechanism,
                maxLoad = maxLoad,
                storageBoxes = storageBoxes,
                removableCover = removableCover,
                decorativePillows = decorativePillows,
                sleepingPlace = sleepingPlace,
                fabric = fabric,
                style = style,
                seatsCount = seatsCount,
                manufacturer = manufacturer
            )
        } else null
    }
}