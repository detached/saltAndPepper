package de.w3is.recipes.recipes.infra.persistence

import com.fasterxml.jackson.databind.ObjectMapper
import de.w3is.recipes.images.ImageId
import de.w3is.recipes.infra.persistence.generated.tables.Recipes.Companion.RECIPES
import de.w3is.recipes.infra.persistence.generated.tables.records.RecipesRecord
import de.w3is.recipes.recipes.*
import de.w3is.recipes.recipes.model.*
import jakarta.inject.Singleton
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.RecordMapper
import org.jooq.impl.DSL
import kotlin.math.ceil

@Singleton
class JooqRecipeRepository(
    private val dslContext: DSLContext,
    private val objectMapper: ObjectMapper
) : RecipeRepository {

    private val filterKeysToColumns = mapOf(
        FilterKey.AUTHOR to RECIPES.AUTHOR_ID,
        FilterKey.CUISINE to RECIPES.CUISINE,
        FilterKey.CATEGORY to RECIPES.CATEGORY
    )

    private val recordMapper = RecordMapper<RecipesRecord, Recipe> { record ->
        Recipe(
            id = RecipeId(record.recipeId!!),
            title = record.title!!,
            category = record.category!!,
            cuisine = record.cuisine!!,
            yields = record.yields!!,
            ingredients = record.ingredients!!,
            instructions = record.instructions!!,
            modifications = record.modifications!!,
            images = objectMapper.readValue(record.images, Array<ImageId>::class.java).toMutableList(),
            authorId = AuthorId(record.authorId!!)
        )
    }

    override fun store(recipe: Recipe) {
        val existingRecord = queryRecipe(recipe.id).fetchOne()

        if (existingRecord == null) {
            dslContext.newRecord(RECIPES).apply {
                recipeId = recipe.id.recipeId
                title = recipe.title
                category = recipe.category
                cuisine = recipe.cuisine
                yields = recipe.yields
                instructions = recipe.instructions
                modifications = recipe.modifications
                ingredients = recipe.ingredients
                images = objectMapper.writeValueAsString(recipe.getImages().toTypedArray())
                authorId = recipe.authorId.value
            }.store()
        } else {
            existingRecord.apply {
                title = recipe.title
                category = recipe.category
                cuisine = recipe.cuisine
                yields = recipe.yields
                instructions = recipe.instructions
                modifications = recipe.modifications
                ingredients = recipe.ingredients
                images = objectMapper.writeValueAsString(recipe.getImages().toTypedArray())
                authorId = recipe.authorId.value
            }.store()
        }
    }

    private fun queryRecipe(recipeId: RecipeId) = dslContext.selectFrom(RECIPES)
        .where(RECIPES.RECIPE_ID.equal(recipeId.recipeId))

    override fun get(recipeId: RecipeId): Recipe = queryRecipe(recipeId).fetchOne(recordMapper)
        ?: error("No Recipe found for $recipeId")

    override fun getAll(): List<Recipe> = dslContext.selectFrom(RECIPES).fetch(recordMapper)

    override fun search(searchRequest: SearchRequest): SearchResponse {
        return with(searchRequest) {

            var searchConditions = if (query.isBlank()) {
                DSL.trueCondition()
            } else {
                query.split(" ").map { queryPart ->
                    RECIPES.TITLE.containsIgnoreCase(queryPart)
                        .or(RECIPES.CATEGORY.containsIgnoreCase(queryPart))
                        .or(RECIPES.CUISINE.containsIgnoreCase(queryPart))
                }.reduce { acc, v -> acc.and(v) }
            }

            if (filter.isNotEmpty()) {
                val filterConditions = filter.mapNotNull { (key, values) ->
                    values.map { value ->
                        filterKeysToColumns[key]!!.contains(value)
                    }.reduceOrNull { acc: Condition, v: Condition -> acc.or(v) }
                }.reduceOrNull { acc: Condition, v: Condition -> acc.and(v) }

                if (filterConditions != null) {
                    searchConditions = searchConditions.and(filterConditions)
                }
            }

            val maxResults = dslContext.selectFrom(RECIPES).where(searchConditions).count()
            val possibleFilter = findPossibleFilterValues(searchConditions)
            val results = dslContext.selectFrom(RECIPES).where(searchConditions)
                .orderBy(RECIPES.TITLE.asc())
                .limit(limit)
                .offset(page * limit)
                .fetch(recordMapper)

            SearchResponse(
                results = results,
                page = Page(
                    current = page,
                    max = ceil(maxResults.toDouble() / limit).toInt() - 1,
                    size = results.size
                ),
                possibleFilter = possibleFilter
            )
        }
    }

    private fun findPossibleFilterValues(
        searchConditions: Condition
    ): Map<FilterKey, List<String>> {

        val result = dslContext.select(
            DSL.arrayAggDistinct(RECIPES.AUTHOR_ID).`as`(FilterKey.AUTHOR.name),
            DSL.arrayAggDistinct(RECIPES.CATEGORY.cast(String::class.java)).`as`(FilterKey.CATEGORY.name),
            DSL.arrayAggDistinct(RECIPES.CUISINE.cast(String::class.java)).`as`(FilterKey.CUISINE.name)
        ).from(RECIPES).where(searchConditions)
            .fetchOne() ?: return emptyMap()

        return mapOf(
            FilterKey.AUTHOR to result.get(FilterKey.AUTHOR.name, Array<String>::class.java).toList(),
            FilterKey.CUISINE to result.get(FilterKey.CUISINE.name, Array<String>::class.java).toList(),
            FilterKey.CATEGORY to result.get(FilterKey.CATEGORY.name, Array<String>::class.java).toList()
        )
    }

    override fun delete(recipe: Recipe) {
        dslContext.deleteFrom(RECIPES).where(RECIPES.RECIPE_ID.eq(recipe.id.recipeId)).execute()
    }
}