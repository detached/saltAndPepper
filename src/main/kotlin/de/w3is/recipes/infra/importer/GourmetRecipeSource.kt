package de.w3is.recipes.infra.importer

import de.w3is.recipes.application.importer.ImportCommandProvider
import de.w3is.recipes.application.importer.ImportRecipe
import java.io.InputStream
import java.util.*
import javax.xml.stream.XMLEventReader
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.events.EndElement
import javax.xml.stream.events.StartElement
import javax.xml.stream.events.XMLEvent

class GourmetRecipeSource(inputStream: InputStream) : ImportCommandProvider {

    private val reader: XMLEventReader = XMLInputFactory.newInstance().createXMLEventReader(inputStream)

    override fun hasNext() = reader.findElement("recipe")

    override fun next(): ImportRecipe {

        val builder = ImportRecipeBuilder()

        reader.readUntil({ it.isEndElement("recipe") }) {

            if (it.isStartElement) {
                builder.appendInfoFrom(it)
            }
        }

        return builder.build()
    }

    private fun ImportRecipeBuilder.appendInfoFrom(event: XMLEvent) {
        when (event.asStartElement().name()) {
            "title" -> title = reader.nextAsString()
            "category" -> category = reader.nextAsString()
            "cuisine" -> cuisine = reader.nextAsString()
            "yields" -> yields = reader.nextAsString()
            "image" -> image = parseImage()
            "ingredient-list" -> ingredients = parseIngredients()
            "instructions" -> instructions = parseMultilineInstructions()
            "modifications" -> modifications = reader.nextAsString()
        }
    }

    private fun parseImage(): InputStream {

        val encodedImage = StringBuilder()
        reader.readUntil({ it.isEndElement("image")}) {event ->
            encodedImage.append(event.asCharacters().toString().trim())
        }

        return Base64.getDecoder().decode(encodedImage.toString()).inputStream()
    }

    private fun parseMultilineInstructions(): String {
        val content = StringBuilder()
        reader.readUntil({ it.isEndElement("instructions") }) { event ->
            content.append(event.asCharacters().toString().replace("\t", ""))
        }
        return content.toString().trim()
    }

    private fun parseIngredients(): String {

        val ingredients = java.lang.StringBuilder()

        reader.readUntil({ it.isEndElement("ingredient-list") }) { event ->

            if (event.isStartElement("ingredient")) {
                parseIngredient(reader).let { ingredients.append(it) }
            }
        }

        return ingredients.toString().trim()
    }

    private fun parseIngredient(reader: XMLEventReader): String {

        var item = ""
        var unit = ""
        var amount = ""

        reader.readUntil({ it.isEndElement("ingredient") }) {

            if (it.isStartElement) {
                when (it.asStartElement().name()) {
                    "item" -> item = reader.nextAsString()
                    "unit" -> unit = reader.nextAsString()
                    "amount" -> amount = reader.nextAsString()
                }
            }
        }

        return "$unit $item $amount".trim()
    }

    private fun XMLEventReader.readUntil(condition: (XMLEvent) -> (Boolean), block: (XMLEvent) -> (Unit)) {
        while (this.hasNext()) {
            val event = nextEvent()
            if (condition(event)) {
                break
            }
            block(event)
        }
    }

    private fun XMLEventReader.findElement(element: String): Boolean {

        while (this.hasNext()) {
            with(nextEvent()) {
                if (this.isStartElement(element)) {
                    return true
                }
            }
        }

        return false
    }

    private fun XMLEvent.isStartElement(name: String) = isStartElement && asStartElement().name() == name
    private fun XMLEvent.isEndElement(name: String) = isEndElement && asEndElement().name() == name
    private fun XMLEventReader.nextAsString() = nextEvent().asCharacters().toString().trim()
    private fun StartElement.name() = name.localPart.lowercase()
    private fun EndElement.name() = name.localPart.lowercase()

}

class ImportRecipeBuilder {
    var title: String? = null
    var category: String? = null
    var cuisine: String? = null
    var yields: String? = null
    var image: InputStream? = null
    var ingredients: String? = null
    var instructions: String? = null
    var modifications: String? = null

    fun build() = ImportRecipe(
            title = title!!,
            category = category.orEmpty(),
            cuisine = cuisine.orEmpty(),
            yields = yields.orEmpty(),
            image = image,
            ingredients = ingredients.orEmpty(),
            instructions = instructions.orEmpty(),
            modifications = modifications.orEmpty()
    )
}