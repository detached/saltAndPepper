package de.w3is.recipes.infra.persistence

import com.fasterxml.jackson.databind.ObjectMapper
import de.w3is.recipes.domain.RecipeRepository
import de.w3is.recipes.domain.model.AuthorId
import de.w3is.recipes.domain.model.ImageId
import de.w3is.recipes.domain.model.Recipe
import de.w3is.recipes.domain.model.RecipeId
import de.w3is.recipes.infra.persistence.generated.public_.Tables.RECIPES
import de.w3is.recipes.infra.persistence.generated.public_.tables.records.RecipesRecord
import org.jooq.DSLContext
import org.jooq.RecordMapper
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.streams.asSequence

@Singleton
class JooqRecipeRepository(
    @Inject private val dslContext: DSLContext,
    @Inject private val objectMapper: ObjectMapper
) : RecipeRepository {

    private val recordMapper = RecordMapper<RecipesRecord?, Recipe?> { record ->
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

    override fun getAll(): Sequence<Recipe> = dslContext.selectFrom(RECIPES)
        .fetchStream().asSequence()
        .mapNotNull { recordMapper.map(it) }

    override fun search(queryString: String, maxResults: Int): Sequence<Recipe> {

        val searchQuery = dslContext.selectFrom(RECIPES)

        if (queryString.isNotBlank()) {
            searchQuery.where(
                RECIPES.TITLE.containsIgnoreCase(queryString)
                    .or(
                        RECIPES.CATEGORY.containsIgnoreCase(queryString)
                            .or(RECIPES.CUISINE.containsIgnoreCase(queryString))
                    )
            )
        }

        return searchQuery
            .maxRows(maxResults)
            .fetchStream().asSequence()
            .mapNotNull { recordMapper.map(it) }
    }

    override fun delete(recipe: Recipe) {
        dslContext.deleteFrom(RECIPES).where(RECIPES.RECIPE_ID.eq(recipe.id.recipeId)).execute()
    }
}