package de.w3is.recipes.recipes

import de.w3is.recipes.recipes.model.Author
import de.w3is.recipes.recipes.model.AuthorId

interface AuthorRepository {

    fun get(authorId: AuthorId): Author

    fun get(authorIds: Set<AuthorId>): Set<Author>
}