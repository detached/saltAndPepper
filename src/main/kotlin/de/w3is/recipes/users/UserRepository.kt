package de.w3is.recipes.users

import de.w3is.recipes.common.model.PageRequest
import de.w3is.recipes.common.model.PageResponse
import de.w3is.recipes.users.model.User
import de.w3is.recipes.users.model.UserId

interface UserRepository {
    fun findUser(userName: String): User?

    fun store(user: User)

    fun update(user: User)

    fun getUser(userId: UserId): User

    fun getAllUsers(request: PageRequest): PageResponse<User>
}
