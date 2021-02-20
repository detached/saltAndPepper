package de.w3is.recipes.domain

import de.w3is.recipes.domain.model.Author
import de.w3is.recipes.domain.model.AuthorId
import de.w3is.recipes.domain.model.ImageId
import java.io.InputStream

interface AuthorRepository {

    fun get(authorId: AuthorId): Author
}