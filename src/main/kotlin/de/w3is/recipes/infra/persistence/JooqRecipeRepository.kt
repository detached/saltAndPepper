package de.w3is.recipes.infra.persistence

import com.fasterxml.jackson.databind.ObjectMapper
import de.w3is.recipes.domain.RecipeRepository
import de.w3is.recipes.domain.model.*
import de.w3is.recipes.infra.persistence.generated.public_.Tables.RECIPES
import de.w3is.recipes.infra.persistence.generated.public_.tables.records.RecipesRecord
import org.jooq.DSLContext
import org.jooq.RecordMapper
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.ceil

@Singleton
class JooqRecipeRepository(
    @Inject private val dslContext: DSLContext,
    @Inject private val objectMapper: ObjectMapper
) : RecipeRepository {

    private val recordMapper = RecordMapper<RecipesRecord, Recipe> { record ->
        Recipe(
            id = RecipeId(record.recipeId),
            title = record.title,
            category = record.category,
            cuisine = record.cuisine,
            yields = record.yields,
            ingredients = record.ingredients,
            instructions = record.instructions,
            modifications = record.modifications,
            images = objectMapper.readValue(record.images, Array<ImageId>::class.java).toMutableList(),
            authorId = AuthorId(record.authorId)
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

    override fun search(query: String, limit: Int, page: Int): SearchResponse {

        val searchQuery = dslContext.selectFrom(RECIPES)

        if (query.isNotBlank()) {
            searchQuery.where(query.split(" ").map { queryPart ->
                RECIPES.TITLE.containsIgnoreCase(queryPart)
                    .or(RECIPES.CATEGORY.containsIgnoreCase(queryPart))
                    .or(RECIPES.CUISINE.containsIgnoreCase(queryPart))
            }.reduce { acc, v -> acc.and(v) })
        }

        val maxResults = searchQuery.count()
        val results = searchQuery
            .limit(limit)
            .offset(page * limit)
            .fetch(recordMapper)


        return SearchResponse(
            results = results,
            page = Page(
                current = page,
                max = ceil(maxResults.toDouble() / limit).toInt() - 1,
                size = results.size
            )
        )
    }

    override fun delete(recipe: Recipe) {
        dslContext.deleteFrom(RECIPES).where(RECIPES.RECIPE_ID.eq(recipe.id.recipeId)).execute()
    }
}