package de.w3is.recipes.comments.infra.api

import assertk.all
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.first
import assertk.assertions.hasSize
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import de.w3is.recipes.recipes.RecipeContent
import de.w3is.recipes.recipes.RecipeService
import de.w3is.recipes.recipes.model.Recipe
import de.w3is.recipes.users.UserService
import de.w3is.recipes.users.model.User
import de.w3is.recipes.utils.AuthenticationClient
import de.w3is.recipes.utils.DBCleaner
import de.w3is.recipes.utils.TestUserProvider
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.kotlin.http.retrieveObject
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.jooq.DSLContext
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@MicronautTest
class CommentsControllerTest(
    @Client("/api") val client: HttpClient,
    @Inject val recipeService: RecipeService,
    @Inject override val userService: UserService,
    @Inject override val authenticationClient: AuthenticationClient,
    @Inject override val dslContext: DSLContext,
) : TestUserProvider, DBCleaner {
    private val userName = "testUser"
    private val password = "password"

    @BeforeEach
    fun setUp() {
        cleanDb()
    }

    @Test
    fun `given no comment when create new comment then list of comment contains new comment`() {
        val (user, token) = setupUser(userName, password)
        val recipe = givenRecipe(user)

        val beforeAddingComment =
            client.toBlocking().retrieveObject<CommentsViewModel>(
                HttpRequest.GET<Any>("/comments/recipe/${recipe.id.recipeId}")
                    .bearerAuth(token.accessToken)
                    .accept("application/json"),
            )

        assertThat(beforeAddingComment.comments).isEmpty()

        val comment = "test comment"
        val createdComment =
            client.toBlocking().retrieveObject<CommentViewModel>(
                HttpRequest
                    .POST<Any>(
                        "/comments/recipe/${recipe.id.recipeId}",
                        CreateCommentViewModel(comment),
                    )
                    .bearerAuth(token.accessToken)
                    .accept("application/json")
                    .contentType("application/json"),
            )

        assertThat(createdComment.comment).isEqualTo(comment)

        val afterAddingComment =
            client.toBlocking().retrieveObject<CommentsViewModel>(
                HttpRequest.GET<Any>("/comments/recipe/${recipe.id.recipeId}")
                    .bearerAuth(token.accessToken)
                    .accept("application/json"),
            )

        assertThat(afterAddingComment.comments).all {
            hasSize(1)
            first().isEqualTo(createdComment)
        }
    }

    @Test
    fun `given comment when creating new comment then new comment should be sorted first`() {
        val (user, token) = setupUser(userName, password)
        val recipe = givenRecipe(user)

        client.toBlocking().retrieveObject<CommentViewModel>(
            HttpRequest
                .POST<Any>(
                    "/comments/recipe/${recipe.id.recipeId}",
                    CreateCommentViewModel("first"),
                )
                .bearerAuth(token.accessToken)
                .accept("application/json")
                .contentType("application/json"),
        )

        client.toBlocking().retrieveObject<CommentViewModel>(
            HttpRequest
                .POST<Any>(
                    "/comments/recipe/${recipe.id.recipeId}",
                    CreateCommentViewModel("second"),
                )
                .bearerAuth(token.accessToken)
                .accept("application/json")
                .contentType("application/json"),
        )

        val comments =
            client.toBlocking().retrieveObject<CommentsViewModel>(
                HttpRequest.GET<Any>("/comments/recipe/${recipe.id.recipeId}")
                    .bearerAuth(token.accessToken)
                    .accept("application/json"),
            )

        assertThat(comments.comments.map { it.comment }).containsExactly("second", "first")
    }

    @Test
    fun `given a comment when deleting the comment the recipe should not have a comment`() {
        val (user, token) = setupUser(userName, password)
        val recipe = givenRecipe(user)

        client.toBlocking().retrieveObject<CommentViewModel>(
            HttpRequest
                .POST<Any>(
                    "/comments/recipe/${recipe.id.recipeId}",
                    CreateCommentViewModel("test comment"),
                )
                .bearerAuth(token.accessToken)
                .accept("application/json")
                .contentType("application/json"),
        )

        val beforeDeletion =
            client.toBlocking().retrieveObject<CommentsViewModel>(
                HttpRequest.GET<Any>("/comments/recipe/${recipe.id.recipeId}")
                    .bearerAuth(token.accessToken)
                    .accept("application/json"),
            )

        assertThat(beforeDeletion.comments).hasSize(1)
        val comment = beforeDeletion.comments.first()

        client.toBlocking().exchange<Any, Any>(
            HttpRequest.DELETE<Any>("/comments/recipe/${recipe.id.recipeId}/comment/${comment.id}")
                .bearerAuth(token.accessToken),
        )

        val afterDeletion =
            client.toBlocking().retrieveObject<CommentsViewModel>(
                HttpRequest.GET<Any>("/comments/recipe/${recipe.id.recipeId}")
                    .bearerAuth(token.accessToken)
                    .accept("application/json"),
            )

        assertThat(afterDeletion.comments).isEmpty()
    }

    @Test
    fun `when the comment is created by user then it can be deleted`() {
        val (user, token) = setupUser(userName, password)
        val recipe = givenRecipe(user)

        val comment = "test comment"
        client.toBlocking().retrieveObject<CommentViewModel>(
            HttpRequest
                .POST<Any>(
                    "/comments/recipe/${recipe.id.recipeId}",
                    CreateCommentViewModel(comment),
                )
                .bearerAuth(token.accessToken)
                .accept("application/json")
                .contentType("application/json"),
        )
        val comments =
            client.toBlocking().retrieveObject<CommentsViewModel>(
                HttpRequest.GET<Any>("/comments/recipe/${recipe.id.recipeId}")
                    .bearerAuth(token.accessToken)
                    .accept("application/json"),
            )

        assertThat(comments.comments.first().canDelete).isTrue()
    }

    @Test
    fun `when the comment is created by other user then it can't be deleted`() {
        val (user, token) = setupUser(userName, password)
        val (_, token2) = setupUser("${userName}2", password)
        val recipe = givenRecipe(user)

        val comment = "test comment"
        client.toBlocking().retrieveObject<CommentViewModel>(
            HttpRequest
                .POST<Any>(
                    "/comments/recipe/${recipe.id.recipeId}",
                    CreateCommentViewModel(comment),
                )
                .bearerAuth(token2.accessToken)
                .accept("application/json")
                .contentType("application/json"),
        )
        val comments =
            client.toBlocking().retrieveObject<CommentsViewModel>(
                HttpRequest.GET<Any>("/comments/recipe/${recipe.id.recipeId}")
                    .bearerAuth(token.accessToken)
                    .accept("application/json"),
            )

        assertThat(comments.comments.first().canDelete).isFalse()
    }

    private fun givenRecipe(user: User): Recipe {
        return recipeService.createNewRecipe(
            RecipeContent(
                title = "",
                category = "",
                cuisine = "",
                yields = "",
                ingredients = "",
                instructions = "",
                modifications = "",
                images = emptyList(),
            ),
            user,
        )
    }
}
